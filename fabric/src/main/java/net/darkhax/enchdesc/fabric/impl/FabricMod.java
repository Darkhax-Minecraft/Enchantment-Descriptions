package net.darkhax.enchdesc.fabric.impl;

import net.darkhax.enchdesc.common.impl.EnchantmentDescriptionsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EnchantmentDescriptionsMod.config.get();
        }
    }
}