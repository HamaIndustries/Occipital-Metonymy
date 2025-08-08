package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;

// HOLE LOGIC
public class CAntimonicView {
    public static void reset() {
        funny = false;
    }

    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
        OCCMYClient.nextTick(() -> {
            funny = !funny;
            MinecraftClient client = MinecraftClient.getInstance();
            client.worldRenderer.reload();
        });
    }

    private static boolean funny = false;

    public static boolean solidifyParadox() {
        return funny;
    }
}
