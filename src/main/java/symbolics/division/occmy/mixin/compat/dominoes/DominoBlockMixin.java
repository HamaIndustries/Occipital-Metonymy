package symbolics.division.occmy.mixin.compat.dominoes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sisby.dominoes.Dominoes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.compat.Unfall;

@Pseudo
@Mixin(targets = "dev.sisby.dominoes.DominoBlock", remap = false)
public abstract class DominoBlockMixin {
    @Inject(
            method = "collapse",
            at = @At("TAIL")
    )
    public void unfall(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean forwards, boolean initial, CallbackInfo ci) {
        if (state.isOf(Unfall.MYSTERIOUS_DOMINO_BLOCK)) {
            Unfall.unf_all(world, pos);
        }
    }

    @WrapOperation(
            method = "neighborUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    )
    public boolean extend(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, Dominoes.DOMINO_BLOCK) || original.call(instance, Unfall.MYSTERIOUS_DOMINO_BLOCK);
    }
}
