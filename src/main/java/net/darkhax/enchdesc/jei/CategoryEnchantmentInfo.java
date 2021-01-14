package net.darkhax.enchdesc.jei;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.enchdesc.EnchantmentDescriptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CategoryEnchantmentInfo implements IRecipeCategory<EnchantmentInfoEntry> {
    
    public static final ResourceLocation ID = new ResourceLocation(EnchantmentDescriptions.MOD_ID, "compatible_items");
    
    private final IDrawable icon;
    private final IDrawableStatic background;
    private final IDrawableStatic slotDrawable;
    
    public CategoryEnchantmentInfo(IGuiHelper guiHelper) {
        
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.ENCHANTED_BOOK));
        this.background = guiHelper.createBlankDrawable(133, 48);
        this.slotDrawable = guiHelper.getSlotDrawable();
    }
    
    @Override
    public String getTitle () {
        
        return I18n.format("enchdesc.jei.compatible_items.title");
    }
    
    @Override
    public ResourceLocation getUid () {
        
        return ID;
    }
    
    @Override
    public IDrawable getBackground () {
        
        return this.background;
    }
    
    @Override
    public IDrawable getIcon () {
        
        return this.icon;
    }
    
    @Override
    public Class<? extends EnchantmentInfoEntry> getRecipeClass () {
        
        return EnchantmentInfoEntry.class;
    }
    
    @Override
    public void setIngredients (EnchantmentInfoEntry info, IIngredients ingredients) {
        
        info.setIngredients(ingredients);
    }
    
    @Override
    public void setRecipe (IRecipeLayout layout, EnchantmentInfoEntry recipe, IIngredients ingredients) {
        
        final IGuiItemStackGroup stacks = layout.getItemStacks();
        
        final List<List<ItemStack>> slotContents = Arrays.asList(NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create());
        
        for (int slotId = 0; slotId < recipe.getCompatibleItems().size(); slotId++) {
            
            if (slotId >= recipe.getCompatibleItems().size()) {
                
                break;
            }
            
            slotContents.get(slotId % slotContents.size()).add(recipe.getCompatibleItems().get(slotId));
        }
        
        for (int slotId = 0; slotId < slotContents.size(); slotId++) {
            
            stacks.init(slotId, false, 19 * (slotId % 7), 19 * (1 + slotId / 7) - 8);
            stacks.set(slotId, slotContents.get(slotId));
        }
        
        stacks.addTooltipCallback(recipe::getTooltip);
    }
    
    @Override
    public void draw (EnchantmentInfoEntry info, MatrixStack matrix, double mouseX, double mouseY) {
        
        final Minecraft minecraft = Minecraft.getInstance();
        
        RenderUtils.renderLinesWrapped(matrix, minecraft.fontRenderer, 0, 0, 0, 0xffffff, info.getName(), 130);
        
        for (int slotId = 0; slotId < 14; slotId++) {
            
            this.slotDrawable.draw(matrix, 19 * (slotId % 7), 19 * (1 + slotId / 7) - 8);
        }
    }
}