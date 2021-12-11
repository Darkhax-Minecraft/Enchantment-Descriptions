package net.darkhax.enchdesc.jei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mezz.jei.Internal;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.ingredients.IngredientFilter;
import mezz.jei.ingredients.IngredientManager;
import net.darkhax.enchdesc.Constants;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInfoEntry implements IRecipeCategoryExtension {
    
    private final Enchantment enchantment;
    private final Component name;
    private final Component description;
    private final int maxLevel;
    private final List<Enchantment> incompatibleEnchantments;
    private final List<ItemStack> books;
    private final List<ItemStack> compatibleItems;
    
    public EnchantmentInfoEntry(Enchantment enchantment) {
        
        this.enchantment = enchantment;
        this.name = new TranslatableComponent(enchantment.getDescriptionId()).withStyle(ChatFormatting.BLACK);
        this.description = DescriptionManager.getDescription(enchantment).copy().withStyle(ChatFormatting.BLACK);
        this.maxLevel = enchantment.getMaxLevel();
        this.incompatibleEnchantments = getIncompatibleEnchantments(enchantment);
        this.books = getEnchantedBooks(enchantment);
        this.compatibleItems = getCompatibleItems(enchantment);
    }
    
    @Override
    public void setIngredients (IIngredients ingredients) {
        
        ingredients.setInputs(VanillaTypes.ITEM, this.compatibleItems);
        ingredients.setOutputs(VanillaTypes.ITEM, this.books);
    }
    
    public Component getName () {
        
        return this.name;
    }
    
    public Component getDescription () {
        
        return this.description;
    }
    
    public String getDescriptionKey () {
        
        return this.enchantment.getDescriptionId() + ".desc";
    }
    
    public int getMaxLevel () {
        
        return this.maxLevel;
    }
    
    public List<Enchantment> getIncompatibleEnchantments () {
        
        return this.incompatibleEnchantments;
    }
    
    public List<ItemStack> getBooks () {
        
        return this.books;
    }
    
    public List<ItemStack> getCompatibleItems () {

        final IngredientFilter filter = Internal.getIngredientFilter();
        return this.compatibleItems.stream().filter(filter::isIngredientVisible).collect(Collectors.toList());
    }
    
    public void getTooltip (int slotIndex, boolean input, ItemStack stack, List<Component> tooltip) {
        
        if (this.enchantment.canApplyAtEnchantingTable(stack) && !this.enchantment.isTreasureOnly()) {
            
            tooltip.add(Blocks.ENCHANTING_TABLE.getName().withStyle(ChatFormatting.GREEN));
        }
        
        if (this.enchantment.canEnchant(stack)) {
            
            tooltip.add(Blocks.ANVIL.getName().withStyle(ChatFormatting.GREEN));
        }
    }
    
    /**
     * A cache of compatible item enchantments. Built by {@link #buildCompatibilityCache()},
     * accessed through {@link #getCompatibleItems(Enchantment)}.
     */
    private static Map<Enchantment, NonNullList<ItemStack>> compatibleItemsCache;
    
    /**
     * Gets the compatible items for a given enchantment.
     * 
     * @param enchantment The enchantment to get compatible items.
     * @return A list of compatible items.
     */
    private static NonNullList<ItemStack> getCompatibleItems (Enchantment enchantment) {
        
        if (compatibleItemsCache == null) {
            
            buildCompatibilityCache();
        }
        
        return compatibleItemsCache.getOrDefault(enchantment, NonNullList.create());
    }
    
    /**
     * Builds a cache of which enchantments are compatible with which items. Stored in
     * {@link #compatibleItemsCache} and accessed by {@link #getCompatibleItems(Enchantment)}.
     */
    private static void buildCompatibilityCache () {
        
        compatibleItemsCache = new HashMap<>();
        
        for (final Item item : ForgeRegistries.ITEMS) {
            
            try {
                
                final ItemStack stack = new ItemStack(item);
                
                if (stack.isEnchantable()) {
                    
                    for (final Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
                        
                        if (enchantment.canEnchant(stack) || enchantment.canApplyAtEnchantingTable(stack)) {
                            
                            compatibleItemsCache.computeIfAbsent(enchantment, e -> NonNullList.create()).add(stack);
                        }
                    }
                }
            }
            
            catch (final Exception e) {

                Constants.LOG.error("Failed to determine enchantment compatibility for Item \"{}\".", item.getRegistryName());
                Constants.LOG.catching(e);
            }
        }
    }
    
    /**
     * Builds a list of enchantments that are incompatible with the given enchantment.
     * 
     * @param enchantment The enchantment to generate this list for.
     * @return The list of incompatible enchantments.
     */
    private static List<Enchantment> getIncompatibleEnchantments (Enchantment enchantment) {
        
        final List<Enchantment> incompatible = NonNullList.create();
        
        for (final Enchantment other : ForgeRegistries.ENCHANTMENTS) {
            
            if (other != enchantment && !enchantment.isCompatibleWith(other)) {
                
                incompatible.add(other);
            }
        }
        
        return incompatible;
    }
    
    private static List<ItemStack> getEnchantedBooks (Enchantment enchantment) {
        
        final List<ItemStack> books = NonNullList.create();
        
        for (int lvl = enchantment.getMinLevel(); lvl <= enchantment.getMaxLevel(); lvl++) {
            
            final ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment, lvl));
            books.add(book);
        }
        
        return books;
    }
}