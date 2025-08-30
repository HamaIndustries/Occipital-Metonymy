package symbolics.division.occmy.view;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.net.S2CStabilizingPayload;

public class GroundingView extends AbstractView<View.Context<World, PlayerEntity>> {
    @Override
    public void open(World world, PlayerEntity user) {
        if (world.isClient) {
            callback().accept(View.Context.of(world, user));
        } else {
            resetAll(user);
        }
    }

    @Override
    public void reset(PlayerEntity user) {

    }

    public static void resetAll(PlayerEntity player) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, new S2CStabilizingPayload());
        for (View view : Views.all()) view.reset(player);
    }
}
