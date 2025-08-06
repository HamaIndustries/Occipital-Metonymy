package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.client.view.Perspectives;

@Mixin(Mouse.class)
public class MouseMixin {
    @WrapWithCondition(
            method = "updateMouse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V")
    )
    private boolean a(ClientPlayerEntity instance, double u, double v) {
        return !Perspectives.OBSCURED.living();
    }
}
