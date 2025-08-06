package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.block.ThetiscopeBlock;
import symbolics.division.occmy.block.ThetiscopeBlockEntity;

import java.util.function.Function;

public class OccBloccs {
    public static void init() {
    }

    public static final ThetiscopeBlock THETISCOPE = register("thetiscope", ThetiscopeBlock::new, AbstractBlock.Settings.create()
            .pistonBehavior(PistonBehavior.DESTROY)
            .nonOpaque()
            .strength(0.1f));

    public static final BlockEntityType<ThetiscopeBlockEntity> THETISCOPE_BLOCK_ENTITY =
            register("counter", ThetiscopeBlockEntity::new, THETISCOPE);

    private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
        Identifier identifier = OCCMY.id(id);
        var key = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        return Registry.register(Registries.BLOCK, key, factory.apply(settings.registryKey(key)));
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = OCCMY.id(name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
