package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.obv.OccEntities;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @WrapOperation(
            method = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;onEntityPositionSync(Lnet/minecraft/network/packet/s2c/play/EntityPositionSyncS2CPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;hasEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    public boolean a(ClientWorld instance, Entity entity, Operation<Boolean> original) {
        if (entity instanceof PlayerEntity player) {
            boolean proj = player.getAttachedOrSet(OccEntities.PROJECTING, false);
            return original.call(instance, entity) && !proj;
        }
        return original.call(instance, entity);
    }
}
