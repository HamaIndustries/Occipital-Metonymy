package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.gfx.Effects;
import symbolics.division.occmy.client.gfx.RenderBlueSkyRed;
import symbolics.division.occmy.client.view.CAntimonicView;
import symbolics.division.occmy.client.view.CExteriorityView;
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

    @Inject(
            method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
    )
    public void deepen(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null) {
            var buffer = MinecraftClient.getInstance().getFramebuffer();
            if (CExteriorityView.active()) {
                Effects.DEPTH.apply(buffer);
            }
            if (CAntimonicView.solidifyParadox()) {
                Effects.DISHARMONY.apply(buffer);
            }
        }
    }

    @WrapOperation(
            method = "renderWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;")
    )
    public Vector4f paintTheBlueSkyRed(FogRenderer instance, Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world, Operation<Vector4f> original) {
        return RenderBlueSkyRed.paintTheBlueSkyRed(instance, camera, viewDistance, thick, tickCounter, skyDarkness, world, original);
    }
}
