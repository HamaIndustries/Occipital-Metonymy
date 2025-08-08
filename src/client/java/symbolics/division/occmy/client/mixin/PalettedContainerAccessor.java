package symbolics.division.occmy.client.mixin;

import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PalettedContainer.class)
public interface PalettedContainerAccessor<T> {
    @Accessor
    PalettedContainer.PaletteProvider getPaletteProvider();

    @Invoker
    T invokeGet(int index);

    @Accessor
    PalettedContainer.Data<T> getData();
}
