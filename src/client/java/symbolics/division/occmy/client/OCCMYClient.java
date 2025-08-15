package symbolics.division.occmy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.block.ParadoxBlock;
import symbolics.division.occmy.client.ent.ProjectionRenderer;
import symbolics.division.occmy.client.gfx.AntimonicConsistencyProperty;
import symbolics.division.occmy.client.gfx.ThetiscopeFullnessProperty;
import symbolics.division.occmy.client.view.*;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.ent.ProjectionEntity;
import symbolics.division.occmy.net.C2SHollowingPayload;
import symbolics.division.occmy.net.S2CAnsibleQuale;
import symbolics.division.occmy.net.S2CCaptureImagePayload;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccEntities;
import symbolics.division.occmy.state.Necessity;
import symbolics.division.occmy.state.Sachverhalten;
import symbolics.division.occmy.view.Views;

public class OCCMYClient implements ClientModInitializer {
    public static final Sachverhalten AFFAIRS = new Sachverhalten();

    @Override
    public void onInitializeClient() {
        Views.PROJECTION.setCallback(c -> CProjectionView.open(c.world(), c.user(), c.anchor()));
        Views.EXTERIORITY.setCallback(c -> c.ap(CExteriorityView::open));
        Views.BEST.setCallback(c -> c.ap(CBestView::open));
        Views.INVERSION.setCallback(c -> c.ap(CInversionView::open));
        Views.ANTIMONY.setCallback(c -> c.ap(CAntimonicView::open));
        Views.TREACHERY.setCallback(c -> c.ap(CTreacherousView::open));
        Views.NULLITY.setCallback(c -> c.ap(CNullView::open));

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

        ClientPlayNetworking.registerGlobalReceiver(
                S2CAnsibleQuale.ID,
                (payload, context) -> {
                    ClientWorld world = world();
                    PlayerEntity subject = player();
                    if (world != null && subject != null) {
                        payload.view().open(world, subject);
                    }
                }
        );

        OCCMY.interiority = () -> MinecraftClient.getInstance().player;

        BooleanProperties.ID_MAPPER.put(
                OCCMY.id("thetiscope_fullness"),
                ThetiscopeFullnessProperty.CODEC
        );

        BooleanProperties.ID_MAPPER.put(
                OCCMY.id("antimonic_consistency"),
                AntimonicConsistencyProperty.CODEC
        );

        MarionetteEntity.signal = () -> {
            cutStrings(MinecraftClient.getInstance().world);
            ClientPlayNetworking.send(new C2SHollowingPayload());
        };

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            resetAll();
            Perspectives.reset();
        });

        ClientEntityEvents.ENTITY_LOAD.register(((entity, clientWorld) -> {
            if (entity instanceof ClientPlayerEntity)
                Perspectives.reset();
        }));

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, clientWorld) -> {
            if (entity instanceof ClientPlayerEntity) {
                resetAll();
            }
        });

        ParadoxBlock.solid = CAntimonicView::solidifyParadox;
        BlockRenderLayerMap.putBlock(OccBloccs.PARADOX, BlockRenderLayer.CUTOUT);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> CAntimonicView.setup());

        ClientSendMessageEvents.ALLOW_CHAT.register(CProjectionView::introspect);
        ClientSendMessageEvents.ALLOW_CHAT.register(CExteriorityView::letsGetThisOverWith);
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

    private static void resetAll() {
        CBestView.reset();
        CExteriorityView.reset();
        CAntimonicView.reset();
        CInversionView.reset();
        CTreacherousView.reset();
    }
}