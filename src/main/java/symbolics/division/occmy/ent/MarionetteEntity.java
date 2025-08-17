package symbolics.division.occmy.ent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.obv.OccSounds;

public class MarionetteEntity extends HostileEntity {
    public static Runnable signal = () -> {
        throw new NotImplementedException();
    };

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

        @Override
        protected TargetPredicate getAndUpdateTargetPredicate() {
            return super.getAndUpdateTargetPredicate();
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
                target.playSound(SoundEvents.ENTITY_PLAYER_HURT);
                ((MarionetteEntity) this.mob).hit(target);
                target.setHealth(target.getHealth() - 3);
                if (target.getHealth() <= 0) {
                    signal.run();
                }
            }
        }

        protected boolean canAttack(LivingEntity target) {
//            return this.isCooledDown();
            return this.isCooledDown() && this.mob.isInAttackRange(target);
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = OCCMY.self();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
//            } else if (!this.pauseWhenMobIdle) {
//                return !this.mob.getNavigation().isIdle();
            } else {
                return !this.mob.isInPositionTargetRange(livingEntity.getBlockPos())
                        ? false
                        : !(livingEntity instanceof PlayerEntity playerEntity && (playerEntity.isSpectator()));
            }
        }
    }

    public void hit(LivingEntity target) {
        knockback(target);
    }

    private PlayerEntity control;

    public MarionetteEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.initGoals();
    }

    public void setControl(PlayerEntity player) {
        this.control = player;
    }

    @Override
    protected void initGoals() {
        if (this.getWorld().isClient) {
            this.targetSelector.add(1, new MarionetteTargetGoal<>(this, PlayerEntity.class, false));
            this.goalSelector.add(2, new MarionetteAttackGoal(this, 1, true));
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

    int age = 0;

    @Override
    public void tick() {
        super.tick();
        if (this.control != null) {
            Vec3d p = this.getPos();
            control.updateTrackedPositionAndAngles(p, this.bodyYaw, this.getPitch());
            control.setHeadYaw(this.headYaw);

            age--;
            if (age <= 0) {
                age = 210;

                float distance = this.random.nextTriangular(1, 0.2f);
                if (this.getTarget() != null) {
                    distance = (float) Math.max(1 - (this.getTarget().distanceTo(this) / 20) + 0.5, 0.5);
                }
                getWorld().playSoundFromEntityClient(this, OccSounds.MARIONETTE, SoundCategory.HOSTILE, 0.8f, distance);
            }
        }
    }
}
