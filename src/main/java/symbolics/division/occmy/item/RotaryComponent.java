package symbolics.division.occmy.item;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;

/*
    Largely copied from Glowcase (CC0) shout out to Sisby Folk :thumbsup::thumbsup::thumbsup:
*/

public record RotaryComponent(ImmutableList<ItemStack> disks, int index) {
    public static final Codec<RotaryComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(ItemStack.CODEC).fieldOf("disks").forGetter(RotaryComponent::disks),
            Codec.INT.fieldOf("selected").forGetter(RotaryComponent::index)
    ).apply(instance, (c, s) -> new RotaryComponent(ImmutableList.copyOf(c), s)));
    public static final ComponentType<RotaryComponent> TYPE = ComponentType.<RotaryComponent>builder().codec(CODEC).packetCodec(PacketCodecs.registryCodec(CODEC)).build();

    public RotaryComponent() {
        this(ImmutableList.of(), 0);
    }

    public RotaryComponent add(ItemStack stack) {
        return new RotaryComponent(ImmutableList.<ItemStack>builder().addAll(disks).add(stack).build(), index);
    }

    public RotaryComponent remove(int at) {
        if (disks.isEmpty()) return this;
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        for (int i = 0; i < disks.size(); i++) {
            if (i != at) builder.add(disks.get(i));
        }
        return new RotaryComponent(builder.build(), index == disks.size() - 1 ? 0 : index);
    }

    public RotaryComponent forward() {
        int i = index + 1;
        if (i >= disks.size()) i = 0;
        return new RotaryComponent(disks, i);
    }

    public RotaryComponent backward() {
        int i = index - 1;
        if (i <= -1) i = disks.size() - 1;
        return new RotaryComponent(disks, i);
    }

    public ItemStack getSelected() {
        return disks.isEmpty() ? ItemStack.EMPTY : disks.get(index % disks.size());
    }

    public ItemStack getTop() {
        return disks.isEmpty() ? ItemStack.EMPTY : disks.getFirst();
    }
}
