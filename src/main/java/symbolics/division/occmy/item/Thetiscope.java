package symbolics.division.occmy.item;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccComponents;
import symbolics.division.occmy.obv.OccSounds;

import java.util.List;

public class Thetiscope extends BlockItem {
    public Thetiscope(Settings settings) {
        super(OccBloccs.THETISCOPE, settings);
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
        ContainerComponent container = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        if (clickType == ClickType.RIGHT) {
            boolean otherValid = otherStack.isEmpty() || otherStack.get(OccComponents.VIEW) != null;
            if (otherValid) {
                cursorStackReference.set(container.copyFirstStack());
                stack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of(otherStack)));
                slot.setStack(stack);
                player.playSound(OccSounds.SHUTTER, 0.8F, 1.6F + player.getWorld().getRandom().nextFloat() * 0.4F);
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack scope = user.getStackInHand(hand);
        ItemStack held = scope.getComponents().get(DataComponentTypes.CONTAINER).copyFirstStack();
        if (held.getComponents().contains(OccComponents.VIEW)) {
            held.get(OccComponents.VIEW).open(world, user);
        }

        return ActionResult.FAIL;
    }

    private boolean diskLike(ItemStack stack) {
        return stack.getComponents().contains(OccComponents.VIEW);
    }
}
