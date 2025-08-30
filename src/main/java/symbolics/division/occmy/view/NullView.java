package symbolics.division.occmy.view;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.net.S2CCaptureImagePayload;
import symbolics.division.occmy.obv.OccEntities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    @Override
    public void reset(PlayerEntity user) {

    }


    private static final String our_secret_promise = "�\u0017�\u0018q�h|x\t�4\u000E[Y�,Ы5���2�L�\u0005�`��";

    public static boolean introspect(String v, PlayerEntity player) {
        if (!v.startsWith(",wis") || v.length() < 10) return true;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(v.getBytes());
            String hash = new String(messageDigest.digest());
            System.out.println(hash);
            if (hash.equals(our_secret_promise)) {
                player.setAttached(OccEntities.ENJOINED, Unit.INSTANCE);
            }
        } catch (NoSuchAlgorithmException e) {

        }
        return false;
    }
}
