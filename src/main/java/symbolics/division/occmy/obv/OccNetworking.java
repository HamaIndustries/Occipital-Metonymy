package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import symbolics.division.occmy.net.C2SHollowingPayload;
import symbolics.division.occmy.net.C2SProjectionPayload;
import symbolics.division.occmy.net.S2CAnsibleQuale;
import symbolics.division.occmy.net.S2CCaptureImagePayload;

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

        PayloadTypeRegistry.playS2C().register(
                S2CCaptureImagePayload.ID,
                S2CCaptureImagePayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                S2CAnsibleQuale.ID,
                S2CAnsibleQuale.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(C2SProjectionPayload.ID, C2SProjectionPayload::HANDLER);
        ServerPlayNetworking.registerGlobalReceiver(C2SHollowingPayload.ID, C2SHollowingPayload::HANDLER);
    }
}
