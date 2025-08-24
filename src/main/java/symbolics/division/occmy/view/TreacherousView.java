package symbolics.division.occmy.view;

import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TreacherousView extends AbstractView<View.Context<World, PlayerEntity>> {

    public static final int BETRAYAL_DISTANCE = 15;

    private static final HashMap<UUID, List<BlockPos>> updates = new HashMap<>();

    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) callback().accept(View.Context.of(world, user));
        else if (active(user)) {
            forgive((ServerPlayerEntity) user);
        } else {
            depaint(world, (ServerPlayerEntity) user);
        }
    }

    public static void depaint(World world, ServerPlayerEntity player) {
        player.setAttached(OccEntities.BETRAYAL_LOCUS, player.getPos());
        for (PaintingEntity paint : world.getEntitiesByClass(PaintingEntity.class, player.getBoundingBox().expand(BETRAYAL_DISTANCE), p -> true)) {
            Vec3d dir = paint.getRotationVector().multiply(-0.5);
            if (dir.dotProduct(player.getRotationVector()) <= 0) continue;
            BlockPos.stream(paint.getBoundingBox().contract(0.5).stretch(dir.x, dir.y, dir.z)).forEach(
                    p -> {
                        BlockPos bp = new BlockPos(p);
                        updates.computeIfAbsent(player.getUuid(), id -> new ArrayList<>()).add(bp);
                        player.networkHandler.sendPacket(new BlockUpdateS2CPacket(bp, Blocks.AIR.getDefaultState()));
                    }
            );
        }
    }


    public static boolean active(PlayerEntity player) {
        return !player.getWorld().isClient() && player.hasAttached(OccEntities.BETRAYAL_LOCUS);
    }

    private static void forgive(ServerPlayerEntity player) {
        player.removeAttached(OccEntities.BETRAYAL_LOCUS);
        if (updates.containsKey(player.getUuid())) {
            for (BlockPos pos : updates.get(player.getUuid())) {
                player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, player.getWorld().getBlockState(pos)));
            }
        }
    }

    public static void tick(ServerPlayerEntity player) {
        Vec3d anchor = player.getAttached(OccEntities.BETRAYAL_LOCUS);
        if (anchor != null && player.getPos().distanceTo(anchor) > BETRAYAL_DISTANCE * 3) {
            forgive(player);
        }
    }
}
