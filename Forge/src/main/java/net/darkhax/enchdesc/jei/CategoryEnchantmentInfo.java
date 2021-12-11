package net.darkhax.enchdesc.jei;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.darkhax.enchdesc.Constants;
import net.darkhax.enchdesc.TempRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CategoryEnchantmentInfo implements IRecipeCategory<EnchantmentInfoEntry> {
    
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "compatible_items");
    
    private final IDrawable icon;
    private final IDrawableStatic background;
    private final IDrawableStatic slotDrawable;
    
    public CategoryEnchantmentInfo(IGuiHelper guiHelper) {
        
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.ENCHANTED_BOOK));
        this.background = guiHelper.createBlankDrawable(133, 48);
        this.slotDrawable = guiHelper.getSlotDrawable();
    }
    
    @Override
    public Component getTitle () {
        
        return new TranslatableComponent("enchdesc.jei.compatible_items.title");
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
        final List<ItemStack> compatible = recipe.getCompatibleItems();
        
        for (int slotId = 0; slotId < compatible.size(); slotId++) {
            
            slotContents.get(slotId % slotContents.size()).add(compatible.get(slotId));
        }
        
        for (int slotId = 0; slotId < slotContents.size(); slotId++) {
            
            stacks.init(slotId, false, 19 * (slotId % 7), 19 * (1 + slotId / 7) - 8);
            stacks.set(slotId, slotContents.get(slotId));
        }
        
        stacks.addTooltipCallback(recipe::getTooltip);
    }
    
    @Override
    public void draw (EnchantmentInfoEntry info, PoseStack matrix, double mouseX, double mouseY) {
        
        final Minecraft minecraft = Minecraft.getInstance();
        
        TempRenderUtils.renderLinesWrapped(matrix, minecraft.font, 0, 0, 0, 0xffffff, info.getName(), 130);
        
        for (int slotId = 0; slotId < 14; slotId++) {
            
            this.slotDrawable.draw(matrix, 19 * (slotId % 7), 19 * (1 + slotId / 7) - 8);
        }
    }
}