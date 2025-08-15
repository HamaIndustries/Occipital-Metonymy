package symbolics.division.occmy.client.view;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.ent.IStringedEntity;

import java.util.regex.Pattern;

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

    public static boolean active() {
        return complex;
    }

    private static final Pattern YOU_ALREADY_KNOW = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");

    public static boolean letsGetThisOverWith(String m) {
        if (YOU_ALREADY_KNOW.matcher(m).find()) {
            if (!active()) {
                open(OCCMYClient.world(), OCCMYClient.player());
            }
            return false;
        }
        return true;
    }
}
