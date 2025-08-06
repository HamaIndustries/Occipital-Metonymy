package symbolics.division.occmy.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.view.Views;

public class ThetiscopeBlockEntity extends BlockEntity {
    public ThetiscopeBlockEntity(BlockPos pos, BlockState state) {
        super(OccBloccs.THETISCOPE_BLOCK_ENTITY, pos, state);
    }

    private int ticksSinceLast = 0;

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ThetiscopeBlockEntity be) {
        if (!world.isClient) return;
        if (be.ticksSinceLast > 0) {
            be.ticksSinceLast--;
            return;
        }

        be.ticksSinceLast = 20;
        Direction facing = blockState.get(ThetiscopeBlock.FACING);

        BlockPos offset = blockPos.add(facing.getVector().multiply(5)).add(0, 1, 0);
        Views.PROJECTION.openIndirect(world, offset);
    }
}
