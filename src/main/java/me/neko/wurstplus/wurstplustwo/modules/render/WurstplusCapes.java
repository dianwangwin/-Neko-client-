package me.neko.wurstplus.wurstplustwo.modules.render;

import me.neko.wurstplus.wurstplustwo.guiscreen.settings.Setting;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;

public class WurstplusCapes extends Module {

    public WurstplusCapes() {
        super(Category.render);

        this.name = "Capes";
        this.tag = "Capes";
        this.description = "see epic capes behind epic dudes";
    }

    Setting cape = create("Cape", "CapeCape", "New", combobox("New", "OG"));

}
