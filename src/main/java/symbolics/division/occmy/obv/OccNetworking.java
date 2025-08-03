package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import symbolics.division.occmy.net.C2SProjectionPayload;
import symbolics.division.occmy.net.S2CCaptureImagePayload;

public class OccNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(
                C2SProjectionPayload.ID,
                C2SProjectionPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                S2CCaptureImagePayload.ID,
                S2CCaptureImagePayload.CODEC
        );
    }
}
