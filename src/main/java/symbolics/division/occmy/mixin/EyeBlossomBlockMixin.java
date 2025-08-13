package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.EyeblossomBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import symbolics.division.occmy.craft.Retrospection;

@Mixin(EyeblossomBlock.class)
public abstract class EyeBlossomBlockMixin {
    @WrapMethod(
            method = "updateStateAndNotifyOthers"
    )
    public boolean witness(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Boolean> original) {
        boolean result = original.call(state, world, pos, random);
        if (result) Retrospection.perform(state, world, pos);
        return result;
    }
}
