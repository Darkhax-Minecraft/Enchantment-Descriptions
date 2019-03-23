package net.darkhax.enchdesc;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(value = EnchantmentDescriptions.MOD_ID)
public class EnchantmentDescriptions {

	public static final String MOD_ID = "enchdesc";
	
    private static final Logger LOG = LogManager.getLogger("Enchantment Descriptions");

    public EnchantmentDescriptions() {

    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->{
    		
        	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLLoadComplete);
        	MinecraftForge.EVENT_BUS.addListener(this::onTooltipDisplayed);
    	});
	}
    
    private void onFMLLoadComplete (FMLLoadCompleteEvent event) {

        for (final Enchantment enchant : ForgeRegistries.ENCHANTMENTS) {

            if (I18n.format(getTranslationKey(enchant)).startsWith("enchantment.")) {

                LOG.info(String.format("Undefined enchantment from %s %s", getModName(enchant), getTranslationKey(enchant)));
            }
        }
    }

    private void onTooltipDisplayed (ItemTooltipEvent event) {

        if (event.getEntityPlayer() != null && !event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemEnchantedBook) {

            final List<ITextComponent> tooltip = event.getToolTip();
            final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;
            
            if (InputMappings.isKeyDown(keyBindSneak.getKey().getKeyCode())) {

                final List<Enchantment> enchants = this.getEnchantments(event.getItemStack());

                for (final Enchantment enchant : enchants) {

                    tooltip.add(new TextComponentString(ChatFormatting.GRAY + I18n.format("tooltip.enchdesc.name") + ": " + I18n.format(enchant.getName())));
                    tooltip.add(new TextComponentString(ChatFormatting.GRAY + this.getDescription(enchant)));
                    tooltip.add(new TextComponentString(ChatFormatting.GRAY + I18n.format("tooltip.enchdesc.addedby") + ": " + ChatFormatting.BLUE + getModName(enchant)));
                }
            }
            
            else {
                tooltip.add(new TextComponentString(ChatFormatting.GRAY + I18n.format("tooltip.enchdesc.activate", ChatFormatting.LIGHT_PURPLE, I18n.format(keyBindSneak.getTranslationKey()), ChatFormatting.GRAY)));
            }
        }
    }

    private String getDescription (Enchantment enchantment) {

        final String key = getTranslationKey(enchantment);
        String description = I18n.format(key);

        if (description.startsWith("enchantment.")) {
            description = I18n.format("tooltip.enchdesc.missing", getModName(enchantment), key);
        }

        return description;
    }

    private List<Enchantment> getEnchantments (ItemStack stack) {

        final NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(stack);
        final List<Enchantment> enchantments = new ArrayList<>();
        
        for(int i = 0; i < nbttaglist.size(); ++i) {
           NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
           Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryCreate(nbttagcompound.getString("id")));
           if (enchantment != null) {
        	   enchantments.add(enchantment);
           }
        }

        return enchantments;
    }

    private String getModName (Enchantment registerable) {

        if (registerable != null && registerable.getRegistryName() != null) {

            final String modID = registerable.getRegistryName().getNamespace();
            final ModContainer mod = ModList.get().getModContainerById(modID).orElse(null);
            return mod != null ? mod.getModInfo().getDisplayName() : modID;
        }

        return "NULL";
    }

    private String getTranslationKey (Enchantment enchant) {

    	return (enchant != null && enchant.getRegistryName() != null) ? String.format("enchantment.%s.%s.desc", enchant.getRegistryName().getNamespace(), enchant.getRegistryName().getPath()) : "NULL";
    }
}