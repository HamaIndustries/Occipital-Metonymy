package symbolics.division.occmy.obv;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class OccCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(OccCommands::registerCommands);

    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
        LiteralArgumentBuilder<ServerCommandSource> root = literal("occmy");
        LiteralArgumentBuilder<ServerCommandSource> clientArg = literal("client");
        RequiredArgumentBuilder<ServerCommandSource, ?> allowArg = CommandManager.argument("allow", BoolArgumentType.bool());

        dispatcher.register(root
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("fightclub")
                        .then(clientArg
                                .then(allowArg
                                        .executes(OccCommands::optOutOfFightClub))))


        );
    }

    public static int optOutOfFightClub(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        boolean fun = BoolArgumentType.getBool(context, "allow");
        if (context.getSource().getEntity() == null) {
            return 0;
        }
        if (fun) {
            context.getSource().getEntity().removeAttached(OccEntities.IMMUNE);
        } else {
            context.getSource().getEntity().setAttached(OccEntities.IMMUNE, Unit.INSTANCE);
        }

        context.getSource().sendFeedback(() -> Text.translatable("occmy.command.fightclub.client.success", Boolean.toString(fun)), true);
        return Command.SINGLE_SUCCESS;
    }
}
