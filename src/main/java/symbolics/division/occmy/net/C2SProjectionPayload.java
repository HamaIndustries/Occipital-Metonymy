package symbolics.division.occmy.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import symbolics.division.occmy.OCCMY;

import java.util.UUID;

public record C2SProjectionPayload(Vec3d to) implements CustomPayload {
    public static CustomPayload.Id<C2SProjectionPayload> ID = OCCMY.payloadId("obscure");
    public static final PacketCodec<PacketByteBuf, C2SProjectionPayload> CODEC = CustomPayload.codecOf(
            (p, b) -> {
                b.writeVec3d(p.to);
            },
            (b) -> new C2SProjectionPayload(b.readVec3d())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void HANDLER(C2SProjectionPayload payload, ServerPlayNetworking.Context context) {
        UUID id = context.player().getUuid();
        for (ServerPlayerEntity player : context.player().getWorld().getPlayers()) {
            if (player == context.player()) continue;
            ServerPlayNetworking.send(player, new S2CCaptureImagePayload(context.player().getPos(), payload.to, id));
        }
        ServerWorld world = context.player().getWorld();
        context.player().teleportTo(new TeleportTarget(world, payload.to, Vec3d.ZERO, context.player().headYaw, context.player().getPitch(), TeleportTarget.NO_OP));
    }
}
