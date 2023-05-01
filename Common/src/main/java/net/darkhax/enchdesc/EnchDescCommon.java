package net.darkhax.enchdesc;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EnchDescCommon {

    private final ConfigSchema config;

    public EnchDescCommon(Path configPath) {

        this.config = ConfigSchema.load(configPath.resolve(Constants.MOD_ID + ".json").toFile());
        Services.EVENTS.addItemTooltipListener(this::onItemTooltip);
    }

    private void onItemTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (this.config.enableMod && !stack.isEmpty() && stack.hasTag()) {

            if ((!config.onlyDisplayOnBooks && stack.isEnchanted()) || stack.getItem() instanceof EnchantedBookItem) {

                if (!config.onlyDisplayInEnchantingTable || Minecraft.getInstance().screen instanceof EnchantmentScreen) {

                    final Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();

                    if (!enchantments.isEmpty()) {

                        if (!config.requireKeybindPress || Screen.hasShiftDown()) {

                            for (Enchantment enchantment : enchantments) {

                                for (Component line : tooltip) {

                                    if (line.getContents() instanceof TranslatableContents translatable && translatable.getKey().equals(enchantment.getDescriptionId())) {

                                        Component descriptionText = DescriptionManager.getDescription(enchantment);

                                        if (config.indentSize > 0) {

                                            descriptionText = Component.literal(StringUtils.repeat(' ', config.indentSize)).append(descriptionText);
                                        }

                                        tooltip.add(tooltip.indexOf(line) + 1, descriptionText);

                                        List<Item> applicableItems = new ArrayList<>();

                                        // vanilla list of items that can be enchanted (using diamond for armor/tools)
                                        // turtle shell is included in helmet category
                                        Item[] itemCategories = new Item[]{Items.DIAMOND_SWORD, Items.BOW,
                                                Items.CROSSBOW, Items.FISHING_ROD, Items.SHEARS, Items.TRIDENT,
                                                Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL,
                                                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS,
                                                Items.DIAMOND_BOOTS, Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND_HOE,
                                                Items.SHIELD, Items.ELYTRA, Items.FLINT_AND_STEEL, Items.CARROT_ON_A_STICK,
                                                Items.WARPED_FUNGUS_ON_A_STICK, Items.BRUSH, Items.COMPASS};

                                        // iterate over all items in array
                                        for (Item item : itemCategories) {
                                            ItemStack itemStack = new ItemStack(item);
                                            if (enchantment.canEnchant(itemStack) && !applicableItems.contains(item)) {
                                                applicableItems.add(item);
                                            }
                                        }

                                        // add applicable items to tooltip
                                        if (!applicableItems.isEmpty()) {
                                            for (Item item : applicableItems) {
                                                tooltip.add(tooltip.indexOf(line) + 2, Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GOLD));
                                            }
                                        }

                                        if (config.showMaxLevel) {
                                            int maxLevel = enchantment.getMaxLevel();
                                            if (maxLevel > 1) {
                                                // Write "Max Level: NUM" in the tooltip
                                                tooltip.add(tooltip.indexOf(line) + 2, Component.translatable("enchdesc.max_level", maxLevel).withStyle(ChatFormatting.DARK_PURPLE));
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                        } else {

                            tooltip.add(Component.translatable("enchdesc.activate.message").withStyle(ChatFormatting.DARK_GRAY));
                        }
                    }
                }
            }
        }
    }
}