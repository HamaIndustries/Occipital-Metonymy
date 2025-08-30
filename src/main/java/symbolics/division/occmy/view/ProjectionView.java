package symbolics.division.occmy.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectionView extends AbstractView<ProjectionView.ProjectionContext> {
    public record ProjectionContext(World world, PlayerEntity user, @Nullable BlockPos anchor) {
    }

    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) callback().accept(new ProjectionContext(world, user, null));
    }

    @Override
    public void reset(PlayerEntity user) {

    }

    public void openIndirect(World world, BlockPos anchor) {
        if (world.isClient) callback().accept(new ProjectionContext(world, null, anchor));
    }
}
