package symbolics.division.occmy.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.item.RotaryComponent;
import symbolics.division.occmy.obv.OccComponents;

public record C2SRotaryPayload(int amount) implements CustomPayload {
    public static Id<C2SRotaryPayload> ID = OCCMY.payloadId("rotary");
    public static final PacketCodec<PacketByteBuf, C2SRotaryPayload> CODEC = CustomPayload.codecOf(
            (p, b) -> {
                b.writeInt(p.amount);
            },
            (b) -> new C2SRotaryPayload(b.readInt())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void HANDLER(C2SRotaryPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        RotaryComponent rotary = stack.get(OccComponents.ROTARY);
        if (rotary != null) {
            stack.set(OccComponents.ROTARY, payload.amount > 0 ? rotary.backward() : rotary.forward());
        }
    }
}
