package symbolics.division.occmy.obv;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SingleStackRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.craft.Retrospection;

public class OccEtc {
    public static void init() {
        Recipe.init();
        Damage.init();
    }

    public static final class Recipe {
        public static void init() {
        }

        public static final RecipeType<Retrospection> RETROSPECTION = Registry.register(Registries.RECIPE_TYPE, OCCMY.id("retrospection"), new RecipeType<Retrospection>() {
            @Override
            public String toString() {
                return OCCMY.id("retrospection").toString();
            }
        });

        public static final RecipeSerializer<Retrospection> RETROSPECTION_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER, OCCMY.id("retrospection"), new SingleStackRecipe.Serializer<>(Retrospection::new)
        );

//        public static final RegistryKey<RecipePropertySet> RETROSPECTION_PROPERTY = RegistryKey.of(RecipePropertySet.REGISTRY, OCCMY.id("retrospection"));
    }

    public static final class Damage {
        public static final RegistryKey<DamageType> HOLLOWING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, OCCMY.id("hollowing"));

        public static void init() {
        }
    }
}
