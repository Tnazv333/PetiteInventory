package com.sighs.petiteinventory.api;

/** Stable value-only description of an item's inventory footprint. */
public record ItemArea(int width, int height) {
    public ItemArea {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Item area dimensions must be positive");
        }
    }

    public int slotCount() {
        return width * height;
    }
}
