package net.darkhax.enchdesc;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DescriptionManager {

    private static final Map<Enchantment, MutableComponent> descriptions = new ConcurrentHashMap<>();

    public static MutableComponent getDescription(Enchantment ench) {

        return descriptions.computeIfAbsent(ench, e -> {

            String descriptionKey = e.getDescriptionId() + ".desc";

            if (!I18n.exists(descriptionKey) && I18n.exists(e.getDescriptionId() + ".description")) {

                descriptionKey = e.getDescriptionId() + ".description";
            }

            return Component.translatable(descriptionKey).withStyle(ChatFormatting.DARK_GRAY);
        });
    }
}