package symbolics.division.occmy.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.net.S2CAnsibleQuale;
import symbolics.division.occmy.obv.OccBloccs;
import symbolics.division.occmy.obv.OccComponents;
import symbolics.division.occmy.obv.OccSounds;
import symbolics.division.occmy.view.View;

public class FlopsterDriveModelBSiltware extends BlockEntity {
    private ItemStack disk = ItemStack.EMPTY;

    public FlopsterDriveModelBSiltware(BlockPos pos, BlockState state) {
        super(OccBloccs.FLOPSTER_DRIVE_SILTWARE, pos, state);
    }

    public ItemStack insert(ItemStack disk) {
        ItemStack old = this.disk;
        if (disk.isEmpty()) {
            this.disk = ItemStack.EMPTY;
            if (!old.isEmpty()) {
                this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), OccSounds.SHUTTER, SoundCategory.BLOCKS, 1, 1);
            }
            return old;
        }
        if (disk.get(OccComponents.VIEW) == null) return disk;
        this.disk = disk;
        Vec3d pos = this.getPos().toCenterPos();
        this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), OccSounds.SHUTTER, SoundCategory.BLOCKS, 1, 1);
        this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), OccSounds.STARTUP, SoundCategory.BLOCKS, 1, 1);
        return old;
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.put("disk", ItemStack.CODEC, disk);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        disk = view.read("disk", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    private boolean active = false;

    public void trigger(boolean on) {
        if (on && !active) {
            active = true;
            View program = disk.get(OccComponents.VIEW);
            if (program == null) return;
            for (PlayerEntity player : getWorld().getEntitiesByClass(PlayerEntity.class, new Box(getPos()).expand(3), p -> true)) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, new S2CAnsibleQuale(program));
                program.open(getWorld(), player);
            }
        } else {
            active = on;
        }
    }


}
