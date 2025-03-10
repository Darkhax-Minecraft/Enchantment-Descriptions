package net.darkhax.enchdesc.common.impl;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public class DescriptionHolder {
    @Nullable
    private MutableComponent description;
    private final Holder<Enchantment> enchantment;
    private final int level;

    public DescriptionHolder(@Nullable MutableComponent description, Holder<Enchantment> enchantment, int level) {
        this.description = description;
        this.enchantment = enchantment;
        this.level = level;
    }


    public void setDescription(@Nullable MutableComponent newDescription) {
        this.description = newDescription;
    }

    @Nullable
    public MutableComponent getDescription() {
        return description;
    }

    public Holder<Enchantment> getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }
}
