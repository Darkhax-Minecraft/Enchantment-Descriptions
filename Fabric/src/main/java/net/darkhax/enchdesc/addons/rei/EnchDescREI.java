package net.darkhax.enchdesc.addons.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class EnchDescREI implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        for (Enchantment enchantment : Registry.ENCHANTMENT) {

            final Component description = DescriptionManager.getDescription(enchantment).copy().withStyle(ChatFormatting.BLACK);

            for (int level = 1; level <= enchantment.getMaxLevel(); level++) {

                final ItemStack bookStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
                final DefaultInformationDisplay display = DefaultInformationDisplay.createFromEntry(EntryStacks.of(bookStack), new TranslatableComponent(enchantment.getDescriptionId()));
                display.line(description);
                registry.add(display);
            }
        }
    }
}