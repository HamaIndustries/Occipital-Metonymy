package symbolics.division.occmy.client.view;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.client.ent.IStringedEntity;
import symbolics.division.occmy.obv.OccEntities;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

public class CExteriorityView {
    private static boolean complex = false;
    private static Vec3d anchor = Vec3d.ZERO;
    private static float pitch = 0;
    private static float yaw = 0;

    public static void reset() {
        complex = false;
    }

    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 40);

        if (!complex) {
            for (Entity e : ((ClientWorld) world).getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$rig();
                }
            }
            complex = true;
            anchor = player.getPos();
            pitch = player.getPitch();
            yaw = player.getYaw();
        } else {
            for (Entity e : ((ClientWorld) world).getEntities()) {
                if (e instanceof IStringedEntity stringed) {
                    stringed.occmy$cut();
                }
            }
            complex = false;
            player.setPosition(anchor);
            player.setYaw(yaw);
            player.setPitch(pitch);
        }
    }

    public static boolean active() {
        return complex;
    }

    private static final Pattern YOU_ALREADY_KNOW = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");

    public static boolean letsGetThisOverWith(String m) {
        if (YOU_ALREADY_KNOW.matcher(m).find()) {
            if (!active()) {
//                open(OCCMYClient.world(), OCCMYClient.player());
                OCCMY.self().setAttached(OccEntities.CURSED, Unit.INSTANCE);
            }
            return false;
        }
        return true;
    }

    public static boolean silence(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || !client.player.hasAttached(OccEntities.CURSED) || sender.getName().equals(client.player.getNameForScoreboard()))
            return true;


//        message.getContent().visit()
        MutableText tex;

        Optional<String> name = message.visit(string -> {
            if (client.world.getPlayers().stream().anyMatch(p -> p.getNameForScoreboard().equals(string))) {
                var players = client.world.getPlayers();
                return Optional.of(players.get(client.world.getRandom().nextInt(players.size())).getNameForScoreboard());
            }
            return Optional.empty();
        });

        if (name.isEmpty() || name.get().isEmpty()) return true;

        StringBuilder builder = new StringBuilder();
        message.visit(string -> {
            builder.append(matchName(client.world, string) ? name.get() : string);
            return Optional.empty();
        });
        Text result = Text.literal(builder.toString());
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(result);
        return false;
    }

    private static boolean matchName(World world, String name) {
        return world.getPlayers().stream().anyMatch(p -> p.getNameForScoreboard().equals(name));
    }
}
