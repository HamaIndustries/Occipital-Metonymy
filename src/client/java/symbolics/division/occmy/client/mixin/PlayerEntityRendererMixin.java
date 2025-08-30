package symbolics.division.occmy.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.ent.TurnkeyState;
import symbolics.division.occmy.client.view.CExteriorityView;
import symbolics.division.occmy.obv.OccEntities;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(
            method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At("TAIL")
    )
    public void anonymous(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (abstractClientPlayerEntity.getAttachedOrElse(OccEntities.OBSCURED, false)) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != abstractClientPlayerEntity && player.canSee(abstractClientPlayerEntity)) {
                abstractClientPlayerEntity.setAttached(OccEntities.OBSCURED, false);
            } else {
                playerEntityRenderState.playerName = null;
                playerEntityRenderState.nameLabelPos = null;
            }
        }

        if (abstractClientPlayerEntity.hasAttached(OccEntities.ENJOINED)) {
            ((TurnkeyState) playerEntityRenderState).occmy$setTurnkey(true);
        } else {
            ((TurnkeyState) playerEntityRenderState).occmy$setTurnkey(false);
        }

        if (CExteriorityView.proxying) {
            playerEntityRenderState.handSwinging = false;
            playerEntityRenderState.pitch = 0;
            playerEntityRenderState.bodyYaw = CExteriorityView.getYaw();
            playerEntityRenderState.relativeHeadYaw = 0;
            playerEntityRenderState.shaking = true;
            playerEntityRenderState.pose = EntityPose.STANDING;
            playerEntityRenderState.limbSwingAnimationProgress = 0;
            playerEntityRenderState.limbSwingAmplitude = 0;
            playerEntityRenderState.positionOffset = new Vec3d(0, abstractClientPlayerEntity.getHeight() + 0.1, 0);
            playerEntityRenderState.sneaking = false;
            playerEntityRenderState.isInSneakingPose = false;
        }
    }
}
