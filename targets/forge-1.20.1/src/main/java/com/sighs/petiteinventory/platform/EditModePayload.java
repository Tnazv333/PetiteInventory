package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.client.ClientEditMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server-to-client synchronization for the editor toggle. */
public record EditModePayload(boolean enabled) {
    public static final ResourceLocation ID = new ResourceLocation("petiteinventory", "edit_mode");

    public static void encode(EditModePayload message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.enabled);
    }

    public static EditModePayload decode(FriendlyByteBuf buffer) {
        return new EditModePayload(buffer.readBoolean());
    }

    public static void handle(EditModePayload message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ClientEditMode.setEnabled(message.enabled));
        contextSupplier.get().setPacketHandled(true);
    }
}
