package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import symbolics.division.occmy.net.*;

public class OccNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(
                C2SProjectionPayload.ID,
                C2SProjectionPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                C2SHollowingPayload.ID,
                C2SHollowingPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                C2SBetrayingPayload.ID,
                C2SBetrayingPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                S2CCaptureImagePayload.ID,
                S2CCaptureImagePayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                S2CAnsibleQuale.ID,
                S2CAnsibleQuale.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                S2CStabilizingPayload.ID,
                S2CStabilizingPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(C2SProjectionPayload.ID, C2SProjectionPayload::HANDLER);
        ServerPlayNetworking.registerGlobalReceiver(C2SHollowingPayload.ID, C2SHollowingPayload::HANDLER);
        ServerPlayNetworking.registerGlobalReceiver(C2SBetrayingPayload.ID, C2SBetrayingPayload::HANDLER);
    }
}
