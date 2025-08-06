package symbolics.division.occmy.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class InversionView extends AbstractView<View.Context<World, PlayerEntity>> {
    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) callback().accept(View.Context.of(world, user));
    }
}
