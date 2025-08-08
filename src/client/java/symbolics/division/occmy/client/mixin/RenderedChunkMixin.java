package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.RenderedChunk;
import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.client.view.CAntimonicView;
import symbolics.division.occmy.obv.OccBloccs;

// ask not the sparrow why the eagle soars
@Mixin(RenderedChunk.class)
public class RenderedChunkMixin {
    @WrapOperation(
            method = "getBlockState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/PalettedContainer;get(III)Ljava/lang/Object;")
    )
    public <T> Object offset(PalettedContainer<T> instance, int x, int y, int z, Operation<T> original) {
        if (CAntimonicView.solidifyParadox()) {
            PalettedContainer.Data<T> data = occmy$getData(instance);
            int id = occmy$getId(instance, data, x, y, z);

            BlockState state = (BlockState) data.palette().get(id);
            if (state.isOf(OccBloccs.COROLLARY)) return data.palette().get(0);
            if (id > 1) {
                return data.palette().get(Math.max(0, id - 1));
            } else {
                return state;
            }
        }

        BlockState state = (BlockState) original.call(instance, x, y, z);
        // tell me if this is a performance hazard and I'll take it out
        if (state.isOf(OccBloccs.COROLLARY)) {
            PalettedContainer.Data<T> data = occmy$getData(instance);
            return data.palette().get(1);
        }
        return state;
    }

    private <T> PalettedContainer.Data<T> occmy$getData(PalettedContainer<T> instance) {
        return ((PalettedContainerAccessor) instance).getData();
    }

    private <T> int occmy$getId(PalettedContainer<T> instance, PalettedContainer.Data<T> data, int x, int y, int z) {
        PaletteStorage storage = ((PalettedContainerDataAccessor) (Object) data).getStorage();
        int index = ((PalettedContainerAccessor) instance).getPaletteProvider().computeIndex(x, y, z);
        return storage.get(index);
    }
}
