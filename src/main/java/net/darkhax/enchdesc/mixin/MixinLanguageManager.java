package net.darkhax.enchdesc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.darkhax.enchdesc.EnchantmentDescriptions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(LanguageManager.class)
public class MixinLanguageManager {
    
    /**
     * This Mixin runs code after the language manager has loaded/reloaded. The code checks the
     * enchantment registry for enchantments that do not have a description and raises a
     * warning. Client reload listeners are not suitable for this as they are asynchronous and
     * frequently result in incorrect or outdated views of the language map.
     * 
     * Additionally, using FMLLoadCompleteEvent for a one-time print out is also unreliable as
     * the language listener can and often does complete after FMLLoadComplete.
     */
    @Inject(method = "onResourceManagerReload(Lnet/minecraft/resources/IResourceManager;)V", at = @At("RETURN"))
    private void onResourceManagerReload (IResourceManager resourceManager, CallbackInfo callback) {
        
        int missing = 0;
        
        for (final Enchantment enchant : ForgeRegistries.ENCHANTMENTS) {
            
            if (!I18n.hasKey(enchant.getName() + ".desc")) {
                
                EnchantmentDescriptions.LOG.warn("Missing description for {}. '{}'", enchant.getRegistryName(), enchant.getName() + ".desc");
                missing++;
            }
        }
        
        EnchantmentDescriptions.LOG.debug("Found {} enchantment descriptions and {} missing or invalid descriptions.", ForgeRegistries.ENCHANTMENTS.getValues().size() - missing, missing);
    }
}