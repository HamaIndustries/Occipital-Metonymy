package symbolics.division.occmy.client.view;

import dev.doublekekse.area_lib.data.AreaClientData;
import dev.doublekekse.area_lib.data.AreaSavedData;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.ent.IStringedEntity;
import symbolics.division.occmy.compat.FightClubAreaComponent;
import symbolics.division.occmy.state.Gestalt;

public class CExteriorityView {
    private static boolean complex = false;
    private static Vec3d anchor = Vec3d.ZERO;
    private static float pitch = 0;
    private static float yaw = 0;

    private static Gestalt SAFETY = new Gestalt() {
        @Override
        public void tick() {
            super.tick();
            PlayerEntity subject = OCCMYClient.player();
            World world = OCCMYClient.world();
            if (active() && subject != null && world != null && subject.squaredDistanceTo(anchor) > (128 * 128)) {
                open(world, subject);
            }
        }
    };

    public static void reset() {
        ClientWorld world = OCCMYClient.world();
        if (world != null) {
            for (Entity e : world.getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$cut();
                }
            }
        }

        if (complex) {
            PlayerEntity player = OCCMYClient.player();
            if (player != null && !player.isRemoved()) {
                player.setPosition(anchor);
                player.setYaw(yaw);
                player.setPitch(pitch);
            }
        }
        SAFETY.disable();
        complex = false;
    }

    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 40);

        if (!complex) {
            if (fightClub(player)) {
                for (Entity e : ((ClientWorld) world).getEntities()) {
                    if (e instanceof IStringedEntity stringed) {
                        stringed.occmy$rig();
                    }
                }
            }
            complex = true;
            anchor = player.getPos();
            pitch = player.getPitch();
            yaw = player.getYaw();
            OCCMYClient.AFFAIRS.enable(SAFETY);
        } else {
            reset();
        }
    }

    public static Vec3d getAnchor() {
        return anchor;
    }

    public static boolean active() {
        return complex;
    }

    public static float getPitch() {
        return pitch;
    }

    public static float getYaw() {
        return yaw;
    }

    @FunctionalInterface
    public interface RenderEntity {
        void call(Entity entity, double cameraX, double cameraY, double cameraZ, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    }

    public static boolean proxying = false;

    public static void bwo(RenderEntity renderEntity,
                           MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, PlayerEntity player) {
        Vec3d p = camera.getPos().add(player.getPos().subtract(CExteriorityView.getAnchor()));
        proxying = true;
        renderEntity.call(OCCMY.self(), p.x, p.y, p.z, 0f, matrices, vertexConsumers);
        proxying = false;
    }

    private static boolean fightClub(PlayerEntity player) {
        AreaSavedData data = AreaClientData.getClientLevelData();
        if (player == null || data == null) return true;
        return data.findTrackedAreasContaining(player).stream().noneMatch(
                a -> a.has(FightClubAreaComponent.TYPE)
        );
    }
}
