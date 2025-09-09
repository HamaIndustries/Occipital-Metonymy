package symbolics.division.occmy.compat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.doublekekse.area_lib.Area;
import dev.doublekekse.area_lib.command.argument.AreaArgument;
import dev.doublekekse.area_lib.component.AreaDataComponent;
import dev.doublekekse.area_lib.component.AreaDataComponentType;
import dev.doublekekse.area_lib.data.AreaSavedData;
import dev.doublekekse.area_lib.registry.AreaDataComponentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.PermissionLevelSource;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import symbolics.division.occmy.OCCMY;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class ProjectionRestrictionAreaComponent implements AreaDataComponent {
    public static final AreaDataComponentType<ProjectionRestrictionAreaComponent> TYPE =
            AreaDataComponentTypeRegistry.registerTracking(OCCMY.id("time_zone"), ProjectionRestrictionAreaComponent::new);

    private record Data(BlockState state) {
        public static Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockState.CODEC.fieldOf("ticks").forGetter(Data::state)
        ).apply(instance, Data::new));

        public static Data NONE = new Data(Blocks.AIR.getDefaultState());
    }

    private Data data = Data.NONE;

    public ProjectionRestrictionAreaComponent() {
    }

    public ProjectionRestrictionAreaComponent(BlockState state) {
        this.data = new Data(state);
    }

    @Override
    public void load(AreaSavedData areaSavedData, NbtCompound nbtCompound) {
        this.data = Data.CODEC.decode(NbtOps.INSTANCE, nbtCompound.get("projection_restriction")).mapOrElse(
                Pair::getFirst,
                error -> {
                    OCCMY.LOGGER.error("failed to deserialize area component: {}", error);
                    return Data.NONE;
                }
        );
    }


    @Override
    public NbtCompound save() {
        NbtCompound tag = new NbtCompound();
        tag.put("projection_restriction", Data.CODEC.encode(this.data, NbtOps.INSTANCE, null).getOrThrow());
        return tag;
    }

    public BlockState restriction() {
        return data.state;
    }

    public static int restrict(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Area area = AreaArgument.getArea(context, "area");
        BlockState state = BlockStateArgumentType.getBlockState(context, "block").getBlockState();
        area.put(context.getSource().getServer(), TYPE, new ProjectionRestrictionAreaComponent(state));
        context.getSource().sendFeedback(() -> Text.translatable("occmy.command.restrict.success"), true);
        return Command.SINGLE_SUCCESS;
    }

    public static int clear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Area area = AreaArgument.getArea(context, "area");
        area.remove(context.getSource().getServer(), TYPE);
        context.getSource().sendFeedback(() -> Text.translatable("occmy.command.clear.success"), true);
        return Command.SINGLE_SUCCESS;
    }

    // its called casting because you have to
    public static void register() {
        CommandRegistrationCallback.EVENT.register(ProjectionRestrictionAreaComponent::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
        LiteralArgumentBuilder<ServerCommandSource> root = literal("occmy");
        LiteralArgumentBuilder<ServerCommandSource> sub = literal("area");
        RequiredArgumentBuilder<ServerCommandSource, ?> areaArg = argument("area", AreaArgument.area());
        RequiredArgumentBuilder<ServerCommandSource, ?> blockArg = CommandManager.argument("block", BlockStateArgumentType.blockState(access));
        ArgumentBuilder<ServerCommandSource, ?> restrictSubCommand = areaArg.
                then(blockArg.
                        executes(ProjectionRestrictionAreaComponent::restrict));
        ArgumentBuilder<ServerCommandSource, ?> clearSubCommand = areaArg.executes(ProjectionRestrictionAreaComponent::clear);

        dispatcher.register(root
                .then(sub
                        .then(LiteralArgumentBuilder.<ServerCommandSource>literal("restrict").then(restrictSubCommand))
                        .then(LiteralArgumentBuilder.<ServerCommandSource>literal("clear").then(clearSubCommand))
                        .requires(PermissionLevelSource::hasElevatedPermissions))
        );
    }
}
