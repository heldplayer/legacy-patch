package blue.heldplayer.legacypatch;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"blue.heldplayer.legacypatch"})
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class Plugin implements IFMLLoadingPlugin, IFMLCallHook {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"blue.heldplayer.legacypatch.PortalGunPatcher"};
    }

    @Override
    public String getModContainerClass() {
        return "blue.heldplayer.legacypatch.ModContainer";
    }

    @Override
    public String getSetupClass() {
        return "blue.heldplayer.legacypatch.Plugin";
    }

    @Override
    public void injectData(Map<String, Object> map) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() {
        return null;
    }
}
