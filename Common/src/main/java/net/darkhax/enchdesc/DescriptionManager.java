package net.darkhax.enchdesc;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;

public class DescriptionManager {

    private static final Map<Enchantment, MutableComponent> descriptions = new HashMap<>();

    public static MutableComponent getDescription (Enchantment ench) {

        return descriptions.computeIfAbsent(ench, e -> {

            TranslatableComponent description = new TranslatableComponent(e.getDescriptionId() + ".desc");

            if (!I18n.exists(description.getKey()) && I18n.exists(e.getDescriptionId() + ".description")) {

                description = new TranslatableComponent(e.getDescriptionId() + ".description");
            }

            return description.withStyle(ChatFormatting.DARK_GRAY);
        });
    }
}