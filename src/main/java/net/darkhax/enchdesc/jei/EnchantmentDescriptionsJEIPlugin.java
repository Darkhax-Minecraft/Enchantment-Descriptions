package net.darkhax.enchdesc.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.darkhax.enchdesc.EnchantmentDescriptions;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class EnchantmentDescriptionsJEIPlugin implements IModPlugin {
    
    @Override
    public ResourceLocation getPluginUid () {
        
        return new ResourceLocation(EnchantmentDescriptions.MOD_ID, "jei");
    }
    
    @Override
    public void registerRecipeCatalysts (IRecipeCatalystRegistration registration) {
        
        registration.addRecipeCatalyst(new ItemStack(Items.ENCHANTING_TABLE), CategoryEnchantmentInfo.ID);
        registration.addRecipeCatalyst(new ItemStack(Items.ENCHANTED_BOOK), CategoryEnchantmentInfo.ID);
    }
    
    @Override
    public void registerRecipes (IRecipeRegistration registration) {
        
        final List<EnchantmentInfoEntry> recipes = ForgeRegistries.ENCHANTMENTS.getValues().stream().map(EnchantmentInfoEntry::new).collect(Collectors.toList());
        registration.addRecipes(recipes, CategoryEnchantmentInfo.ID);
        
        recipes.forEach(i -> registration.addIngredientInfo(i.getBooks(), VanillaTypes.ITEM, I18n.format(i.getDescriptionKey())));
    }
    
    @Override
    public void registerCategories (IRecipeCategoryRegistration registration) {
        
        registration.addRecipeCategories(new CategoryEnchantmentInfo(registration.getJeiHelpers().getGuiHelper()));
    }
    
    @Override
    public void onRuntimeAvailable (IJeiRuntime jeiRuntime) {
        
        final List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Items.GOLDEN_HELMET));
        items.add(new ItemStack(Items.GOLDEN_LEGGINGS));
        items.add(new ItemStack(Items.TURTLE_HELMET));
        
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM, items);
    }
}