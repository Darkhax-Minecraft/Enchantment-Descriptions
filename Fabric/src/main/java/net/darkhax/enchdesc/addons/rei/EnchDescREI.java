package net.darkhax.enchdesc.addons.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class EnchDescREI implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        registry.registerGlobalDisplayGenerator(new EnchantmentDescriptionDisplayGenerator());
    }
}