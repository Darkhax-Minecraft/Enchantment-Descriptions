package net.darkhax.enchdesc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.enchdesc.client.TooltipHandler;
import net.darkhax.enchdesc.handler.ConfigurationHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "enchdesc", name = "Enchantment Descriptions", version = "@VERSION@", clientSideOnly = true, certificateFingerprint = "@FINGERPRINT@")
public class EnchantmentDescriptions {

    public static final Logger LOG = LogManager.getLogger("Enchantment Descriptions");

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void preInit (FMLPreInitializationEvent event) {

        ConfigurationHandler.initConfig(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit (FMLPostInitializationEvent event) {

        if (ConfigurationHandler.isExploreMode()) {

            for (final Enchantment enchant : Enchantment.REGISTRY) {

                if (I18n.format(TooltipHandler.getTranslationKey(enchant)).startsWith("enchantment.")) {

                    LOG.info(String.format("Undefined enchantment from %s %s", TooltipHandler.getModName(enchant), TooltipHandler.getTranslationKey(enchant)));
                }
            }
        }
    }
}