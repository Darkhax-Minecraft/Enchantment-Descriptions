package net.darkhax.enchdesc;

import net.darkhax.enchdesc.client.TooltipHandler;
import net.darkhax.enchdesc.handler.ConfigurationHandler;
import net.darkhax.enchdesc.lib.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Constants.MODID, name = Constants.MOD_NAME, version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class EnchantmentDescriptions {

    @Mod.Instance(Constants.MODID)
    public static EnchantmentDescriptions instance;

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
            Constants.LOG.warn("This is a client side mod. There is no benefit to installing it server side.");
        }
        else if (event.getSide().equals(Side.CLIENT)) {

            ConfigurationHandler.initConfig(event.getSuggestedConfigurationFile());
            MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        }
    }

    @EventHandler
    public void postInit (FMLPostInitializationEvent event) {

        if (event.getSide().equals(Side.CLIENT) && ConfigurationHandler.exploreMode) {
            for (final Enchantment enchant : Enchantment.REGISTRY) {
                if (I18n.format(TooltipHandler.getTranslationKey(enchant)).startsWith("enchantment.")) {
                    Constants.LOG.info(String.format("Undefined enchantment from %s %s", TooltipHandler.getModName(enchant), TooltipHandler.getTranslationKey(enchant)));
                }
            }
        }
    }
}