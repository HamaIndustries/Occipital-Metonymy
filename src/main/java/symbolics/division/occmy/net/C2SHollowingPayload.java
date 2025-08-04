package symbolics.division.occmy.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import symbolics.division.occmy.OCCMY;

public record C2SHollowingPayload() implements CustomPayload {
    public static Id<C2SHollowingPayload> ID = OCCMY.payloadId("hollowing");
    public static final PacketCodec<PacketByteBuf, C2SHollowingPayload> CODEC = CustomPayload.codecOf(
            (p, b) -> {
            },
            (b) -> new C2SHollowingPayload()
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void HANDLER(C2SHollowingPayload payload, ServerPlayNetworking.Context context) {
        context.player().kill(context.player().getWorld());
    }
}
