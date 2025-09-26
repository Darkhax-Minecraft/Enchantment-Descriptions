package net.darkhax.enchdesc.common.mixin.patch;

import net.darkhax.enchdesc.common.impl.EnchantmentDescriptionsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 2))
    public void beforeEnchantmentTooltips(Item.TooltipContext context, Player player, TooltipFlag flags, CallbackInfoReturnable<List<Component>> cbi) {
        EnchantmentDescriptionsMod.setupContext((ItemStack) (Object) this);
    }

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 3, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void afterEnchantmentTooltips(Item.TooltipContext context, Player player, TooltipFlag flags, CallbackInfoReturnable<List<Component>> cbi, List<Component> lines) {
        final ItemStack self = (ItemStack) (Object) this;
        EnchantmentDescriptionsMod.revertContext(self);
        if (EnchantmentDescriptionsMod.canDisplayDescription(self) && !EnchantmentDescriptionsMod.isKeybindConditionMet()) {
            lines.add(EnchantmentDescriptionsMod.getKeybindText());
        }
    }
}