package symbolics.division.occmy.client;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Models;
import symbolics.division.occmy.client.gfx.ThetiscopeFullnessProperty;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccItems;

public class OCCMYDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(OccModels::new);
    }

    private static class OccModels extends FabricModelProvider {
        public OccModels(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            blockStateModelGenerator.registerRotatable(OccBloccs.THETISCOPE);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_EXTERIORITY, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_BEST, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_INVERSION, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.output.accept(
                    OccItems.THETISCOPE,
                    ItemModels.condition(
                            new ThetiscopeFullnessProperty(),
                            ItemModels.basic(
                                    itemModelGenerator.registerSubModel(OccItems.THETISCOPE, "_full", Models.GENERATED)
                            ),
                            ItemModels.basic(
                                    itemModelGenerator.registerSubModel(OccItems.THETISCOPE, "_empty", Models.GENERATED)
                            )
                    )
            );
        }
    }
}
