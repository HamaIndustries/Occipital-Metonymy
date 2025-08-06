package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
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
