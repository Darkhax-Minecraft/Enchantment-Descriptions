package net.darkhax.enchdesc;

import net.darkhax.enchdesc.commands.ExportCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class EnchDescFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        new EnchDescCommon(FabricLoader.getInstance().getConfigDir());
        ExportCommand.register();
    }
}