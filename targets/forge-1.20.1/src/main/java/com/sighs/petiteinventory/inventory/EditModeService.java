package com.sighs.petiteinventory.inventory;

import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Server-authoritative edit-mode state. */
public final class EditModeService {
    private static final Set<UUID> EDITORS = ConcurrentHashMap.newKeySet();

    private EditModeService() {
    }

    public static boolean toggle(ServerPlayer player) {
        UUID id = player.getUUID();
        if (!EDITORS.add(id)) {
            EDITORS.remove(id);
            return false;
        }
        return true;
    }

    public static boolean isEnabled(ServerPlayer player) {
        return EDITORS.contains(player.getUUID());
    }
}
