package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.ent.ProjectionRenderer;
import symbolics.division.occmy.client.view.Perspectives;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Final
    @Shadow
    private Framebuffer framebuffer;

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;blitToScreen()V")
    )
    public void shutter(Framebuffer instance, Operation<Void> original, @Local(ordinal = 0) Framebuffer frameBuffer) {
        if (Perspectives.OBSCURED.living() && !Perspectives.OBSCURED.complete()) {
            RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(frameBuffer.getColorAttachment(), 0, frameBuffer.getDepthAttachment(), 1.0);
        }
        original.call(instance);
    }

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;swapBuffers(Lnet/minecraft/client/util/tracy/TracyFrameCapturer;)V")
    )
    public void capture(boolean tick, CallbackInfo ci) {
        ProjectionRenderer.capture(framebuffer);
    }

    @Inject(
            method = "openGameMenu",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z")
    )
    public void nullityAbsolution(boolean pauseOnly, CallbackInfo ci) {
        if (Perspectives.OBSCURED.isActive()
                && (OCCMYClient.player() == null || OCCMYClient.player().isRemoved())) {
            OCCMYClient.nextTick(() -> GameMenuScreen.disconnect(MinecraftClient.getInstance(), ClientWorld.QUITTING_MULTIPLAYER_TEXT));
        }
    }
}
