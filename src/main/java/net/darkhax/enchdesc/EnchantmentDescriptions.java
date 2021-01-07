package net.darkhax.enchdesc;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class EnchantmentDescriptions implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		
		ItemTooltipCallback.EVENT.register(EnchantmentDescriptions::onTooltipDisplayed);
	}
	
    private static void onTooltipDisplayed (ItemStack stack, TooltipContext context, List<Text> lines) {
        
    	if (stack.getItem() instanceof EnchantedBookItem && !EnchantmentHelper.get(stack).isEmpty()) {
    		
            insertDescriptionTooltips(lines, stack);
    	}
    }
    
    private static void insertDescriptionTooltips (List<Text> tips, ItemStack stack) {
        
        final Iterator<Enchantment> enchants = EnchantmentHelper.get(stack).keySet().iterator();
        
        while (enchants.hasNext()) {
            
            final Enchantment enchant = enchants.next();
            final ListIterator<Text> tooltips = tips.listIterator();
            
            while (tooltips.hasNext()) {
                
                final Text tooltipLine = tooltips.next();
                
                if (tooltipLine instanceof TranslatableText && ((TranslatableText) tooltipLine).getKey().equals(enchant.getTranslationKey())) {
                    
                    tooltips.add(new TranslatableText(enchant.getTranslationKey() + ".desc").formatted(Formatting.DARK_GRAY));
                    break;
                }
            }
        }
    }
}