package symbolics.division.occmy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.occmy.net.C2SProjectionPayload;
import symbolics.division.occmy.obv.OccComponents;
import symbolics.division.occmy.obv.OccEntities;
import symbolics.division.occmy.obv.OccItems;
import symbolics.division.occmy.obv.OccNetworking;

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
        OccItems.init();
        OccComponents.init();
        OccNetworking.init();
        OccEntities.init();


        ServerPlayNetworking.registerGlobalReceiver(
                C2SProjectionPayload.ID,
                C2SProjectionPayload::HANDLER
        );
    }

    public static Supplier<PlayerEntity> interiority = () -> {
        throw new NotImplementedException("attempted external self reference");
    };

    public static PlayerEntity self() {
        return interiority.get();
    }
}
