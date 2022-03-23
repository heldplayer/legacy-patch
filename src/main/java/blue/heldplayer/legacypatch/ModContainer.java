package blue.heldplayer.legacypatch;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Collections;

public class ModContainer extends DummyModContainer {
    public ModContainer() {
        super(new ModMetadata());

        ModMetadata meta = this.getMetadata();
        meta.modId = "legacy-patch";
        meta.name = "Legacy Patch";
        meta.version = "1.7.10.1";
        meta.authorList = Collections.singletonList("heldplayer");
        meta.description = "Coremod to Patch iChun's Portal Gun mod";
        meta.url = "https://github.com/heldplayer/legacy-patch";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true; // Return true to not show up as disabled in the mod list
    }
}
