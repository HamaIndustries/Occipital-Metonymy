package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.obv.OccEntities;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapOperation(
            method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getBasicProjectionMatrix(F)Lorg/joml/Matrix4f;")
    )
    private Matrix4f backflip(GameRenderer instance, float fovDegrees, Operation<Matrix4f> original) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.getAttachedOrElse(OccEntities.INVERTED, false)) {
            return original.call(instance, fovDegrees).scale(-1);
        }
        return original.call(instance, fovDegrees);
    }
}
