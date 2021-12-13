package net.darkhax.enchdesc.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.darkhax.enchdesc.Constants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class EnchDescJEI implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {

        return new ResourceLocation(Constants.MOD_ID, "jei");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(new ItemStack(Items.ENCHANTING_TABLE), CategoryEnchantmentInfo.ID);
        registration.addRecipeCatalyst(new ItemStack(Items.ENCHANTED_BOOK), CategoryEnchantmentInfo.ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        final List<EnchantmentInfoEntry> recipes = ForgeRegistries.ENCHANTMENTS.getValues().stream().map(EnchantmentInfoEntry::new).collect(Collectors.toList());
        registration.addRecipes(recipes, CategoryEnchantmentInfo.ID);

        recipes.forEach(i -> registration.addIngredientInfo(i.getBooks(), VanillaTypes.ITEM, new TranslatableComponent(i.getDescriptionKey())));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new CategoryEnchantmentInfo(registration.getJeiHelpers().getGuiHelper()));
    }
}