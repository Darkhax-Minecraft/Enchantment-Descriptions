package net.darkhax.enchdesc;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Set;

public class EnchDescCommon {

    public static void onItemTooltip(ItemStack stack, TooltipFlag context, List<Component> tooltip) {

        if (!stack.isEmpty() && stack.hasTag()) {

            final Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();

            if (!enchantments.isEmpty()) {

                for (Enchantment enchantment : enchantments) {

                    for (Component line : tooltip) {

                        if (line instanceof TranslatableComponent translatable && translatable.getKey().equals(enchantment.getDescriptionId())) {

                            tooltip.add(tooltip.indexOf(line) + 1, DescriptionManager.getDescription(enchantment));
                            break;
                        }
                    }
                }
            }
        }
    }
}