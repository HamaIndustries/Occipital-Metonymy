package symbolics.division.occmy.client.mixin;

import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
//    @WrapMethod(
//            method = "getAdjustedVolume(FLnet/minecraft/sound/SoundCategory;)F"
//    )
//    private float shutterMute(float volume, SoundCategory category, Operation<Float> original) {
//        if (Perspectives.OBSCURED.living()) {
//            return 0;
//        }
//        return original.call(volume, category);
//    }
}
