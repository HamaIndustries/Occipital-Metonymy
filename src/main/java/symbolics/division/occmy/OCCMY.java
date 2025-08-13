package symbolics.division.occmy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.occmy.obv.*;
import symbolics.division.occmy.view.TreacherousView;

import java.util.function.Supplier;

public class OCCMY implements ModInitializer {
    public static final String ID = "occmy";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static Identifier id(String id) {
        return Identifier.of(ID, id);
    }

    public static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String identififer) {
        return new CustomPayload.Id<>(id(identififer));
    }

    @Override
    public void onInitialize() {
        OccBloccs.init();
        OccItems.init();
        OccComponents.init();
        OccNetworking.init();
        OccEntities.init();
        OccSounds.init();
        OccEtc.init();

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(TreacherousView::tick);
        });
    }

    public static Supplier<PlayerEntity> interiority = () -> {
        throw new NotImplementedException("attempted external self reference");
    };

    public static PlayerEntity self() {
        return interiority.get();
    }
}
