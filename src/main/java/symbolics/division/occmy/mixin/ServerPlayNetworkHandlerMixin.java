package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.view.Views;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
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
}
