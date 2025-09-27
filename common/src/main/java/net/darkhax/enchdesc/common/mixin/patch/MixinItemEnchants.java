package net.darkhax.enchdesc.common.mixin.patch;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.darkhax.enchdesc.common.impl.EnchantmentDescriptionsMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public class MixinItemEnchants {

    @Inject(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void sortedEnchantment(Item.TooltipContext context, Consumer<Component> tooltips, TooltipFlag flag, DataComponentGetter components, CallbackInfo cbi, HolderLookup.Provider lookup, HolderSet<Enchantment> sorted, Iterator<Holder<Enchantment>> iter, Holder<Enchantment> enchantment, int level) {
        EnchantmentDescriptionsMod.insertDescriptions(enchantment, level, tooltips);
    }

    @Inject(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void unsortedEnchantment(Item.TooltipContext context, Consumer<Component> tooltips, TooltipFlag flag, DataComponentGetter components, CallbackInfo cbi, HolderLookup.Provider lookup, HolderSet<Enchantment> sorted, ObjectIterator<Holder<Enchantment>> iter, Object2IntMap.Entry<Holder<Enchantment>> entry, Holder<Enchantment> enchantment) {
        EnchantmentDescriptionsMod.insertDescriptions(enchantment, entry.getIntValue(), tooltips);
    }
}