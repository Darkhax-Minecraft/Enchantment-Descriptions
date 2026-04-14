package net.darkhax.enchdesc.fabric;

import net.darkhax.enchdesc.common.impl.EnchantmentDescriptionsMod;
import net.fabricmc.api.ClientModInitializer;

public class EnchantmentDescriptionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EnchantmentDescriptionsMod.LOG.debug("Initializing Enchantment Descriptions.");
    }
}