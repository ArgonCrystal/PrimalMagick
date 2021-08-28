package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.util.LevelUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet sent from the server to remove a prop marker particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class RemovePropMarkerPacket implements IMessageToClient {
    protected BlockPos pos;
    
    public RemovePropMarkerPacket() {}
    
    public RemovePropMarkerPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }
    
    public static void encode(RemovePropMarkerPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }
    
    public static RemovePropMarkerPacket decode(FriendlyByteBuf buf) {
        RemovePropMarkerPacket message = new RemovePropMarkerPacket();
        message.pos = buf.readBlockPos();
        return message;
    }
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(RemovePropMarkerPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Level world = (FMLEnvironment.dist == Dist.CLIENT) ? LevelUtils.getCurrentLevel() : null;
                // Only process positions that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (world != null && world.hasChunkAt(message.pos)) {
                    FxDispatcher.INSTANCE.removePropMarker(message.pos);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
