package net.darkhax.enchdesc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.darkhax.bookshelf.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.config.ModConfig;

public class EnchantmentDescriptionsClient {
    
    private final KeyBinding keybind;
    private final Configuration config;
    
    private static final Map<Enchantment, IFormattableTextComponent> descriptions = new HashMap<>();
    
    public EnchantmentDescriptionsClient() {
        
        this.keybind = new KeyBinding("key.enchdesc.show", KeyConflictContext.GUI, Type.KEYSYM, 340, "key.enchdesc.title");
        this.config = new Configuration();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, this.config.getSpec());
        MinecraftForge.EVENT_BUS.addListener(this::onTooltipDisplayed);
        ClientRegistry.registerKeyBinding(this.keybind);
    }
    
    public static final IFormattableTextComponent getDescription (Enchantment ench) {
        
        return descriptions.computeIfAbsent(ench, e -> {
            
            IFormattableTextComponent description = new TranslationTextComponent(e.getName() + ".desc").mergeStyle(TextFormatting.DARK_GRAY);
            
            if (!I18n.hasKey(e.getName() + ".desc") && I18n.hasKey(e.getName() + ".description")) {
                
                description = new TranslationTextComponent(e.getName() + ".description").mergeStyle(TextFormatting.DARK_GRAY);
            }
            
            return description;
        });
    }
    
    private void onTooltipDisplayed (ItemTooltipEvent event) {
        
        try {
            
            final ItemStack stack = event.getItemStack();
            
            // Only show on enchanted books. Can be configured to allow all items.
            if (!stack.isEmpty() && !EnchantmentHelper.getEnchantments(stack).isEmpty() && (!this.config.onlyShowOnEnchantedBooks() || stack.getItem() instanceof EnchantedBookItem)) {
                
                if (!this.config.requiresKeybindPress() || InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), this.keybind.getKey().getKeyCode())) {
                    
                    this.insertDescriptionTooltips(event.getToolTip(), stack);
                }
                
                else if (this.config.requiresKeybindPress()) {
                    
                    event.getToolTip().add(new StringTextComponent(TextFormatting.GRAY + I18n.format("tooltip.enchdesc.activate", TextFormatting.LIGHT_PURPLE, I18n.format(this.keybind.getTranslationKey()), TextFormatting.GRAY)));
                }
            }
        }
        
        catch (final Exception e) {
            
            event.getToolTip().add(new TranslationTextComponent("enchdesc.fatalerror").mergeStyle(TextFormatting.RED));
            EnchantmentDescriptions.LOG.error("Ran into issues displaying tooltip for {}", event.getItemStack());
            EnchantmentDescriptions.LOG.catching(e);
        }
    }
    
    private void insertDescriptionTooltips (List<ITextComponent> tips, ItemStack stack) {
        
        final Iterator<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet().iterator();
        
        while (enchants.hasNext()) {
            
            final Enchantment enchant = enchants.next();
            final ListIterator<ITextComponent> tooltips = tips.listIterator();

            boolean didInsert = false;

            while (tooltips.hasNext()) {
                
                final ITextComponent component = tooltips.next();
                
                if (component instanceof TranslationTextComponent && ((TranslationTextComponent) component).getKey().equals(enchant.getName())) {

                    didInsert = true;
                    tooltips.add(getDescription(enchant));
                    
                    final ModContainer mod = ModUtils.getOwner(enchant);
                    
                    if (this.config.shouldShowOwner() && mod != null) {
                        
                        final ITextComponent modName = ModUtils.getModName(mod);
                        
                        if (modName instanceof IFormattableTextComponent) {
                            
                            ((IFormattableTextComponent) modName).mergeStyle(TextFormatting.BLUE);
                        }
                        
                        tooltips.add(new TranslationTextComponent("tooltip.enchdesc.addedby", modName).mergeStyle(TextFormatting.DARK_GRAY));
                    }
                    
                    if (this.config.shouldAddNewlines() && enchants.hasNext()) {
                        
                        tooltips.add(new StringTextComponent(" "));
                    }
                    
                    break;
                }
            }

            if (!didInsert) {

                tooltips.add(new StringTextComponent(" "));
                tooltips.add(new TranslationTextComponent(enchant.getName()));
                tooltips.add(getDescription(enchant));
            }
        }
    }
}