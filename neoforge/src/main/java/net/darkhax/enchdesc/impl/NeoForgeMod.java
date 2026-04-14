package net.darkhax.enchdesc.impl;

import net.darkhax.enchdesc.common.impl.EnchantmentDescriptionsMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = EnchantmentDescriptionsMod.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeMod {

    public NeoForgeMod() {
        EnchantmentDescriptionsMod.LOG.debug("Initializing Enchantment Descriptions.");
    }
}