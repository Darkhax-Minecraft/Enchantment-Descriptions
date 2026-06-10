package net.darkhax.enchdesc.common.impl;

import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.enchdesc.common.api.ContextProvider;
import net.darkhax.pricklemc.common.api.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EnchdescMod {

    private static final String[] KEY_TYPES = {"desc", "description", "info"};

    private static EnchdescMod instance;
    private boolean hasInitialized = false;
    private Config config;

    public void init() {
        if (hasInitialized) {
            throw new IllegalStateException("The " + Constants.MOD_NAME + " has already been initialized.");
        }
        if (Services.PLATFORM.isPhysicalClient()) {
            config = ConfigManager.load(Constants.MOD_ID, new Config());
        }
        hasInitialized = true;
    }

    public void setupContext(ItemStack stack) {
        if (this.canDisplayDescription(stack) && this.isKeybindConditionMet()) {
            if (stack.getEnchantments() instanceof ContextProvider provider) {
                provider.enchdesc$setStack(stack);
            }
            if (stack.get(DataComponents.STORED_ENCHANTMENTS) instanceof ContextProvider provider) {
                provider.enchdesc$setStack(stack);
            }
        }
    }

    public void revertContext(ItemStack stack) {
        if (stack.getEnchantments() instanceof ContextProvider provider) {
            provider.enchdesc$setStack(ItemStack.EMPTY);
        }
        if (stack.get(DataComponents.STORED_ENCHANTMENTS) instanceof ContextProvider provider) {
            provider.enchdesc$setStack(ItemStack.EMPTY);
        }
    }

    public boolean canDisplayDescription(ItemStack stack) {
        return hasInitialized &&
               config.enabled &&
               hasEnchantments(stack) &&
               (!config.only_on_books || stack.getItem() instanceof EnchantedBookItem) &&
               (!config.only_in_enchanting_table || Minecraft.getInstance().screen instanceof EnchantmentScreen);
    }

    public Component getKeybindText() {
        return this.config.activate_text;
    }

    public boolean isKeybindConditionMet() {
        return !this.config.require_keybind || Screen.hasShiftDown();
    }

    private boolean hasEnchantments(ItemStack stack) {
        return !stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty() || !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
    }

    @Nullable
    private ResourceKey<Enchantment> getKey(Holder<Enchantment> holder) {
        if (holder.kind() == Holder.Kind.REFERENCE) {
            return holder.unwrapKey().orElseThrow();
        } else {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                return level.registryAccess().registry(Registries.ENCHANTMENT).orElseThrow().getResourceKey(holder.unwrap().right().orElseThrow()).orElseThrow();
            }
        }
        return null;
    }

    public void insertDescriptions(Holder<Enchantment> enchantment, int level, Consumer<Component> lines) {
        ResourceKey<Enchantment> key = getKey(enchantment);
        if (key == null) {
            return;
        }
        final MutableComponent description = getDescription(enchantment, key.location(), level);
        if (description != null) {
            ComponentUtils.mergeStyles(description, config.style);
            lines.accept(config.prefix.copy().append(description).append(config.suffix));
        }
    }

    @Nullable
    private MutableComponent getDescription(Holder<Enchantment> enchantment, ResourceLocation id, int level) {
        MutableComponent description = getDescription("enchantment." + id.getNamespace() + "." + id.getPath() + ".", level);
        if (description == null && enchantment.value().description().getContents() instanceof TranslatableContents translatable) {
            description = getDescription(translatable.getKey() + ".", level);
        }
        return description;
    }

    @Nullable
    private MutableComponent getDescription(String baseKey, int level) {
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

    public static EnchdescMod getInstance() {
        if (instance == null) {
            instance = new EnchdescMod();
        }
        return instance;
    }
}