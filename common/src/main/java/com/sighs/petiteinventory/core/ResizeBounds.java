package com.sighs.petiteinventory.core;

/** Pure resize math; platform UI adapters only translate pointer movement into deltas. */
public final class ResizeBounds {
    private ResizeBounds() {
    }

    public static ItemSize resize(ItemSize size, ResizeDirection direction, int deltaX, int deltaY, int maximum) {
        int width = size.width();
        int height = size.height();
        if (direction == ResizeDirection.EAST || direction == ResizeDirection.NORTH_EAST || direction == ResizeDirection.SOUTH_EAST) width += deltaX;
        if (direction == ResizeDirection.WEST || direction == ResizeDirection.NORTH_WEST || direction == ResizeDirection.SOUTH_WEST) width -= deltaX;
        if (direction == ResizeDirection.SOUTH || direction == ResizeDirection.SOUTH_EAST || direction == ResizeDirection.SOUTH_WEST) height += deltaY;
        if (direction == ResizeDirection.NORTH || direction == ResizeDirection.NORTH_EAST || direction == ResizeDirection.NORTH_WEST) height -= deltaY;
        return new ItemSize(clamp(width, maximum), clamp(height, maximum));
    }

    private static int clamp(int value, int maximum) {
        return Math.max(1, Math.min(maximum, value));
    }
}
