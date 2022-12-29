package net.darkhax.enchdesc;

import net.darkhax.bookshelf.api.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.nio.file.Path;
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

            if (!config.onlyDisplayOnBooks || stack.getItem() instanceof EnchantedBookItem) {

                if (!config.onlyDisplayInEnchantingTable || Minecraft.getInstance().screen instanceof EnchantmentScreen) {

                    final Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();

                    if (!enchantments.isEmpty()) {

                        if (!config.requireKeybindPress || Screen.hasShiftDown()) {

                            for (Enchantment enchantment : enchantments) {

                                for (Component line : tooltip) {

                                    if (line instanceof TranslatableComponent translatable && translatable.getKey().equals(enchantment.getDescriptionId())) {

                                        tooltip.add(tooltip.indexOf(line) + 1, DescriptionManager.getDescription(enchantment));
                                        break;
                                    }
                                }
                            }
                        }

                        else {

                            tooltip.add(new TranslatableComponent("enchdesc.activate.message").withStyle(ChatFormatting.DARK_GRAY));
                        }
                    }
                }
            }
        }
    }
}