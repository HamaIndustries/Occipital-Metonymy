package symbolics.division.occmy.client.view;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.ent.IStringedEntity;

public class CExteriorityView {
    private static boolean complex = false;

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
        } else {
            for (Entity e : ((ClientWorld) world).getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$cut();
                }
            }
            complex = false;
        }
    }

    public boolean active() {
        return complex;
    }
}
