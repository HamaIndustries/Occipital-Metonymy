package symbolics.division.occmy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.occmy.obv.OccItems;

public class OCCMY implements ModInitializer {
	public static final String ID = "occmy";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static Identifier id(String id) {
		return Identifier.of(ID, id);
	}

	@Override
	public void onInitialize() {
		OccItems.init();
	}
}
