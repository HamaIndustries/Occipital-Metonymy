package symbolics.division.occmy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.client.ent.ProjectionRenderer;
import symbolics.division.occmy.client.view.CProjectionView;
import symbolics.division.occmy.ent.ProjectionEntity;
import symbolics.division.occmy.net.S2CCaptureImagePayload;
import symbolics.division.occmy.obv.OccEntities;
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

        EntityRendererRegistry.register(
                OccEntities.PROJECTION,
                ProjectionRenderer::new
        );

        ClientPlayNetworking.registerGlobalReceiver(
                S2CCaptureImagePayload.ID,
                (payload, context) -> {
                    ClientWorld world = context.client().world;
                    Entity subject = world.getEntity(payload.subject());
                    if (subject != null) {
                        subject.setAttached(OccEntities.PROJECTING, true);
                    }
                    spawnImage(world, payload.from());
                    spawnImage(world, payload.to());
                }
        );
    }

    private void spawnImage(ClientWorld world, Vec3d pos) {
        ProjectionEntity proj = OccEntities.PROJECTION.create(world, SpawnReason.LOAD);
        world.addEntity(proj);
        proj.setPosition(pos);
        proj.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, MinecraftClient.getInstance().player.getEyePos());
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