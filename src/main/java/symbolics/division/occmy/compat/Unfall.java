package symbolics.division.occmy.compat;

import dev.sisby.dominoes.DominoBlock;
import dev.sisby.dominoes.mixin.FallingBlockEntityAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import symbolics.division.occmy.OCCMY;

import java.util.LinkedList;
import java.util.Queue;

public class Unfall {
    public static RegistryKey<Item> MYSTERIOUS_DOMINO_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM, OCCMY.id("antimonic_domino"));
    public static RegistryKey<Block> MYSTERIOUS_DOMINO_BLOCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, OCCMY.id("antimonic_domino"));
    public static Block MYSTERIOUS_DOMINO_BLOCK;
    public static Item MYSTERIOUS_DOMINO;

    public static void setup() {
        ServerTickEvents.START_SERVER_TICK.register(Unfall::tick);
        MYSTERIOUS_DOMINO_BLOCK = Registry.register(Registries.BLOCK, MYSTERIOUS_DOMINO_BLOCK_KEY.getValue(),
                new AntimonicDominoBlock(
                        AbstractBlock.Settings.create()
                                .registryKey(MYSTERIOUS_DOMINO_BLOCK_KEY)
                                .mapColor(MapColor.CLEAR)
                                .breakInstantly()
                                .pistonBehavior(PistonBehavior.DESTROY)
                                .sounds(BlockSoundGroup.STONE)
                )
        );

        MYSTERIOUS_DOMINO = Registry.register(Registries.ITEM, MYSTERIOUS_DOMINO_ITEM_KEY.getValue(),
                new BlockItem(MYSTERIOUS_DOMINO_BLOCK, new Item.Settings().registryKey(MYSTERIOUS_DOMINO_ITEM_KEY))
        );

        ItemGroupEvents.modifyEntriesEvent(
                RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("dominoes", "items"))
        ).register(entries -> entries.add(MYSTERIOUS_DOMINO));
    }

    public static Queue<Pair<RegistryKey<World>, BlockPos>> positions = new LinkedList<>();

    public static void tick(MinecraftServer server) {
        Queue<Pair<RegistryKey<World>, BlockPos>> newPositions = new LinkedList<>();
        for (Pair<RegistryKey<World>, BlockPos> pair : positions) {
            World world = server.getWorld(pair.getLeft());
            BlockPos pos = pair.getRight();
            if (world == null) continue;
            var key = world.getRegistryKey();
            if (world.isPosLoaded(pos) && unFall(world, pos)) {
                for (Direction dir : Direction.values()) {
                    if (!world.getBlockState(pos.offset(dir)).isOf(MYSTERIOUS_DOMINO_BLOCK))
                        newPositions.add(new Pair<>(key, pos.offset(dir)));
                }
            }
        }
        positions = newPositions;
    }

    public static void unf_all(World world, BlockPos pos) {
        if (world.isClient) return;
        positions.add(new Pair<>(world.getRegistryKey(), pos));
    }

    private static boolean unFall(World world, BlockPos pos) {
        if (world.isClient) return false;
        BlockState state = world.getBlockState(pos);
        if (state.isOf(MYSTERIOUS_DOMINO_BLOCK)) return true;
        if (state.getProperties().contains(DominoBlock.COLLAPSED) && state.get(DominoBlock.COLLAPSED) != DominoBlock.Collapsed.NONE) {
            world.setBlockState(pos, (BlockState) ((BlockState) state.with(DominoBlock.COLLAPSED, DominoBlock.Collapsed.NONE)).with(DominoBlock.COLLAPSING, false));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS);
            return true;
        }
        return false;
    }

    private static class AntimonicDominoBlock extends DominoBlock {
        public AntimonicDominoBlock(Settings settings) {
            super(settings);
        }

        @Override
        protected void collapse(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean forwards, boolean initial) {
            if (state.get(COLLAPSED) == DominoBlock.Collapsed.NONE) {
                Shape shape = state.get(SHAPE);
                if (DominoBlock.Shape.STACKS.contains(shape)) {
                    BlockPos ahead = pos.offset(shape.connections().get(forwards ? 1 : 0));
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntityAccessor.invokeConstructor(world, (double) ahead.getX() + (double) 0.5F, (double) ahead.getY(), (double) ahead.getZ() + (double) 0.5F, (BlockState) ((BlockState) ((BlockState) state.with(SHAPE, (Shape) DominoBlock.Shape.STRAIGHTS.get(DominoBlock.Shape.STACKS.indexOf(shape)))).with(COLLAPSING, true)).with(COLLAPSED, forwards ? DominoBlock.Collapsed.FORWARDS : DominoBlock.Collapsed.BACKWARDS));
                    world.spawnEntity(fallingBlockEntity);
                }

                world.playSound(player, pos, initial ? SoundEvents.BLOCK_STONE_PLACE : SoundEvents.BLOCK_STONE_FALL, SoundCategory.BLOCKS);
                world.setBlockState(pos, ((state.with(COLLAPSING, false)).with(COLLAPSED, forwards ? DominoBlock.Collapsed.FORWARDS : DominoBlock.Collapsed.BACKWARDS)).with(SHAPE, DominoBlock.Shape.STACKS.contains(shape) ? (Shape) DominoBlock.Shape.STRAIGHTS.get(DominoBlock.Shape.STACKS.indexOf(shape)) : shape), 2, 0);
                world.scheduleBlockTick(pos, state.getBlock(), initial ? 5 : 2);
                Unfall.unf_all(world, pos);
            }

        }
    }
}
