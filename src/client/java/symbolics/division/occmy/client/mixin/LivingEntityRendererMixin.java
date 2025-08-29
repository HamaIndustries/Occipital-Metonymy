package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import symbolics.division.occmy.client.OCCMYClient;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @WrapMethod(
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z"
    )
    private <T extends LivingEntity> boolean anonymize(T livingEntity, double d, Operation<Boolean> original) {
        if (livingEntity instanceof PlayerEntity player && OCCMYClient.inRestrictedArea(player)) {
            return false;
        }
        return original.call(livingEntity, d);
    }

//    private boolean occmy$rerunning = false;
//
//    @Inject(
//            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
//            at = @At("TAIL")
//    )
//    public <S extends LivingEntityRenderState> void rerun(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        if (!occmy$rerunning && livingEntityRenderState.)
//    }
}
