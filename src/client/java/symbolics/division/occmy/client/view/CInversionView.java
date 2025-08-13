package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.obv.OccEntities;
import symbolics.division.occmy.state.Sufficiency;

import java.util.function.Consumer;

public class CInversionView {

    private static Sufficiency INVERTED_STATE = new Sufficiency() {
        {
            check = false;
        }

        private void setPlayer(Consumer<PlayerEntity> consumer) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) consumer.accept(player);
        }

        @Override
        protected void onStart() {
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            setPlayer(p -> p.setAttached(OccEntities.INVERTED, true));
        }

        @Override
        protected void onEnd() {
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            setPlayer(p -> p.setAttached(OccEntities.INVERTED, false));
        }
    };

    public static void open(World world, PlayerEntity player) {
        if (INVERTED_STATE.isActive()) {
            INVERTED_STATE.disable();
        } else {
            OCCMYClient.AFFAIRS.enableFor(INVERTED_STATE, 200);
        }
    }

    public static boolean active() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        return player != null && player.getAttachedOrElse(OccEntities.INVERTED, false);
    }

    public static void reset() {
        if (INVERTED_STATE.isActive()) INVERTED_STATE.disable();
    }
}
