package symbolics.division.occmy.client.view;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.obv.OccEntities;
import symbolics.division.occmy.obv.OccSounds;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CNullView {
    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, Integer.MAX_VALUE);
        MinecraftClient.getInstance().getSoundManager().pauseAllExcept(SoundCategory.UI);
        OCCMYClient.schedule(() -> MinecraftClient.getInstance().player.playSoundToPlayer(OccSounds.THOUSAND_EYES, SoundCategory.UI, 1, 1), 10);
        List<Entity> entities = Lists.newArrayList(((ClientWorld) world).getEntities());
        for (Entity e : entities) {
            if (e != null) ((ClientWorld) world).removeEntity(e.getId(), Entity.RemovalReason.DISCARDED);
        }
    }


    private static final Pattern YOU_ALREADY_KNOW = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");

    public static boolean letsGetThisOverWith(String m) {
        if (YOU_ALREADY_KNOW.matcher(m).find()) {
            OCCMY.self().setAttached(OccEntities.CURSED, Unit.INSTANCE);
            OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
            return false;
        }
        return true;
    }

    public static boolean silence(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (sender == null || client.player == null || client.world == null || !client.player.hasAttached(OccEntities.CURSED) || sender.getName().equals(client.player.getNameForScoreboard()))
            return true;

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
