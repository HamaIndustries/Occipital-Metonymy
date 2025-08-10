package symbolics.division.occmy.client.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;

public class CNullView {
    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, Integer.MAX_VALUE);
    }
}
