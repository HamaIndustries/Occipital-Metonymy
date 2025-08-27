package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.client.view.CTreacherousView;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @WrapWithCondition(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Camera;FLcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
    )
    public boolean a(WorldRenderer instance, FrameGraphBuilder frameGraphBuilder, Camera camera, float tickProgress, GpuBufferSlice fog) {
        return !CTreacherousView.active();
    }
}
