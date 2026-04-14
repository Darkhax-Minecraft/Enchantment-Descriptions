package net.darkhax.enchdesc.common.impl;

import net.darkhax.enchdesc.common.mixin.patch.AccessorAbstractContainerScreen;
import net.darkhax.pricklemc.common.api.config.ConfigManager;
import net.darkhax.pricklemc.common.api.util.CachedSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EnchantmentDescriptionsMod {

    public static final String MOD_ID = "enchdesc";
    public static final String MOD_NAME = "EnchantmentDescriptions";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final String[] KEY_TYPES = {"desc", "description", "info"};
    public static final Config config = ConfigManager.load(MOD_ID, new Config());
    private static final Set<String> missingEnchants = ConcurrentHashMap.newKeySet();

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
        return config.enabled && hasEnchantments(getHoveredStack()) && (!config.only_on_books || getHoveredStack().getItem() == Items.ENCHANTED_BOOK) && (!config.only_in_enchanting_table || Minecraft.getInstance().screen instanceof EnchantmentScreen);
    }

    public static boolean hasEnchantments(ItemStack stack) {
        final ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        final ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
        return (enchantments != null && !enchantments.isEmpty()) || (stored != null && !stored.isEmpty());
    }

    public static Component getKeybindText() {
        return config.activate_text;
    }

    public static boolean isKeybindConditionMet() {
        return !config.require_keybind || Minecraft.getInstance().hasShiftDown();
    }

    public static void insertDescriptions(Holder<Enchantment> enchantment, int level, Consumer<Component> lines) {
        if (canDisplayDescription() && isKeybindConditionMet()) {
            MutableComponent description = getDescription(enchantment, enchantment.unwrapKey().orElseThrow().identifier(), level);
            if (description != null) {
                description = ComponentUtils.mergeStyles(description, config.style);
                lines.accept(config.prefix.copy().append(description).append(config.suffix));
            }
        }
    }

    @Nullable
    private static MutableComponent getDescription(Holder<Enchantment> enchantment, Identifier id, int level) {
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
        if (!missingEnchants.contains(baseKey)) {
            LOG.warn("Enchantment {} does not have a description!", baseKey);
            missingEnchants.add(baseKey);
        }
        return null;
    }
}