package net.darkhax.enchdesc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class Configuration {
    
    private static final String ONLY_ENCH_BOOK = "onlyEnchantedBooks";
    private static final String NEW_LINES = "addNewLines";
    private static final String SHOW_OWNER = "showOwner";
    private static final String REQUIRE_KEYBIND = "requireKeybind";
    
    private final ForgeConfigSpec spec;
    
    private final BooleanValue onlyEnchBooks;
    private final BooleanValue newLines;
    private final BooleanValue showOwner;
    private final BooleanValue requireKeybind;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("General settings for the mod.");
        builder.push("general");
        
        builder.comment("Should descriptions only be shown on enchanted books?");
        this.onlyEnchBooks = builder.define(ONLY_ENCH_BOOK, true);
        
        builder.comment("Should new/blank lines be added between enchantments?");
        this.newLines = builder.define(NEW_LINES, true);
        
        builder.comment("Should the mod that owns the enchantment be displayed?");
        this.showOwner = builder.define(SHOW_OWNER, true);
        
        builder.comment("Should players be required to press a keybind in order to see the descriptions?");
        this.requireKeybind = builder.define(REQUIRE_KEYBIND, true);
        
        builder.pop();
        
        this.spec = builder.build();
    }
    
    public ForgeConfigSpec getSpec () {
        
        return this.spec;
    }
    
    public boolean onlyShowOnEnchantedBooks () {
        
        return this.onlyEnchBooks.get();
    }
    
    public boolean shouldAddNewlines () {
        
        return this.newLines.get();
    }
    
    public boolean shouldShowOwner () {
        
        return this.showOwner.get();
    }
    
    public boolean requiresKeybindPress () {
        
        return this.requireKeybind.get();
    }
}