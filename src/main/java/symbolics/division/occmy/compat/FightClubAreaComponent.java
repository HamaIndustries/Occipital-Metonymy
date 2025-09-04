package symbolics.division.occmy.compat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.doublekekse.area_lib.Area;
import dev.doublekekse.area_lib.command.argument.AreaArgument;
import dev.doublekekse.area_lib.component.AreaDataComponent;
import dev.doublekekse.area_lib.component.AreaDataComponentType;
import dev.doublekekse.area_lib.data.AreaSavedData;
import dev.doublekekse.area_lib.registry.AreaDataComponentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.PermissionLevelSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import symbolics.division.occmy.OCCMY;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class FightClubAreaComponent implements AreaDataComponent {
    public static final AreaDataComponentType<FightClubAreaComponent> TYPE =
            AreaDataComponentTypeRegistry.registerTracking(OCCMY.id("fight_club"), FightClubAreaComponent::new);

    public FightClubAreaComponent() {
    }

    @Override
    public void load(AreaSavedData areaSavedData, NbtCompound nbtCompound) {
    }


    @Override
    public NbtCompound save() {
        return new NbtCompound();
    }

    public static int funPolice(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Area area = AreaArgument.getArea(context, "area");
        boolean fun = BoolArgumentType.getBool(context, "allow");

        if (fun) {
            area.remove(context.getSource().getServer(), TYPE);
        } else {
            area.put(context.getSource().getServer(), TYPE, new FightClubAreaComponent());
        }

        context.getSource().sendFeedback(() -> Text.translatable("occmy.command.fightclub.success"), true);
        return Command.SINGLE_SUCCESS;
    }

    // its called casting because you have to
    public static void register() {
        CommandRegistrationCallback.EVENT.register(FightClubAreaComponent::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
        LiteralArgumentBuilder<ServerCommandSource> root = literal("occmy");
        RequiredArgumentBuilder<ServerCommandSource, ?> areaArg = argument("area", AreaArgument.area());
        RequiredArgumentBuilder<ServerCommandSource, ?> allowArg = CommandManager.argument("allow", BoolArgumentType.bool());

        dispatcher.register(root
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("fightclub")
                        .then(areaArg
                                .then(allowArg
                                        .executes(FightClubAreaComponent::funPolice))))
                .requires(PermissionLevelSource::hasElevatedPermissions)
        );
    }
}
