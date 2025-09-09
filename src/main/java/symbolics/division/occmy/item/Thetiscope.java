package symbolics.division.occmy.item;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccComponents;
import symbolics.division.occmy.obv.OccSounds;

import java.util.function.Consumer;

public class Thetiscope extends BlockItem {
    public String model;

    public Thetiscope(Settings settings) {
        super(OccBloccs.THETISCOPE, settings.component(OccComponents.ROTARY, new RotaryComponent()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            return place(new ItemPlacementContext(context));
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        RotaryComponent rotary = stack.get(OccComponents.ROTARY);
        if (rotary != null && clickType == ClickType.RIGHT) {
            boolean modified = false;
            if (otherStack.isEmpty()) {
                cursorStackReference.set(rotary.getTop());
                stack.set(OccComponents.ROTARY, rotary.remove(0));
                slot.setStack(stack);
                modified = true;
            } else if (otherStack.get(OccComponents.VIEW) != null) {
                cursorStackReference.set(ItemStack.EMPTY);
                stack.set(OccComponents.ROTARY, rotary.add(otherStack));
                slot.setStack(stack);
                modified = true;
            }

            if (modified) {
                player.playSound(OccSounds.SHUTTER, 0.8F, 1.6F + player.getWorld().getRandom().nextFloat() * 0.4F);
                return true;
            }
        }
        return false;
    }

    public static Runnable special = null;

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack held = getDisk(user.getStackInHand(hand));
        if (held.getComponents().contains(OccComponents.VIEW)) {
            held.get(OccComponents.VIEW).open(world, user);
            return ActionResult.SUCCESS;
        } else if (special != null) {
            special.run();
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public Text getName(ItemStack stack) {
        if (model == null)
            model = FabricLoader.getInstance().getModContainer("occmy").get().getMetadata().getVersion().getFriendlyString();

        MutableText name = MutableText.of(Text.translatable("item.occmy.thetiscope", model).getContent());

        ItemStack disk = getDisk(stack);
        if (!disk.isEmpty()) {
            name.append(" : ").append(disk.getName());
        }
        return name;
    }

    private static ItemStack getDisk(ItemStack stack) {
        RotaryComponent rotaryComponent = stack.get(OccComponents.ROTARY);
        if (rotaryComponent != null) {
            return rotaryComponent.getSelected();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        RotaryComponent rotary = stack.get(OccComponents.ROTARY);
        if (rotary != null && !rotary.disks().isEmpty()) {
            int index = rotary.index();
            for (int i = 0; i < rotary.disks().size(); i++) {
                textConsumer.accept(MutableText.of(Text.literal(index == i ? ">" : "").getContent()).append(rotary.disks().get(i).getName()));
            }
        }
    }
}
