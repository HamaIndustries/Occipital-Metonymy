package symbolics.division.occmy.client;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;
import symbolics.division.occmy.client.gfx.AntimonicConsistencyProperty;
import symbolics.division.occmy.client.gfx.ThetiscopeFullnessProperty;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccItems;

public class OCCMYDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(OccModels::new);
//        pack.addProvider(OccRecipes::new);
    }

    private static class OccModels extends FabricModelProvider {
        public OccModels(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            blockStateModelGenerator.registerSimpleCubeAll(OccBloccs.PARADOX);
            blockStateModelGenerator.registerSimpleCubeAll(OccBloccs.COROLLARY);
//            blockStateModelGenerator.registerItemModel(OccItems.FLOPSTER_DRIVE_MODEL_B, blockStateModelGenerator.uploadBlockItemModel(OccItems.FLOPSTER_DRIVE_MODEL_B, OccBloccs.FLOPSTER_DRIVE_MODEL_B));
//            blockStateModelGenerator.registerItemModel(OccBloccs.FLOPSTER_DRIVE_MODEL_B);
//            blockStateModelGenerator.registerItemModel(OccBloccs.FLOPSTER_DRIVE_MODEL_B);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            registerAntimony(itemModelGenerator, OccItems.BLOCK_PARADOX, OccItems.BLOCK_COROLLARY, false);
            registerAntimony(itemModelGenerator, OccItems.BLOCK_COROLLARY, OccItems.BLOCK_PARADOX, true);
//            itemModelGenerator.register(OccItems.BLOCK_PARADOX, Models.GENERATED);
//            itemModelGenerator.register(OccItems.BLOCK_COROLLARY, Models.GENERATED);
            itemModelGenerator.register(OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_EXTERIORITY, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_BEST, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_INVERSION, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_ANTIMONY, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_TREACHERY, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_NULLITY, OccItems.DISK_PROJECTION, Models.GENERATED);
            itemModelGenerator.registerWithTextureSource(OccItems.DISK_GROUNDING, OccItems.DISK_PROJECTION, Models.GENERATED);
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
            itemModelGenerator.output.accept(OccItems.FLOPSTER_DRIVE_MODEL_B, ItemModels.basic(ModelIds.getBlockModelId(OccBloccs.FLOPSTER_DRIVE_MODEL_B)));
        }

        private static void registerAntimony(ItemModelGenerator itemModelGenerator, Item item, Item other, boolean invert) {
            String s1 = invert ? "_true" : "_false";
            String s2 = invert ? "_false" : "_true";
            itemModelGenerator.output.accept(
                    item,
                    ItemModels.condition(
                            new AntimonicConsistencyProperty(),
                            ItemModels.basic(
                                    Models.GENERATED.upload(ModelIds.getItemSubModelId(item, s1), TextureMap.layer0(TextureMap.getId(item)), itemModelGenerator.modelCollector)
                            ),
                            ItemModels.basic(
                                    Models.GENERATED.upload(ModelIds.getItemSubModelId(item, s2), TextureMap.layer0(TextureMap.getId(other)), itemModelGenerator.modelCollector)
                            )
                    )
            );
        }
    }

//    private static class OccRecipes extends FabricRecipeProvider {
//        public OccRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
//            super(output, registriesFuture);
//        }
//
//        @Override
//        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
//            return new RecipeGenerator(registryLookup, exporter) {
//                @Override
//                public void generate() {
//                    RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
//                    offerStonecuttingRecipe();
//                }
//
//                private void offerRetrospection(ItemConvertible output, ItemConvertible input) {
//                    CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItem(input), RecipeCategory.BUILDING_BLOCKS, output, 0.1F, 200)
//                            .criterion(hasItem(input), this.conditionsFromItem(input))
//                            .offerTo(this.exporter);
//                }
//            };
//
//
//        }
//
//        @Override
//        public String getName() {
//            return "OCCMYRECIPES";
//        }
//    }
}
