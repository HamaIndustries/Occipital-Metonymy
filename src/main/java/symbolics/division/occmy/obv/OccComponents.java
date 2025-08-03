package symbolics.division.occmy.obv;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import symbolics.division.occmy.OCCMY;
import symbolics.division.occmy.view.View;
import symbolics.division.occmy.view.Views;

import java.util.function.UnaryOperator;

public class OccComponents {
	public static void init() {
	}

	public static ComponentType<View> VIEW = register("view", b -> b.codec(Views.CODEC));

	private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, OCCMY.id(id), ((ComponentType.Builder) builderOperator.apply(ComponentType.builder())).build());
	}
}
