package net.darkhax.enchdesc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class EnchDescForge {
    
    public EnchDescForge() {

        final Consumer<ItemTooltipEvent> listener = event -> EnchDescCommon.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
        MinecraftForge.EVENT_BUS.addListener(listener);
    }
}