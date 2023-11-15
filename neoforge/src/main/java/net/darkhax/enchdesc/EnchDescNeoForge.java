package net.darkhax.enchdesc;

import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.NetworkConstants;
import net.neoforged.neoforgespi.Environment;

@Mod(Constants.MOD_ID)
public class EnchDescNeoForge {

    public EnchDescNeoForge() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (Environment.get().getDist().isClient()) {

            new EnchDescCommon(FMLPaths.CONFIGDIR.get());
        }
    }
}