package net.darkhax.enchdesc.addons.rei;

import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnchantmentDescriptionDisplayGenerator implements DynamicDisplayGenerator<DefaultInformationDisplay> {

    @Override
    public Optional<List<DefaultInformationDisplay>> getUsageFor(EntryStack<?> entry) {

        final Object obj = entry.getValue();

        if (obj instanceof ItemStack stack && stack.getItem() instanceof EnchantedBookItem && stack.hasTag()) {

            final List<DefaultInformationDisplay> info = new ArrayList<>();

            for (Enchantment enchantment : EnchantmentHelper.getEnchantments(stack).keySet()) {

                final Component description = DescriptionManager.getDescription(enchantment).copy().withStyle(ChatFormatting.BLACK);
                final DefaultInformationDisplay display = DefaultInformationDisplay.createFromEntry(entry, new TranslatableComponent(enchantment.getDescriptionId()));
                display.line(description);
                info.add(display);
            }

            return !info.isEmpty() ? Optional.of(info) : Optional.empty();
        }

        return Optional.empty();
    }
}
