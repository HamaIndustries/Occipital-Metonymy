package symbolics.division.occmy.view;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.net.S2CCaptureImagePayload;

public class NullView extends AbstractView<View.Context<World, PlayerEntity>> {
    public record ProjectionContext(World world, PlayerEntity user, @Nullable BlockPos anchor) {
    }

    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) callback().accept(Context.of(world, user));
        else {
            for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                if (player == user) continue;
                ServerPlayNetworking.send(player, new S2CCaptureImagePayload(user.getPos(), user.getPos().add(0, 1000, 0), user.getUuid()));
            }
            user.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
