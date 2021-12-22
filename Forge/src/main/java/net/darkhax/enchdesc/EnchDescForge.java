package net.darkhax.enchdesc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class EnchDescForge {

    public EnchDescForge() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener((Consumer<ItemTooltipEvent>) event -> EnchDescCommon.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip()));
    }
}