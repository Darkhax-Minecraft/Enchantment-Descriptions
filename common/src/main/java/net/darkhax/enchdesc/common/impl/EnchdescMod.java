package net.darkhax.enchdesc.common.impl;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.darkhax.bookshelf.common.api.PhysicalSide;
import net.darkhax.bookshelf.common.api.annotation.OnlyFor;
import net.darkhax.bookshelf.common.api.service.Services;
import net.darkhax.pricklemc.common.api.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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


    @OnlyFor(PhysicalSide.CLIENT)
    public void insertDescriptions(ItemStack stack, List<Component> lines) {
        if (this.hasInitialized && this.config.enabled && hasEnchantments(stack)) {
            if (config.only_on_books && !(stack.getItem() instanceof EnchantedBookItem)) {
                return;
            }
            if (config.only_in_enchanting_table && !(Minecraft.getInstance().screen instanceof EnchantmentScreen)) {
                return;
            }
            if (config.require_keybind && !Screen.hasShiftDown()) {
                if (config.activate_text.getContents() != PlainTextContents.EMPTY) {
                    lines.add(config.activate_text);
                }
                return;
            }
            insertDescriptions(stack.getEnchantments(), lines);
            insertDescriptions(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY), lines);
        }
    }

    private boolean hasEnchantments(ItemStack stack) {
        return !stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty() || !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
    }

    @OnlyFor(PhysicalSide.CLIENT)
    private void insertDescriptions(ItemEnchantments enchantments, List<Component> lines) {
        if (!enchantments.isEmpty()) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                entry.getKey().unwrapKey().ifPresent(key -> {
                    final Component fullName = Enchantment.getFullname(entry.getKey(), entry.getIntValue());
                    for (Component line : lines) {
                        if (fullName.getContents().equals(line.getContents())) {
                            final int index = lines.indexOf(line);
                            if (index != -1) {
                                MutableComponent description = getDescription(entry.getKey(), key.location(), entry.getIntValue());
                                description = getNewDescription(new DescriptionHolder(description, entry.getKey(), entry.getIntValue()));

                                if (description != null) {
                                    ComponentUtils.mergeStyles(description, config.style);
                                    lines.add(index + 1, config.prefix.copy().append(description).append(config.suffix));
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Mixin inject point to modify holder to make special custom desc base on level or oldDescription
     */
    @Nullable
    private MutableComponent getNewDescription(DescriptionHolder holder) {

        return holder.getDescription();
    }


    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
    private MutableComponent getDescription(Holder<Enchantment> enchantment, ResourceLocation id, int level) {
        MutableComponent description = getDescription("enchantment." + id.getNamespace() + "." + id.getPath() + ".", level);
        if (description == null && enchantment.value().description().getContents() instanceof TranslatableContents translatable) {
            description = getDescription(translatable.getKey() + ".", level);
        }
        return description;
    }

    @Nullable
    @OnlyFor(PhysicalSide.CLIENT)
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

    public boolean hasInitialized() {
        return this.hasInitialized;
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public static EnchdescMod getInstance() {
        if (instance == null) {
            instance = new EnchdescMod();
        }
        return instance;
    }
}