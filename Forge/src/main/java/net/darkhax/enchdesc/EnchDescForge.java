package net.darkhax.enchdesc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.network.NetworkConstants;

@Mod(Constants.MOD_ID)
public class EnchDescForge {

    private EnchDescCommon common;

    public EnchDescForge() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (Environment.get().getDist().isClient()) {

            this.common = new EnchDescCommon(FMLPaths.CONFIGDIR.get());
            MinecraftForge.EVENT_BUS.addListener(this::onTooltip);
        }
    }

    private void onTooltip(ItemTooltipEvent event) {

        if (this.common != null) {

            common.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
        }
    }
}