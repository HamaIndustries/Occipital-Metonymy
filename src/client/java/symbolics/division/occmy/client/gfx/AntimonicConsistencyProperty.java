package symbolics.division.occmy.client.gfx;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.client.view.CAntimonicView;

public class AntimonicConsistencyProperty implements BooleanProperty {
    public static final MapCodec<AntimonicConsistencyProperty> CODEC = MapCodec.unit(new AntimonicConsistencyProperty());

    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return CAntimonicView.solidifyParadox();
    }

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return CODEC;
    }
}
