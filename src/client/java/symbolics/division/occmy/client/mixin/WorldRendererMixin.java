package symbolics.division.occmy.client.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
//    @WrapMethod(
//            method = "renderEntities"
//    )
//    public void backflip(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, RenderTickCounter tickCounter, List<Entity> entities, Operation<Void> original) {
//        matrices.push();
//
////        matrices.peek().getPositionMatrix();
//        original.call(matrices, vertexConsumers, camera, tickCounter, entities);
//        matrices.pop();
//    }


}
