package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.view.TreacherousView;
import symbolics.division.occmy.view.Views;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @WrapOperation(
            method = "onPlayerMove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;shouldCheckMovement(Z)Z")
    )
    public boolean doYouHaveAnyIdeaWhoYouAreDealingWith(ServerPlayNetworkHandler instance, boolean elytra, Operation<Boolean> original) {
        return false; // #lol
    }

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSleeping()Z")
    )
    public boolean actuallyFeelingKindOfEepyRN(ServerPlayerEntity instance, Operation<Boolean> original) {
        return true;
    }

    @WrapOperation(
            method = "onPlayerMove",
            at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;noClip:Z")
    )
    public boolean doesThisWork(ServerPlayerEntity instance, Operation<Boolean> original) {
        return original.call(instance) || Views.immaterial(instance);
    }

    @WrapWithCondition(
            method = "onPlayerInteractBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V")
    )
    public boolean betrayInKind(ServerPlayNetworkHandler instance, Packet packet) {
        return !TreacherousView.active(this.player) || !TreacherousView.deceived(this.player, ((BlockUpdateS2CPacket) packet).getPos());
    }
}
