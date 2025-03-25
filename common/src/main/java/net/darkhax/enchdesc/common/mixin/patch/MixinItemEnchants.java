package net.darkhax.enchdesc.common.mixin.patch;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.darkhax.enchdesc.common.api.ContextProvider;
import net.darkhax.enchdesc.common.impl.EnchdescMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public class MixinItemEnchants implements ContextProvider {

    @Unique
    private ItemStack enchdesc$heldStack = ItemStack.EMPTY;

    @Override
    public ItemStack enchdesc$getStack() {
        return enchdesc$heldStack;
    }

    @Override
    public void enchdesc$setStack(ItemStack stack) {
        this.enchdesc$heldStack = stack;
    }

    @Inject(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void sortedEnchantment(Item.TooltipContext context, Consumer<Component> tooltips, TooltipFlag flag, CallbackInfo cbi, HolderLookup.Provider lookup, HolderSet<Enchantment> sorted, Iterator<Holder<Enchantment>> iter, Holder<Enchantment> enchantment, int level) {
        if (!this.enchdesc$heldStack.isEmpty()) {
            EnchdescMod.getInstance().insertDescriptions(enchantment, level, tooltips);
        }
    }

    @Inject(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void unsortedEnchantment(Item.TooltipContext context, Consumer<Component> tooltips, TooltipFlag flag, CallbackInfo cbi, HolderLookup.Provider lookup, HolderSet<Enchantment> sorted, ObjectIterator<Holder<Enchantment>> iter, Object2IntMap.Entry<Holder<Enchantment>> entry, Holder<Enchantment> enchantment) {
        if (!this.enchdesc$heldStack.isEmpty()) {
            EnchdescMod.getInstance().insertDescriptions(enchantment, entry.getIntValue(), tooltips);
        }
    }
}