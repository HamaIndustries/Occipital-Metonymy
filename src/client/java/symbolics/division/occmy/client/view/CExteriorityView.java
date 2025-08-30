package symbolics.division.occmy.client.view;

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

public class CExteriorityView {
    private static boolean complex = false;
    private static Vec3d anchor = Vec3d.ZERO;
    private static float pitch = 0;
    private static float yaw = 0;

    public static void reset() {
        complex = false;
    }

    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 40);

        if (!complex) {
            for (Entity e : ((ClientWorld) world).getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$rig();
                }
            }
            complex = true;
            anchor = player.getPos();
            pitch = player.getPitch();
            yaw = player.getYaw();
        } else {
            for (Entity e : ((ClientWorld) world).getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$cut();
                }
            }
            complex = false;
            player.setPosition(anchor);
            player.setYaw(yaw);
            player.setPitch(pitch);
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
}
