package symbolics.division.occmy.item;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import symbolics.division.occmy.obv.OccComponents;

import java.util.List;

public class Thetiscope extends Item {
    public Thetiscope(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack scope = user.getStackInHand(hand);
        ItemStack held = scope.getComponents().get(DataComponentTypes.CONTAINER).copyFirstStack();
        if (user.isSneaking()) {
            if (world.isClient) return ActionResult.SUCCESS;
            Hand off = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack disk = user.getStackInHand(off);
            if (disk.getComponents().contains(OccComponents.VIEW) || disk.isEmpty()) {
                user.setStackInHand(off, held);
                scope.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of(disk)));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        }

        if (held.getComponents().contains(OccComponents.VIEW)) {
            held.get(OccComponents.VIEW).open(world, user);
        }

        return ActionResult.FAIL;
    }
}
