package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.view.Views;

@Mixin(Entity.class)
public class EntityMixin {
    private boolean occmy$player = false;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void mark(EntityType type, World world, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof ServerPlayerEntity) {
            occmy$player = true;
        }
    }

    @WrapOperation(
            method = "move",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;")
    )
    public Vec3d hereWeAreYetAgainMyOldFriend(Entity instance, Vec3d movement, Operation<Vec3d> original) {
        // does not affect pressure plate
        if (occmy$player && Views.immaterial((ServerPlayerEntity) (Object) this)) {
            return movement;
        }
        return original.call(instance, movement);
    }

    @WrapOperation(
            method = "move",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z")
    )
    public boolean ok(Entity instance, Operation<Boolean> original) {
        return original.call(instance) || (occmy$player && Views.immaterial((ServerPlayerEntity) (Object) this));
    }

    @WrapMethod(
            method = "isInsideWall"
    )
    public boolean wallEyes(Operation<Boolean> original) {
        if (occmy$player && ((Object) this) instanceof ServerPlayerEntity player) {
            boolean v = Views.immaterial(player);
            return original.call() && !v && !Views.matryoshka(player, o -> (o instanceof ServerPlayerEntity other && Views.immaterial(other)));
        }
        return original.call();
    }
}
