package symbolics.division.occmy.client;

import dev.doublekekse.area_lib.data.AreaClientData;
import dev.doublekekse.area_lib.data.AreaSavedData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.block.ParadoxBlock;
import symbolics.division.occmy.client.ent.ProjectionRenderer;
import symbolics.division.occmy.client.ent.TurnkeyFeatureRenderer;
import symbolics.division.occmy.client.ent.TurnkeyModel;
import symbolics.division.occmy.client.gfx.AntimonicConsistencyProperty;
import symbolics.division.occmy.client.gfx.ThetiscopeFullnessProperty;
import symbolics.division.occmy.client.view.*;
import symbolics.division.occmy.compat.ProjectionRestrictionAreaComponent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                    updateRestriction();
                    AFFAIRS.tick();
                    if (client.player != null) {
                        client.player.setAttached(OccEntities.PROJECTION_PROTECTION, client.player.getAttachedOrSet(OccEntities.PROJECTION_PROTECTION, 0) - 1);
                    }
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
        ClientSendMessageEvents.ALLOW_CHAT.register(CNullView::letsGetThisOverWith);
        ClientReceiveMessageEvents.ALLOW_CHAT.register(CNullView::silence);

        EntityModelLayerRegistry.registerModelLayer(
                TurnkeyModel.TURNKEY_LAYER,
                TurnkeyModel::getTexturedModelData
        );

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityType == EntityType.PLAYER) {
                        registrationHelper.register(new TurnkeyFeatureRenderer<>((PlayerEntityRenderer) entityRenderer, context.getEntityModels()));
                    }
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
        synchronized (AFFAIRS) {
            AFFAIRS.enable(new Necessity(r, ticks));
        }
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
        CInversionView.reset();
        CTreacherousView.reset();
    }

    private static Map<UUID, Boolean> restrictions = new HashMap<>();

    public static boolean inRestrictedArea(PlayerEntity player) {
        boolean otherRestricted = restrictions.getOrDefault(player.getUuid(), false);
        boolean selfRestricted = restrictions.getOrDefault(player().getUuid(), false);
        return otherRestricted != selfRestricted;
    }

    private static void updateRestriction() {
        AreaSavedData data = AreaClientData.getClientLevelData();
        if (data == null) {
            restrictions.clear();
            return;
        }

        for (PlayerEntity player : world().getPlayers()) {
            boolean restricted = data.findTrackedAreasContaining(player)
                    .stream().anyMatch(
                            area -> {
                                var component = area.get(ProjectionRestrictionAreaComponent.TYPE);
                                if (component != null) return component.restriction() != null;
                                return false;
                            }
                    );
            restrictions.put(player.getUuid(), restricted);
        }


    }
}