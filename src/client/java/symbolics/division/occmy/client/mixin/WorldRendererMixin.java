package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
//    @WrapOperation(
//            method = "renderMain",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntities(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/RenderTickCounter;Ljava/util/List;)V")
//    ) private void flip() {
//
//    }

    @WrapMethod(
            method = "renderEntities"
    )
    public void backflip(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, RenderTickCounter tickCounter, List<Entity> entities, Operation<Void> original) {
        matrices.push();
//        matrices.peek().getPositionMatrix();
        original.call(matrices, vertexConsumers, camera, tickCounter, entities);
        matrices.pop();
    }
}
