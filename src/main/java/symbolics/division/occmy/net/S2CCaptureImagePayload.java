package symbolics.division.occmy.net;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;

import java.util.UUID;
import java.util.function.Consumer;

public record S2CCaptureImagePayload(Vec3d from, Vec3d to, UUID subject) implements CustomPayload {
    public static Id<S2CCaptureImagePayload> ID = OCCMY.payloadId("obscure");
    public static final PacketCodec<PacketByteBuf, S2CCaptureImagePayload> CODEC = CustomPayload.codecOf(
            (p, b) -> {
                b.writeVec3d(p.from);
                b.writeVec3d(p.to);
                b.writeUuid(p.subject);
            },
            (b) -> new S2CCaptureImagePayload(b.readVec3d(), b.readVec3d(), b.readUuid())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private static Consumer<S2CCaptureImagePayload> callback = null;

    public static void register(Consumer<S2CCaptureImagePayload> sendPayloadCallback) {
        callback = sendPayloadCallback;
    }
}
