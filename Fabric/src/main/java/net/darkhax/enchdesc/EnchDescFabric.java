package net.darkhax.enchdesc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class EnchDescFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {

        ItemTooltipCallback.EVENT.register(EnchDescCommon::onItemTooltip);
    }
}