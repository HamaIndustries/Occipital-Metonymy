package symbolics.division.occmy.ent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import symbolics.division.occmy.OCCMY;

public class MarionetteEntity extends HostileEntity {

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 50)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.23F)
                .add(EntityAttributes.ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.ARMOR, 2.0);
    }

    public static class MarionetteTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public MarionetteTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }

        @Override
        protected void findClosestTarget() {
            this.targetEntity = OCCMY.self();
        }
    }

    public static class MarionetteAttackGoal extends MeleeAttackGoal {
        public MarionetteAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                target.clientDamage(target.getWorld().getDamageSources().generic());
                target.setHealth(target.getHealth() - 3);
                if (target.getHealth() <= 0) {
                }
            }
        }

        protected boolean canAttack(LivingEntity target) {
            return this.isCooledDown() && this.mob.isInAttackRange(target);
        }
    }


    public MarionetteEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.initGoals();
    }


    @Override
    protected void initGoals() {
        if (this.getWorld().isClient) {
            this.targetSelector.add(1, new MarionetteTargetGoal<>(this, PlayerEntity.class, false));
            this.goalSelector.add(2, new MarionetteAttackGoal(this, 1, false));
        } else {
            this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 50));
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 0.2, false));
        }
    }


    @Override
    public boolean isLogicalSideForUpdatingMovement() {
        return this.getWorld().isClient();
    }

    @Override
    protected void tickMovementInput() {
        super.tickMovementInput();
        getVisibilityCache().clear();
        int i = this.age + this.getId();
        if (i % 2 != 0 && this.age > 1) {
            this.targetSelector.tickGoals(false);
            this.goalSelector.tickGoals(false);
        } else {
            this.targetSelector.tick();
            this.goalSelector.tick();
        }

        this.navigation.tick();
        this.moveControl.tick();
        this.lookControl.tick();
        this.jumpControl.tick();
    }


    @Override
    public void tick() {
        super.tick();
        if (this.age % 20 == 0) {
            OCCMY.LOGGER.info("ghosty at {}", this.getPos().toString());
        }
    }
}
