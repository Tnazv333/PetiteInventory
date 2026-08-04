package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.client.ClientInventoryContext;
import com.sighs.petiteinventory.client.ScreenLayoutSettings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Synchronizes the global default used by the per-screen layout overrides. */
public record ScreenLayoutModePayload(boolean defaultEnabled) {
    public static final ResourceLocation ID = new ResourceLocation("petiteinventory", "screen_layout_mode");

    public static void encode(ScreenLayoutModePayload message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.defaultEnabled);
    }

    public static ScreenLayoutModePayload decode(FriendlyByteBuf buffer) {
        return new ScreenLayoutModePayload(buffer.readBoolean());
    }

    public static void handle(ScreenLayoutModePayload message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ScreenLayoutSettings.setDefaultEnabled(message.defaultEnabled);
            ClientInventoryContext.invalidate();
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
