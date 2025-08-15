package symbolics.division.occmy.obv;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.item.Thetiscope;
import symbolics.division.occmy.view.View;
import symbolics.division.occmy.view.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OccItems {
    public static void init() {
        Registry.register(Registries.ITEM_GROUP, OCCMY.id("item_group"), ITEM_GROUP);
    }

    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Item THETISCOPE = register("thetiscope", Thetiscope::new, new Item.Settings()
            .maxCount(1)
            .component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)
            .useCooldown(1)
    );

    public static final Item DISK_PROJECTION = disk("disk_projection", Views.PROJECTION);
    public static final Item DISK_EXTERIORITY = disk("disk_exteriority", Views.EXTERIORITY);
    public static final Item DISK_BEST = disk("disk_but_if_you_close_your_eyes", Views.BEST);
    public static final Item DISK_INVERSION = disk("disk_inversion", Views.INVERSION);
    public static final Item DISK_ANTIMONY = disk("disk_antimony", Views.ANTIMONY);
    public static final Item DISK_TREACHERY = disk("disk_treachery", Views.TREACHERY);
    public static final Item DISK_NULLITY = register("disk_nullity", Item::new, new Item.Settings().component(OccComponents.VIEW, Views.NULLITY).jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, OCCMY.id("thousand_eyes"))));
    public static final Item BLOCK_PARADOX = register("paradox", settings -> new BlockItem(OccBloccs.PARADOX, settings), new Item.Settings());
    public static final Item BLOCK_COROLLARY = register("corollary", settings -> new BlockItem(OccBloccs.COROLLARY, settings), new Item.Settings());
    public static final Item FLOPSTER_DRIVE_MODEL_B = register("flopster_drive_model_b", settings -> new BlockItem(OccBloccs.FLOPSTER_DRIVE_MODEL_B, settings), new Item.Settings());

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(THETISCOPE::getDefaultStack)
            .displayName(Text.translatable("itemGroup.occmy.group"))
            .entries((context, entries) -> {
                ITEMS.remove(DISK_NULLITY);
                ITEMS.remove(FLOPSTER_DRIVE_MODEL_B);
                ITEMS.forEach(entries::add);
            })
            .build();

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OCCMY.ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        ITEMS.add(item);
        return item;
    }

    private static Item disk(String id, View view) {
        return register(id, Item::new, new Item.Settings().component(OccComponents.VIEW, view));
    }


}
