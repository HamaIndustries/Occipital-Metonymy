package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.ent.IStringedEntity;
import symbolics.division.occmy.obv.OccEntities;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Unique
    private boolean occmy$stringed(Entity entity) {
        return entity instanceof IStringedEntity other && other.occmy$isStringed();
    }

    @WrapOperation(
            method = "onEntityPositionSync(Lnet/minecraft/network/packet/s2c/play/EntityPositionSyncS2CPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;hasEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    public boolean desyncOnPurpose(ClientWorld instance, Entity entity, Operation<Boolean> original) {
        if (entity instanceof PlayerEntity player) {
            boolean proj = player.getAttachedOrSet(OccEntities.PROJECTING, false);
            return original.call(instance, entity) && !proj;
        }
        return original.call(instance, entity);
    }

    @Inject(
            method = "onEntityPositionSync",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/EntityPositionSyncS2CPacket;values()Lnet/minecraft/entity/player/PlayerPosition;"),
            cancellable = true
    )
    public void desyncOnAccident(EntityPositionSyncS2CPacket packet, CallbackInfo ci, @Local(ordinal = 0) Entity entity) {
        if (occmy$stringed(entity)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void cancelEntity(EntityS2CPacket packet, CallbackInfo ci) {
        World world = MinecraftClient.getInstance().world;
        if (world != null && occmy$stringed(packet.getEntity(world))) {
            ci.cancel();
        }
    }
}
