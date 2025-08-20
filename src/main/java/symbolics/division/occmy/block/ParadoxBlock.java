package symbolics.division.occmy.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TransparentBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import symbolics.division.occmy.obv.OccItems;

import java.util.function.Function;
import java.util.function.Supplier;

// its called that because this block does not exist!
public class ParadoxBlock extends TransparentBlock {

    public static Function<Settings, ParadoxBlock> of(boolean invert) {
        return settings -> new ParadoxBlock(settings, invert);
    }

    private boolean invert;

    public ParadoxBlock(Settings settings, boolean invert) {
        super(settings);
        this.invert = invert;
    }

    public static Supplier<Boolean> solid = () -> false;

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!solid.get() ^ invert) {
            return VoxelShapes.fullCube();
        }
        return VoxelShapes.empty();
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return context.isHolding(OccItems.BLOCK_COROLLARY) || context.isHolding(OccItems.BLOCK_PARADOX) ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    public static Function<BlockState, BlockRenderType> cb = null;

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        if (cb != null) return cb.apply(state);
        return super.getRenderType(state);
    }

    @Override
    protected float getMaxHorizontalModelOffset() {
        return super.getMaxHorizontalModelOffset();
    }
}
