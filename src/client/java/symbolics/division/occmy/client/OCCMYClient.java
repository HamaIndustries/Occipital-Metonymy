package symbolics.division.occmy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import symbolics.division.occmy.client.view.CProjectionView;
import symbolics.division.occmy.state.Necessity;
import symbolics.division.occmy.state.Sachverhalten;
import symbolics.division.occmy.view.Views;

public class OCCMYClient implements ClientModInitializer {
    public static final Sachverhalten AFFAIRS = new Sachverhalten();

    @Override
    public void onInitializeClient() {
        Views.PROJECTION.setCallback(c -> c.ap(CProjectionView::open));

        ClientTickEvents.START_CLIENT_TICK.register(
                client -> {
                    AFFAIRS.tick();
                }
        );
    }

    public static void nextTick(Runnable r) {
        schedule(r, 1);
    }

    public static void schedule(Runnable r, int ticks) {
        AFFAIRS.enable(new Necessity(r, ticks));
    }

    public static ClientWorld world() {
        return MinecraftClient.getInstance().world;
    }

    public static ClientPlayerEntity player() {
        return MinecraftClient.getInstance().player;
    }
}