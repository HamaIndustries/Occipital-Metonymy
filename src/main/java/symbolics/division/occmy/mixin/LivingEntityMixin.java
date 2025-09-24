package symbolics.division.occmy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import symbolics.division.occmy.obv.OccItems;
import symbolics.division.occmy.view.Views;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = "travelMidAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEffectiveGravity()D")
    )
    public double lie(LivingEntity instance, Operation<Double> original) {
        if (((Entity) (Object) this) instanceof PlayerEntity player && Views.immaterial(player)) {
            return 0;
        }
        return original.call(instance);
    }

    @Inject(
            method = "dropItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;createItemEntity(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;")
    )
    public void soteria(ItemStack stack, boolean dropAtSelf, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (stack.isOf(OccItems.THETISCOPE) && ((Entity) (Object) this) instanceof ServerPlayerEntity player) {
            Views.GROUNDING.open(this.getWorld(), player);
        }
    }
}
