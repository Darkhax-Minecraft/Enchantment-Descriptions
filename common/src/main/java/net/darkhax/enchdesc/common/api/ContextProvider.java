package net.darkhax.enchdesc.common.api;

import net.minecraft.world.item.ItemStack;

public interface ContextProvider {

    ItemStack enchdesc$getStack();

    void enchdesc$setStack(ItemStack stack);
}
