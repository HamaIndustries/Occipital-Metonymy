package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.ent.IStringedEntity;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(
            method = "removeEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onRemoved()V")
    )
    public void absolve(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof IStringedEntity stringed) {
            stringed.occmy$cut();
        }
    }
}
