package symbolics.division.occmy.client.mixin;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.ent.IStringedEntity;
import symbolics.division.occmy.client.view.CExteriorityView;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.obv.OccEntities;

import java.util.UUID;

@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity implements IStringedEntity {
    private LazyEntityReference<MarionetteEntity> ref = new LazyEntityReference<>(UUID.randomUUID());

    public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
            method = "onSpawnPacket",
            at = @At("TAIL")
    )
    private void afterSpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        occmy$cut();
        if (CExteriorityView.active()) {
            occmy$rig();
        }
    }

    @Unique
    private MarionetteEntity occmy$controller;

    @Override
    public void occmy$rig() {
        if (occmy$controller != null) {
            occmy$controller.remove(RemovalReason.DISCARDED);
            occmy$controller = null;
        }
        occmy$controller = OccEntities.MARIONETTE.create(this.getWorld(), SpawnReason.LOAD);
        ref = new LazyEntityReference<>(occmy$controller);
        occmy$controller.setPos(this.getX(), this.getY(), this.getZ());
        this.clientWorld.addEntity(occmy$controller);
        occmy$controller.setControl(this);
    }

    @Override
    public void occmy$cut() {
        if (occmy$controller == null) return;
        occmy$controller.remove(RemovalReason.DISCARDED);
        occmy$controller = null;
    }

    @Override
    public boolean occmy$isStringed() {
        return occmy$controller != null;
    }
}
