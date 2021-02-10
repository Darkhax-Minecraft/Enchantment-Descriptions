package net.darkhax.enchdesc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(EnchantmentDescriptions.MOD_ID)
public class EnchantmentDescriptions {
    
    public static final String MOD_ID = "enchdesc";
    public static final Logger LOG = LogManager.getLogger("Enchantment Descriptions");
    
    public EnchantmentDescriptions() {
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            
            new EnchantmentDescriptionsClient();
        }
    }
}