package net.darkhax.enchdesc.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

    /**
     * An instance of the Configuration object being used.
     */
    private static Configuration config = null;

    /**
     * Whether or not undocumented enchantments should be listed.
     */
    public static boolean exploreMode = false;

    /**
     * Initializes the configuration file.
     *
     * @param file The file to read/write config stuff to.
     */
    public static void initConfig (File file) {

        config = new Configuration(file);
        syncConfig();
    }

    /**
     * Syncs all configuration properties.
     */
    public static void syncConfig () {

        exploreMode = config.getBoolean("exploreMode", Configuration.CATEGORY_GENERAL, false, "Should the mod generate a list of enchantments from the instance that have no description?");

        if (config.hasChanged()) {
            config.save();
        }
    }
}