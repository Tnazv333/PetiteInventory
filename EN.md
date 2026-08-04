# Petite Inventory

PetiteInventory is a modular grid-inventory system for Minecraft Forge. Items can occupy configurable rectangular footprints, while placement, replacement, stacking, rotation, and transfer operations use the same footprint rules.

## Features

- Configure item sizes and tags in `config/PetiteInventory/default.json`.
- Rotate items without changing their identity or stack data.
- Use edit mode to resize item footprints from edges and corners, select multiple items with `Ctrl`, and apply preset border colors.
- Enable or disable the Petite layout per screen. The player inventory and hotbar remain independently configurable.
- Integrate with ordinary containers and Sophisticated Core/Backpacks screens.
- Preserve replacement behavior for differently sized items and find valid areas during quick transfers.
- Keep the hotbar outside the Petite layout by default.

Rules can be reloaded with `/reload`. To configure another container, point at one of its slots and use the copy-container key (default: `U`), then add the screen identifier to the configuration.

## Edit Mode

In edit mode, a normal left click still picks up or places an item. Hold `Ctrl` to enter multi-selection; `Ctrl`-drag selects a region and `Ctrl+Shift` extends a selection. Right-click opens the preset color palette, while holding right-click for 0.5 seconds starts batch footprint editing.

## Notes

Compatibility with sorting, slot-expansion, and other inventory-altering mods can vary. Manual organization is intentional, and the hotbar is excluded to keep its conventional layout. The layout works well with Item Borders.

## Roadmap

- Port to additional Minecraft versions and loaders.
- Add more compatibility adapters for third-party container screens.
- Support inventory expansion mechanisms.
- Explore non-rectangular, Tetris-style item shapes.
