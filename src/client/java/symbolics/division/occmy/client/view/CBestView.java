package symbolics.division.occmy.client.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;

public class CBestView {

    private static boolean shouldFixMinecraft;

    public static boolean fixMinecraft() {
        return shouldFixMinecraft;
    }

    public static void reset() {
        shouldFixMinecraft = false;
    }

    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
        OCCMYClient.nextTick(() -> {
            shouldFixMinecraft = !shouldFixMinecraft;
        });
    }
}
