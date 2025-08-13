package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.obv.OccEntities;

public class CTreacherousView {

    public static void open(World world, PlayerEntity player) {
        if (!player.hasAttached(OccEntities.BETRAYAL_LOCUS)) OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
    }

    private static boolean wasActive = false;

    public static boolean active() {
        PlayerEntity player = OCCMYClient.player();
        if (player == null) return false;
        if (player.hasAttached(OccEntities.BETRAYAL_LOCUS)) {
            wasActive = true;
            return true;
        } else {
            if (wasActive) {
                OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            }
            wasActive = false;
            return false;
        }
    }

    public static void reset() {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.removeAttached(OccEntities.BETRAYAL_LOCUS);
            wasActive = false;
        }
    }
}
