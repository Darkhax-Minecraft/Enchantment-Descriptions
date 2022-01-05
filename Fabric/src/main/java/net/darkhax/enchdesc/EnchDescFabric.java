package net.darkhax.enchdesc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;

public class EnchDescFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EnchDescCommon common = new EnchDescCommon(FabricLoader.getInstance().getConfigDir());
        ItemTooltipCallback.EVENT.register(common::onItemTooltip);
    }
}