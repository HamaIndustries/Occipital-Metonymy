package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.render.entity.state.PaintingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.view.CTreacherousView;

@Mixin(PaintingEntityRenderer.class)
public class PaintingEntityMixin {
    @WrapMethod(
            method = "render(Lnet/minecraft/client/render/entity/state/PaintingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
    )
    public void wahoo(PaintingEntityRenderState paintingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Operation<Void> original) {
        if (OCCMYClient.player() != null && !CTreacherousView.active(OCCMYClient.player()))
            original.call(paintingEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }
}
