package symbolics.division.occmy.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;
import symbolics.division.occmy.OCCMY;

public class Views {
    private static final BiMap<Identifier, View> entries = HashBiMap.create();
    public static final Codecs.IdMapper<Identifier, ? extends View> MAPPER = new Codecs.IdMapper<>();
    public static final Codec<View> CODEC = Identifier.CODEC.flatXmap(
            id -> DataResult.success(entries.get(id)),
            view -> DataResult.success(entries.inverse().get(view))
    );
    public static final PacketCodec<ByteBuf, View> PACKET_CODEC = Identifier.PACKET_CODEC.xmap(entries::get, entries.inverse()::get);

    public static ProjectionView PROJECTION = register("projection", new ProjectionView());
    public static ExteriorityView EXTERIORITY = register("exteriority", new ExteriorityView());
    public static BestView BEST = register("but_if_you_close_your_eyes", new BestView());
    public static InversionView INVERSION = register("inversion", new InversionView());
    public static AntimonicView ANTIMONY = register("antimony", new AntimonicView());
    public static TreacherousView TREACHERY = register("treachery", new TreacherousView());
    public static NullView NULLITY = register("nullity", new NullView());

    private static <T extends View> T register(String id, T v) {
        entries.put(OCCMY.id(id), v);
        return v;
    }

    @Nullable
    public static View get(Identifier id) {
        return entries.get(id);
    }

    @Nullable
    public static Identifier getId(View v) {
        return entries.inverse().get(v);
    }
}
