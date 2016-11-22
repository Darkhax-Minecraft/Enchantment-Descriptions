package net.darkhax.enchdesc.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.darkhax.enchdesc.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TooltipHandler {
    
    private static final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
    
    @SubscribeEvent
    public void onTooltipDisplayed (ItemTooltipEvent event) {
        
        if (event.getItemStack() != null && event.getItemStack().getItem() instanceof ItemEnchantedBook) {
            
            final List<String> tooltip = event.getToolTip();
            
            if (GameSettings.isKeyDown(keyBindSneak)) {
                
                final ItemEnchantedBook item = (ItemEnchantedBook) event.getItemStack().getItem();
                final List<Enchantment> enchants = getEnchantments(item, event.getItemStack());
                
                for (Enchantment enchant : enchants) {
                    
                    tooltip.add(I18n.format("tooltip.enchdesc.name") + ": " + I18n.format(enchant.getName()));
                    tooltip.add(getDescription(enchant));
                    tooltip.add(I18n.format("tooltip.enchdesc.addedby") + ": " + ChatFormatting.BLUE + getModName(enchant));
                }
            }
            
            else
                tooltip.add(I18n.format("tooltip.enchdesc.activate", ChatFormatting.LIGHT_PURPLE, keyBindSneak.getDisplayName(), ChatFormatting.GRAY));
        }
    }
    
    /**
     * Gets the description of the enchantment. Or the missing text, if no description exists.
     * 
     * @param enchantment The enchantment to get a description for.
     * @return The enchantment description.
     */
    private String getDescription (Enchantment enchantment) {
        
        final String key = getTranslationKey(enchantment);
        String description = I18n.format(key);
        
        if (description.startsWith("enchantment."))
            description = I18n.format("tooltip.enchdesc.missing", getModName(enchantment), key);
            
        return description;
    }
    
    /**
     * Reads a List of enchantments from an ItemEnchantedBook stack.
     * 
     * @param book Instance of ItemEnchantedBook, as it uses non-static methods for some
     *        reason.
     * @param stack The stack to read the data from.
     * @return The list of enchantments stored on the stack.
     */
    private List<Enchantment> getEnchantments (ItemEnchantedBook book, ItemStack stack) {
        
        final NBTTagList enchTags = book.getEnchantments(stack);
        final List<Enchantment> enchantments = new ArrayList<Enchantment>();
        
        if (enchTags != null) {
            
            for (int index = 0; index < enchTags.tagCount(); ++index) {
                
                final int id = enchTags.getCompoundTagAt(index).getShort("id");
                final Enchantment enchant = Enchantment.getEnchantmentByID(id);
                
                if (enchant != null)
                    enchantments.add(enchant);
            }
        }
        
        return enchantments;
    }
    
    /**
     * Gets the name of the mod that registered the passed object. Works for anthing which uses
     * Forge's registry.
     * 
     * @param registerable The object to get the mod name of.
     * @return The name of the mod which registered the object.
     */
    public static String getModName (IForgeRegistryEntry.Impl<?> registerable) {
        
    	if (registerable != null && registerable.getRegistryName() != null) {
    		
            final String modID = registerable.getRegistryName().getResourceDomain();
            final ModContainer mod = Loader.instance().getIndexedModList().get(modID);
            return mod != null ? mod.getName() : modID;
    	}
    	
    	return "NULL";
    }
    
    public static String getTranslationKey(Enchantment enchant) {
        
    	if (enchant != null) {

    		Constants.LOG.info(enchant.getClass());
    		
    		if (enchant.getRegistryName() != null)
    			return String.format("enchantment.%s.%s.desc", enchant.getRegistryName().getResourceDomain(), enchant.getRegistryName().getResourcePath());
    	}
    	return "enchantment. NULL";
    }
}