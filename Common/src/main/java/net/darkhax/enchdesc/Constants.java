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
    public static final Item[] itemCategories = new Item[]{
            Items.WARPED_FUNGUS_ON_A_STICK,
            Items.TRIDENT,
            Items.SHIELD,
            Items.SHEARS,
            Items.FLINT_AND_STEEL,
            Items.FISHING_ROD,
            Items.ELYTRA,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_HOE,
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_AXE,
            Items.CROSSBOW,
            Items.COMPASS,
            Items.CARROT_ON_A_STICK,
            Items.BRUSH,
            Items.BOW,
    };
}