package net.darkhax.enchdesc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class EnchDescFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ItemTooltipCallback.EVENT.register(EnchDescCommon::onItemTooltip);
    }
}