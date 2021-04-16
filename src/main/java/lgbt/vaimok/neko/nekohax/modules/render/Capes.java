package lgbt.vaimok.neko.nekohax.modules.render;

import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;

public class Capes extends Module {

    public Capes() {
        super(Category.render);

        this.name = "Capes";
        this.tag = "Capes";
        this.description = "see epic capes behind epic dudes";
    }

    Setting cape = create("Cape", "CapeCape", "New", combobox("New", "OG"));

}
