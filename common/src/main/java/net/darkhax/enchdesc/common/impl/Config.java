package net.darkhax.enchdesc.common.impl;

import net.darkhax.pricklemc.common.api.annotations.Value;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class Config {

    @Value(comment = "Determines if the mod and its features are enabled.")
    public boolean enabled = true;

    @Value(comment = "When enabled, descriptions will only be displayed when looking at an enchanted book.")
    public boolean only_on_books = false;

    @Value(comment = "When enabled, descriptions will only be displayed when inside the enchanting table GUI.")
    public boolean only_in_enchanting_table = false;

    @Value(comment = "When enabled, descriptions will only be displayed when the user holds the shift key.")
    public boolean require_keybind = false;

    @Value(comment = "This text will be displayed when the require_keybind option is enabled and the user has not held the keybind.")
    public Component activate_text = Component.translatable("enchdesc.activate.message").withStyle(ChatFormatting.DARK_GRAY);

    @Value(comment = "Should the activate text be displayed?")
    public boolean display_activate_text = true;

    @Value(comment = "Text that will be added to the start of each description. This can be used to add indents and other decorators.")
    public Component prefix = Component.empty();

    @Value(comment = "Text that will be added to the end of each description.")
    public Component suffix = Component.empty();

    @Value(comment = "The style of the description text. This controls the color, format, font, and other visual properties of the description.")
    public Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
}
