package net.darkhax.enchdesc.common.impl;

import net.darkhax.enchdesc.common.mixin.patch.AccessorAbstractContainerScreen;
import net.darkhax.pricklemc.common.api.config.ConfigManager;
import net.darkhax.pricklemc.common.api.util.CachedSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EnchantmentDescriptionsMod {

    public static final String MOD_ID = "enchdesc";
    public static final String MOD_NAME = "EnchantmentDescriptions";
    public static final String[] KEY_TYPES = {"desc", "description", "info"};
    public static final CachedSupplier<Config> config = CachedSupplier.cache(() -> ConfigManager.load(MOD_ID, new Config()));

    /**
     * Attempts to get the stack in the slot the player is currently hovering over. If the slot does not exist this will
     * be empty.
     *
     * @return The item in the currently hovered slot.
     */
    public static ItemStack getHoveredStack() {
        if (Minecraft.getInstance().screen instanceof AccessorAbstractContainerScreen accessor) {
            final Slot slot = accessor.enchdesc$hoveredSlot();
            if (slot != null && slot.hasItem()) {
                return slot.getItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean canDisplayDescription() {
        final Config cfg = config.get();
        return cfg.enabled &&
               (!cfg.only_on_books || getHoveredStack().getItem() == Items.ENCHANTED_BOOK) &&
               (!cfg.only_in_enchanting_table || Minecraft.getInstance().screen instanceof EnchantmentScreen);
    }

    public static Component getKeybindText() {
        return config.get().activate_text;
    }

    public static boolean isKeybindConditionMet() {
        return !config.get().require_keybind || Screen.hasShiftDown();
    }

    public static void insertDescriptions(Holder<Enchantment> enchantment, int level, Consumer<Component> lines) {
        if (canDisplayDescription()) {
            final MutableComponent description = getDescription(enchantment, enchantment.unwrapKey().orElseThrow().location(), level);
            if (description != null) {
                final Config cfg = config.get();
                ComponentUtils.mergeStyles(description, cfg.style);
                lines.accept(cfg.prefix.copy().append(description).append(cfg.suffix));
            }
        }
    }

    @Nullable
    private static MutableComponent getDescription(Holder<Enchantment> enchantment, ResourceLocation id, int level) {
        MutableComponent description = getDescription("enchantment." + id.getNamespace() + "." + id.getPath() + ".", level);
        if (description == null && enchantment.value().description().getContents() instanceof TranslatableContents translatable) {
            description = getDescription(translatable.getKey() + ".", level);
        }
        return description;
    }

    @Nullable
    private static MutableComponent getDescription(String baseKey, int level) {
        for (String keyType : KEY_TYPES) {
            String key = baseKey + keyType;
            if (I18n.exists(key)) {
                return Component.translatable(key);
            }
            key = key + "." + level;
            if (I18n.exists(key)) {
                return Component.translatable(key);
            }
        }
        return null;
    }
}