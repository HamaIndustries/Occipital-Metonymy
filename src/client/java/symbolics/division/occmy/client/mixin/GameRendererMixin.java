package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.client.view.CInversionView;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapOperation(
            method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getBasicProjectionMatrix(F)Lorg/joml/Matrix4f;")
    )
    private Matrix4f backflip(GameRenderer instance, float fovDegrees, Operation<Matrix4f> original) {
        if (CInversionView.active()) {
            return original.call(instance, fovDegrees).scale(-1);
        }
        return original.call(instance, fovDegrees);
    }
}
