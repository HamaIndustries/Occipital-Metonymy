package symbolics.division.occmy.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import symbolics.division.occmy.client.view.CExteriorityView;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin {
    @WrapMethod(
            method = "sendPacket"
    )
    public void inspect(Packet<?> packet, Operation<Void> original) {
        if (CExteriorityView.active() && packet instanceof PlayerMoveC2SPacket) return;
        original.call(packet);
    }
}
