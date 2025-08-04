package symbolics.division.occmy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.client.ent.ProjectionRenderer;
import symbolics.division.occmy.client.gfx.ThetiscopeFullnessProperty;
import symbolics.division.occmy.client.view.CBestView;
import symbolics.division.occmy.client.view.CExteriorityView;
import symbolics.division.occmy.client.view.CProjectionView;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.ent.ProjectionEntity;
import symbolics.division.occmy.net.C2SHollowingPayload;
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
        Views.EXTERIORITY.setCallback(c -> c.ap(CExteriorityView::open));
        Views.BEST.setCallback(c -> c.ap(CBestView::open));

        ClientTickEvents.START_CLIENT_TICK.register(
                client -> {
                    AFFAIRS.tick();
                }
        );

        EntityRendererRegistry.register(
                OccEntities.PROJECTION,
                ProjectionRenderer::new
        );

        EntityRendererRegistry.register(
                OccEntities.MARIONETTE,
                EmptyEntityRenderer::new
        );

        ClientPlayNetworking.registerGlobalReceiver(
                S2CCaptureImagePayload.ID,
                (payload, context) -> {
                    ClientWorld world = context.client().world;
                    Entity subject = world.getEntity(payload.subject());
                    if (subject != null) {
                        subject.setAttached(OccEntities.PROJECTING, true);
                        subject.setAttached(OccEntities.OBSCURED, true);
                    }
                    spawnImage(world, payload.from());
                    spawnImage(world, payload.to());
                }
        );

        OCCMY.interiority = () -> MinecraftClient.getInstance().player;

        BooleanProperties.ID_MAPPER.put(
                OCCMY.id("thetiscope_fullness"),
                ThetiscopeFullnessProperty.CODEC
        );

        MarionetteEntity.signal = () -> {
            cutStrings(MinecraftClient.getInstance().world);
            ClientPlayNetworking.send(new C2SHollowingPayload());
        };

        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(
                (minecraftClient, clientWorld) -> {
                    CBestView.reset();
                    CExteriorityView.reset();
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

    private void cutStrings(ClientWorld world) {
        if (world == null) return;
        for (Entity e : world.getEntities()) {
            if (e instanceof MarionetteEntity) {
                e.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }
}