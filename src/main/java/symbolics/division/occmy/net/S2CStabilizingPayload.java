package symbolics.division.occmy.net;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import symbolics.division.occmy.OCCMY;

public record S2CStabilizingPayload() implements CustomPayload {
    public static Id<S2CStabilizingPayload> ID = OCCMY.payloadId("atman_stabilizer");
    public static final PacketCodec<PacketByteBuf, S2CStabilizingPayload> CODEC =
            CustomPayload.codecOf(
                    (p, b) -> {
                    },
                    (b) -> new S2CStabilizingPayload()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}
