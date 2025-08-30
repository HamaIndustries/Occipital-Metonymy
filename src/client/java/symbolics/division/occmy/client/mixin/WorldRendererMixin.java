package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.view.CExteriorityView;
import symbolics.division.occmy.client.view.CTreacherousView;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @WrapWithCondition(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Camera;FLcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
    )
    public boolean a(WorldRenderer instance, FrameGraphBuilder frameGraphBuilder, Camera camera, float tickProgress, GpuBufferSlice fog) {
        return !CTreacherousView.active();
    }

    @Inject(
            method = "renderEntities",
            at = @At("TAIL")
    )
    public void BWO(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, RenderTickCounter tickCounter, List<Entity> entities, CallbackInfo ci) {
        PlayerEntity player = OCCMYClient.player();
        if (CExteriorityView.active() && player != null) {
            CExteriorityView.bwo(this::renderEntity, matrices, vertexConsumers, camera
                    , player);
        }
    }
}
