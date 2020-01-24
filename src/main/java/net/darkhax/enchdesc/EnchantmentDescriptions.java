package net.darkhax.enchdesc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(EnchantmentDescriptions.MOD_ID)
@EventBusSubscriber(modid = EnchantmentDescriptions.MOD_ID, value = Dist.CLIENT)
public class EnchantmentDescriptions {
    
    public static final String MOD_ID = "enchdesc";
    
    @SubscribeEvent
    public static void onTooltipDisplayed (ItemTooltipEvent event) {
        
        // Check if the player is not null, some people fire this event before things have been registered.
        // Also check if the item is an enchanted book.
        if (event.getPlayer() != null && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof EnchantedBookItem) {
            
            final List<ITextComponent> tooltip = event.getToolTip();
            final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;
            
            // Check if the sneak key is pressed down. If so show the descriptions.
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBindSneak.getKey().getKeyCode())) {
                
                final List<Enchantment> enchants = getEnchantments(event.getItemStack());
                
                // Add descriptions for all the enchantments.
                for (final Enchantment enchant : enchants) {
                    
                    tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.name") + ": " + I18n.format(enchant.getName())));
                    tooltip.add(new StringTextComponent(TextFormatting.GRAY + getDescription(enchant)));
                    tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.addedby") + ": " + TextFormatting.BLUE + getModName(enchant)));
                }
            }
            
            // Let the player know they can press sneak to see stuff.
            else {
                tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.activate", TextFormatting.LIGHT_PURPLE, I18n.format(keyBindSneak.getTranslationKey()), TextFormatting.GRAY)));
            }
        }
    }
    
    /**
     * Get the translated description for an enchantment.
     * 
     * @param enchantment The enchantment to get a description for.
     * @return A translated description for the enchantment.
     */
    private static String getDescription (Enchantment enchantment) {
        
        final String key = getTranslationKey(enchantment);
        String description = I18n.format(key);
        
        if (description.startsWith("enchantment.")) {
            description = I18n.format("tooltip.enchdesc.missing", getModName(enchantment), key);
        }
        
        return description;
    }
    
    /**
     * Get the list of enchantments from an enchanted book itemstack.
     * 
     * @param stack The stack to get enchantments from.
     * @return The enchantments on the item.
     */
    private static List<Enchantment> getEnchantments (ItemStack stack) {
        
        final ListNBT nbttaglist = EnchantedBookItem.getEnchantments(stack);
        final List<Enchantment> enchantments = new ArrayList<>();
        
        for (int i = 0; i < nbttaglist.size(); ++i) {
            final CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
            final Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryCreate(nbttagcompound.getString("id")));
            if (enchantment != null) {
                enchantments.add(enchantment);
            }
        }
        
        return enchantments;
    }
    
    /**
     * Gets the owner mod name from any registerable object.
     * 
     * @param registerable The object to get the owner name.
     * @return The name of the mod that owns the registerable.
     */
    private static String getModName (IForgeRegistryEntry<?> registerable) {
        
        if (registerable != null && registerable.getRegistryName() != null) {
            
            final String modID = registerable.getRegistryName().getNamespace();
            final ModContainer mod = ModList.get().getModContainerById(modID).orElse(null);
            return mod != null ? mod.getModInfo().getDisplayName() : modID;
        }
        
        return "NULL";
    }
    
    /**
     * Gets the raw translation string for an enchantment.
     * 
     * @param enchant The enchantment to get the name of.
     * @return The translation key for the enchantment.
     */
    private static String getTranslationKey (Enchantment enchant) {
        
        return enchant != null && enchant.getRegistryName() != null ? String.format("enchantment.%s.%s.desc", enchant.getRegistryName().getNamespace(), enchant.getRegistryName().getPath()) : "NULL";
    }
}