package symbolics.division.occmy.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccEntities;

public class AntimonicView extends AbstractView<View.Context<World, PlayerEntity>> {
    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) callback().accept(View.Context.of(world, user));
        else if (active(user)) {
            reset(user);
        } else {
            user.setAttached(OccEntities.CONTRADICTORY, Unit.INSTANCE);
        }
    }

    public static boolean active(PlayerEntity user) {
        return !user.getWorld().isClient() && user.hasAttached(OccEntities.CONTRADICTORY);
    }

    @Override
    public void reset(PlayerEntity user) {
        user.removeAttached(OccEntities.CONTRADICTORY);
    }
}
