package com.sighs.petiteinventory.core;

/** Platform-neutral inventory footprint. */
public final class ItemSize {
    private final int width;
    private final int height;

    public ItemSize(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Item dimensions must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public int width() { return width; }
    public int height() { return height; }
    public int area() { return width * height; }
    public ItemSize rotated() { return new ItemSize(height, width); }
}
