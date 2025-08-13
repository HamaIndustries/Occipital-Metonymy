package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.render.entity.state.PaintingEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.view.CAntimonicView;

@Mixin(PaintingEntityRenderer.class)
public abstract class PaintingEntityRendererMixin {
    @Shadow
    private void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float u, float v, float z, int normalX, int normalY, int normalZ, int light) {
    }

    @Unique
    private boolean occmy$active = false;

    @Inject(
            method = "renderPainting",
            at = @At("HEAD"),
            cancellable = true
    )
    public void a(MatrixStack matrices, VertexConsumer vertexConsumer, int[] lightmapCoordinates, int width, int height, Sprite paintingSprite, Sprite backSprite, CallbackInfo ci) {
        if (!occmy$active) return;
        float left = -width / 2.0F;
        float bottom = -height / 2.0F;
        float zOffset = -0.03125F;
        MatrixStack.Entry entry = matrices.peek();
        this.vertex(entry, vertexConsumer, -left, bottom, 0, 1, zOffset, 0, 0, 1, lightmapCoordinates[0]);
        this.vertex(entry, vertexConsumer, left, bottom, 1, 1, zOffset, 0, 0, 1, lightmapCoordinates[0]);
        this.vertex(entry, vertexConsumer, left, -bottom, 1, 0, zOffset, 0, 0, 1, lightmapCoordinates[0]);
        this.vertex(entry, vertexConsumer, -left, -bottom, 0, 0, zOffset, 0, 0, 1, lightmapCoordinates[0]);
        ci.cancel();
    }


    @WrapOperation(
            method = "render(Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolidZOffsetForward(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;")
    )
    public RenderLayer reskin(Identifier texture, Operation<RenderLayer> original, @Local PaintingEntityRenderState state) {
        if (CAntimonicView.solidifyParadox() && state.variant != null) {
            Identifier trace = CAntimonicView.getTrace(state.variant.assetId());
            if (trace != null) {
                occmy$active = true;
                return original.call(trace);
            }
        }
        occmy$active = false;
        return original.call(texture);
    }
}
