package symbolics.division.occmy.craft;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import symbolics.division.occmy.obv.OccEtc;

public class Retrospection extends SingleStackRecipe {
    public Retrospection(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public RecipeSerializer<Retrospection> getSerializer() {
        return OccEtc.Recipe.RETROSPECTION_SERIALIZER;
    }

    @Override
    public RecipeType<? extends SingleStackRecipe> getType() {
        return OccEtc.Recipe.RETROSPECTION;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static void perform(BlockState state, ServerWorld world, BlockPos pos) {
        for (ItemEntity entity : world.getEntitiesByClass(ItemEntity.class, new Box(pos), stack -> true)) {
            ItemStack stack = entity.getStack();
            SingleStackRecipeInput input = new SingleStackRecipeInput(stack);
            var entryOptional = world.getRecipeManager().getFirstMatch(OccEtc.Recipe.RETROSPECTION, input, world);
            if (entryOptional.isEmpty()) continue;
            RecipeEntry<Retrospection> entry = entryOptional.get();
            ItemStack result = entry.value().craft(input, world.getRegistryManager());
            entity.setStack(result);
        }
    }
}
