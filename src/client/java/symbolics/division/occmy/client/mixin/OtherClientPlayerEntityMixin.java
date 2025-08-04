package symbolics.division.occmy.client.mixin;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.obv.OccEntities;

import java.util.UUID;

@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    private LazyEntityReference<MarionetteEntity> ref = new LazyEntityReference<>(UUID.randomUUID());

    public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void hackTick(CallbackInfo ci) {
        // left right never odd or even // stop pots
        // up down Name now one man // race car
        
    }

    @Inject(
            method = "onSpawnPacket",
            at = @At("TAIL")
    )
    private void afterSpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        MarionetteEntity doll = OccEntities.MARIONETTE.create(this.getWorld(), SpawnReason.LOAD);
        ref = new LazyEntityReference<>(doll);
        doll.setPos(this.getX(), this.getY(), this.getZ());
        this.clientWorld.addEntity(doll);
    }
}
