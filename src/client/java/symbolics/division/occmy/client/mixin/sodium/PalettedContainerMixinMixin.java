package symbolics.division.occmy.client.mixin.sodium;

import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PalettedContainer.class, priority = 1500)
public class PalettedContainerMixinMixin {
//    @TargetHandler(
//            mixin = "net.caffeinemc.mods.sodium.mixin.core.world.chunk.PalettedContainerMixin",
//            name = "Lnet/caffeinemc/mods/sodium/mixin/core/world/chunk/PalettedContainerMixin;sodium$unpack([Ljava/lang/Object;IIIIII)V"
//    )
//    @WrapOperation(
//            method = "@MixinSquared:Handler",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Palette;get(I)Ljava/lang/Object;")
//    )
//    public <T> T a(Palette<T> instance, int paletteIndex, Operation<T> original, @Local(ordinal = 0) PaletteStorage storage, @Local(ordinal = 0) Palette<T> palette) {
//        BlockState state = (BlockState) original.call(instance, paletteIndex);
//        if (CAntimonicView.solidifyParadox()) {
//            if (state.isOf(OccBloccs.COROLLARY)) {
//                return (T) Blocks.AIR.getDefaultState();
//            }
//            return original.call(instance, Math.max(0, paletteIndex - 1));
//        }
//        return (T) (state.isOf(OccBloccs.COROLLARY) ? palette.get(0) : state);
//    }
}
