package net.darkhax.enchdesc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

    public static final String MOD_ID = "enchdesc";
    public static final String MOD_NAME = "Enchantment Descriptions";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);

    // vanilla list of items that can be enchanted (using diamond for armor/tools)
    // turtle shell is included in helmet category
    public static final Item[] itemCategories = new Item[]{Items.BOW,
            Items.BRUSH,
            Items.CARROT_ON_A_STICK,
            Items.COMPASS,
            Items.CROSSBOW,
            Items.DIAMOND_AXE,
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_HELMET,
            Items.DIAMOND_HOE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_SWORD,
            Items.ELYTRA,
            Items.FISHING_ROD,
            Items.FLINT_AND_STEEL,
            Items.SHEARS,
            Items.SHIELD,
            Items.TRIDENT,
            Items.WARPED_FUNGUS_ON_A_STICK,
    };
}