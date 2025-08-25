package symbolics.division.occmy.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.compat.Unfall;

@Pseudo
@Mixin(targets = "dev.sisby.dominoes.Dominoes", remap = false)
public class DominoesCompat {
    @Inject(
            method = "onInitialize",
            at = @At("TAIL")
    )
    public void initCompat(CallbackInfo ci) {
        Unfall.setup();
    }
}
