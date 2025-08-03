package symbolics.division.occmy.obv;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.item.Thetiscope;
import symbolics.division.occmy.view.Views;

import java.util.function.Function;

public class OccItems {
    public static void init() {
    }

    public static final Item THETISCOPE = register("thetiscope", Thetiscope::new, new Item.Settings()
            .component(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE)
            .component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)
    );

    public static final Item DISK_PROJECTION = register("disk_projection", Item::new, new Item.Settings()
            .component(OccComponents.VIEW, Views.PROJECTION)
    );

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OCCMY.ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }
}
