package symbolics.division.occmy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class FlopsterDriveModelB extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final MapCodec<FlopsterDriveModelB> CODEC = AbstractBlock.createCodec(FlopsterDriveModelB::new);

    public FlopsterDriveModelB(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(
                FACING, Direction.NORTH
        ));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends FlopsterDriveModelB> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FlopsterDriveModelBSiltware(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof FlopsterDriveModelBSiltware siltware) {
            player.setStackInHand(hand, siltware.insert(player.getStackInHand(hand)));
            return ActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof FlopsterDriveModelBSiltware siltware) {
            siltware.trigger(world.isReceivingRedstonePower(pos));
        }
    }
}
