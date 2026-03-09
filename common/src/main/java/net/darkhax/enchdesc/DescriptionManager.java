package net.darkhax.enchdesc;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DescriptionManager {

    private final ConfigSchema config;
    private final Map<Enchantment, MutableComponent> descriptions = new ConcurrentHashMap<>();

    public DescriptionManager(ConfigSchema config) {
        this.config = config;
    }

    public static String getKey(Enchantment ench) {

        final String descKey = ench.getDescriptionId() + ".desc";

        if (!I18n.exists(descKey) && I18n.exists(ench.getDescriptionId() + ".description")) {

            return ench.getDescriptionId() + ".description";
        }

        return descKey;
    }

    public MutableComponent get(Enchantment ench) {

        return descriptions.computeIfAbsent(ench, e ->
            Component.translatable(getKey(e)).withStyle(config.style));
    }
}