package com.sighs.petiteinventory.inventory;

import com.sighs.petiteinventory.core.ItemSize;
import net.minecraft.world.item.ItemStack;

public record Area(int width, int height, ItemStack itemStack) {
    public ItemSize size() {
        return new ItemSize(width, height);
    }

    public int minSize() {
        return Math.min(width, height);
    }

    public int maxSize() {
        return Math.max(width, height);
    }

    @Override
    public String toString() {
        return "[" + itemStack + "](" + width + "," + height + ")";
    }
}
