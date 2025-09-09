package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.ent.ProjectionEntity;

public class OccEntities {
    public static void init() {
        FabricDefaultAttributeRegistry.register(MARIONETTE, MarionetteEntity.createAttributes());
    }

    public static final AttachmentType<Boolean> PROJECTING = AttachmentRegistry.create(OCCMY.id("projecting"));
    public static final AttachmentType<Boolean> OBSCURED = AttachmentRegistry.create(OCCMY.id("obscured"));
    public static final AttachmentType<Boolean> INVERTED = AttachmentRegistry.create(OCCMY.id("inverted"));
    public static final AttachmentType<Unit> CONTRADICTORY = AttachmentRegistry.create(OCCMY.id("contradictory"),
            builder -> builder.syncWith(Unit.PACKET_CODEC, AttachmentSyncPredicate.targetOnly())
    );
    public static final AttachmentType<Vec3d> BETRAYAL_LOCUS = AttachmentRegistry.create(OCCMY.id("betrayal_locus"),
            builder -> builder.syncWith(Vec3d.PACKET_CODEC, AttachmentSyncPredicate.targetOnly())
    );

    public static final AttachmentType<Unit> CURSED = AttachmentRegistry.create(OCCMY.id("cursed"));
    public static final AttachmentType<Integer> PROJECTION_PROTECTION = AttachmentRegistry.create(OCCMY.id("cooldown"));
    public static final AttachmentType<Unit> ENJOINED = AttachmentRegistry.create(OCCMY.id("enjoined"),
            builder -> builder.persistent(Unit.CODEC).syncWith(Unit.PACKET_CODEC, AttachmentSyncPredicate.all()).copyOnDeath());
    public static final AttachmentType<Unit> IMMUNE = AttachmentRegistry.create(OCCMY.id("immune"),
            builder -> builder.persistent(Unit.CODEC).syncWith(Unit.PACKET_CODEC, AttachmentSyncPredicate.all()).copyOnDeath());


    public static final RegistryKey<EntityType<?>> PROJECTION_KEY = RegistryKey.of(
            Registries.ENTITY_TYPE.getKey(),
            Identifier.of(OCCMY.ID, "projection")
    );

    public static final EntityType<ProjectionEntity> PROJECTION = Registry.register(
            Registries.ENTITY_TYPE,
            PROJECTION_KEY.getValue(),
            EntityType.Builder.create(ProjectionEntity::new, SpawnGroup.MISC).dimensions(0.75f, 0.75f)
                    .build(PROJECTION_KEY));

    public static final RegistryKey<EntityType<?>> MARIONETTE_KEY = RegistryKey.of(
            Registries.ENTITY_TYPE.getKey(),
            Identifier.of(OCCMY.ID, "marionette")
    );

    public static final EntityType<MarionetteEntity> MARIONETTE = Registry.register(
            Registries.ENTITY_TYPE,
            MARIONETTE_KEY.getValue(),
            EntityType.Builder.create(MarionetteEntity::new, SpawnGroup.MISC).dimensions(0.75f, 0.75f)
                    .build(MARIONETTE_KEY));
}
