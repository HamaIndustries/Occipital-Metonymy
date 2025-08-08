package symbolics.division.occmy.client.mixin;

import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PalettedContainer.Data.class)
public interface PalettedContainerDataAccessor {
    @Accessor
    PaletteStorage getStorage();
}
