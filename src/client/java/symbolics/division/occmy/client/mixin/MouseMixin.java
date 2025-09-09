package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import symbolics.division.occmy.client.view.Perspectives;
import symbolics.division.occmy.net.C2SRotaryPayload;
import symbolics.division.occmy.obv.OccComponents;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @WrapWithCondition(
            method = "updateMouse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V")
    )
    private boolean a(ClientPlayerEntity instance, double u, double v) {
        return !Perspectives.OBSCURED.living();
    }

    @WrapWithCondition(
            method = "onMouseScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V")
    )
    public boolean unscroll(PlayerInventory instance, int slot) {
        if (this.client.player.getStackInHand(Hand.MAIN_HAND).contains(OccComponents.ROTARY) && this.client.player.isSneaking()) {
            int amt = this.client.options.getDiscreteMouseScroll().getValue() ? 1 : 0;
            ClientPlayNetworking.send(new C2SRotaryPayload(amt));
            return false;
        }
        return true;
    }
}
