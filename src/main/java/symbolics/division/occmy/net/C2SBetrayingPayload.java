package symbolics.division.occmy.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.view.TreacherousView;

public record C2SBetrayingPayload(Vec3d pos, Vec3d rot) implements CustomPayload {
    public static Id<C2SBetrayingPayload> ID = OCCMY.payloadId("betraying");
    public static final PacketCodec<PacketByteBuf, C2SBetrayingPayload> CODEC = CustomPayload.codecOf(
            (p, b) -> {
                b.writeVec3d(p.pos);
                b.writeVec3d(p.rot);
            },
            (b) -> new C2SBetrayingPayload(b.readVec3d(), b.readVec3d())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void HANDLER(C2SBetrayingPayload payload, ServerPlayNetworking.Context context) {
        TreacherousView.depaint(context.player().getWorld(), context.player(), payload.pos, payload.rot);
    }
}
