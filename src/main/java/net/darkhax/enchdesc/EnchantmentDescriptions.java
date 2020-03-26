package net.darkhax.enchdesc;

import java.util.Iterator;
import java.util.ListIterator;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod("enchdesc")
@EventBusSubscriber(modid = "enchdesc", value = Dist.CLIENT)
public class EnchantmentDescriptions {
    
    @SubscribeEvent
    public static void onTooltipDisplayed (ItemTooltipEvent event) {
        
        final ItemStack stack = event.getItemStack();
        
        // Check if the player is not null, some people fire this event before things
        // have been registered.
        // Also check if the item is an enchanted book.
        if (event.getPlayer() != null && !stack.isEmpty() && stack.getItem() instanceof EnchantedBookItem) {
            
            final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;
            
            // Check if the sneak key is pressed down. If so show the descriptions.
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBindSneak.getKey().getKeyCode())) {
                
                final Iterator<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet().iterator();
                
                // Add descriptions for all the enchantments.
                while (enchants.hasNext()) {
                    
                    final Enchantment enchant = enchants.next();
                    final ListIterator<ITextComponent> tooltips = event.getToolTip().listIterator();
                    
                    while (tooltips.hasNext()) {
                        
                        final ITextComponent component = tooltips.next();
                        
                        if (component instanceof TranslationTextComponent && ((TranslationTextComponent) component).getKey().equals(enchant.getName())) {
                            
                            tooltips.add(new TranslationTextComponent(enchant.getName() + ".desc").applyTextStyle(TextFormatting.DARK_GRAY));
                            
                            final ModContainer mod = getOwner(enchant);
                            
                            if (mod != null) {
                                
                                tooltips.add(new StringTextComponent(TextFormatting.DARK_GRAY + I18n.format("tooltip.enchdesc.addedby") + ": " + TextFormatting.BLUE + mod.getModInfo().getDisplayName()));
                            }
                            
                            if (enchants.hasNext()) {
                                
                                tooltips.add(new StringTextComponent(""));
                            }
                            
                            break;
                        }
                    }
                }
            }
            
            // Let the player know they can press sneak to see stuff.
            else {
                event.getToolTip().add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.activate", TextFormatting.LIGHT_PURPLE, I18n.format(keyBindSneak.getTranslationKey()), TextFormatting.GRAY)));
            }
        }
    }
    
    /**
     * Gets the ModContainer which owns a registered thing.
     * 
     * @param registerable The thing to get the owner of.
     * @return The owner of the thing. Will be null if no owner can be found.
     */
    @Nullable
    public static ModContainer getOwner (IForgeRegistryEntry<?> registerable) {
        
        if (registerable != null && registerable.getRegistryName() != null) {
            
            return ModList.get().getModContainerById(registerable.getRegistryName().getNamespace()).orElse(null);
        }
        
        return null;
    }
}