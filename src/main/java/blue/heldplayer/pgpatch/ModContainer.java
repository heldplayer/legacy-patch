package blue.heldplayer.pgpatch;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

public class ModContainer extends DummyModContainer {
    public ModContainer() {
        super(new ModMetadata());

        ModMetadata meta = this.getMetadata();
        meta.modId = "pg-patch";
        meta.name = "PG-Patch";
        meta.version = "0.0.1";
        meta.authorList = Arrays.asList("heldplayer");
        meta.description = "Coremod to Patch iChun's Portal Gun mod";
    }
}
