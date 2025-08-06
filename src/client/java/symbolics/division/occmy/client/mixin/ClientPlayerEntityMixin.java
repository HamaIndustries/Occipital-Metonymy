package symbolics.division.occmy.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.view.Perspectives;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity {

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "tickMovementInput",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hijackMotorCortex(CallbackInfo ci) {
        if (Perspectives.OBSCURED.living()) {
            this.sidewaysSpeed = 0;
            this.forwardSpeed = 0;
            this.jumping = false;
            ci.cancel();
        }
    }
}
