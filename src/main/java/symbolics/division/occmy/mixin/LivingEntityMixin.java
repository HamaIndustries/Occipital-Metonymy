package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.view.AntimonicView;
import symbolics.division.occmy.view.TreacherousView;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "travelMidAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEffectiveGravity()D")
    )
    public double lie(LivingEntity instance, Operation<Double> original) {
        if (((Entity) (Object) this) instanceof PlayerEntity player && (TreacherousView.active(player) || AntimonicView.active(player))) {
            return 0;
        }
        return original.call(instance);
    }
}
