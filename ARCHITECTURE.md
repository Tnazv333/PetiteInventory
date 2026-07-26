# Functional modules and targets

The repository uses `common + targets/<loader>-<minecraft-version>`. `common`
contains Java 8, platform-neutral rules only. Minecraft, Forge, Mixin, rendering,
networking and mod metadata remain in the relevant target.

Source code is organized by gameplay responsibility. Forge events, packets and
Mixins are adapters, not homes for business rules.

| Module | Owns | Must not own |
| --- | --- | --- |
| `api` | Supported public integration API | Internal caches, Mixins, packets |
| `core` | Value objects such as item area and border theme | Forge registration and file I/O |
| `module/item` | Item footprint, rotation marker and item-size events | Container interception and packet handling |
| `module/defense` | Every inventory admission: stacking, rotation, footprint, fallback and rejection | Mixin injection and packet encoding |
| `module/container` | Container grid parsing, slot mapping and area placement | Item-admission fallback |
| `module/compatibility` | Optional third-party integrations; currently the KubeJS bridge | Inventory rules and rendering |
| `configuration` | Forge configuration declarations | Gameplay policy |
| `infrastructure/configuration` | JSON rules and cache storage | UI and event handling |
| `client` | Client context, rendering and key mappings | Server rules |
| `platform/forge` | Forge lifecycle, commands and packet adapters | Feature policy |
| `platform/mixin` | Mixin adapters and bridge interfaces | Public API |

Every path that puts an item into a player inventory must call
`module.defense.InventoryAdmissionService`. Container-specific grid and slot
calculation comes only from `module.container`. Third-party integration code
belongs only in `module.compatibility`.
