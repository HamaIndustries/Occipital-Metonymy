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
}
