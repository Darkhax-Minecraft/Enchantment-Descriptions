package net.darkhax.enchdesc;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("enchdesc")
public class EnchantmentDescriptions {
    
    private final Configuration config = new Configuration();
    private final Logger log = LogManager.getLogger("Enchantment Descriptions");
    
    public EnchantmentDescriptions() {
        
    	if (FMLEnvironment.dist == Dist.CLIENT) {
    		
            ModLoadingContext.get().registerConfig(Type.CLIENT, this.config.getSpec());
            MinecraftForge.EVENT_BUS.addListener(this::onTooltipDisplayed);
    	}
    }
    
    private void onTooltipDisplayed (final ItemTooltipEvent event) {
        
        try {
            
            final ItemStack stack = event.getItemStack();
            
            // Only show on enchanted books. Can be configured to allow all items.
            if (!stack.isEmpty() && !EnchantmentHelper.getEnchantments(stack).isEmpty() && (!this.config.onlyShowOnEnchantedBooks() || stack.getItem() instanceof EnchantedBookItem)) {
                
                final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;
                
                if (!this.config.requiresKeybindPress() || InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBindSneak.getKey().getKeyCode())) {
                    
                    this.insertDescriptionTooltips(event.getToolTip(), stack);
                }
                
                else if (this.config.requiresKeybindPress()) {
                    
                    event.getToolTip().add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.activate", TextFormatting.LIGHT_PURPLE, I18n.format(keyBindSneak.getTranslationKey()), TextFormatting.GRAY)));
                }
            }
        }
        
        catch (final Exception e) {
            
            event.getToolTip().add(new TranslationTextComponent("enchdesc.fatalerror").mergeStyle(TextFormatting.RED));
            this.log.error("Ran into issues displaying tooltip for {}", event.getItemStack());
            this.log.catching(e);
        }
    }
    
    private void insertDescriptionTooltips (final List<ITextComponent> tips, final ItemStack stack) {
        
        final Iterator<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet().iterator();
        
        while (enchants.hasNext()) {
            
            final Enchantment enchant = enchants.next();
            final ListIterator<ITextComponent> tooltips = tips.listIterator();
            
            while (tooltips.hasNext()) {
                
                final ITextComponent component = tooltips.next();
                
                if (component instanceof TranslationTextComponent && ((TranslationTextComponent) component).getKey().equals(enchant.getName())) {
                    
                    tooltips.add(new TranslationTextComponent(enchant.getName() + ".desc").mergeStyle(TextFormatting.DARK_GRAY));
                    
                    final ModContainer mod = ModUtils.getOwner(enchant);
                    
                    if (this.config.shouldShowOwner() && mod != null) {
                        
                        final ITextComponent modName = ModUtils.getModName(mod);
                        
                        if (modName instanceof IFormattableTextComponent) {
                        	
                        	((IFormattableTextComponent) modName).mergeStyle(TextFormatting.BLUE);
                        }
                        
                        tooltips.add(new TranslationTextComponent("tooltip.enchdesc.addedby", modName).mergeStyle(TextFormatting.DARK_GRAY));
                    }
                    
                    if (this.config.shouldAddNewlines() && enchants.hasNext()) {
                        
                        tooltips.add(new StringTextComponent(""));
                    }
                    
                    break;
                }
            }
        }
    }
}