package net.darkhax.enchdesc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class EnchDescFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        new EnchDescCommon(FabricLoader.getInstance().getConfigDir());
    }
}