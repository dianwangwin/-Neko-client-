package me.neko.wurstplus.wurstplustwo.modules.movement;

import me.neko.wurstplus.wurstplustwo.guiscreen.settings.Setting;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;

public class WurstplusSprint extends Module {
    
    public WurstplusSprint() {
        super(Category.movement);

		this.name        = "Sprint";
		this.tag         = "Sprint";
		this.description = "ZOOOOOOOOM";
    }

    Setting rage = create("Rage", "SprintRage", true);

    @Override
	public void update() {

    	if (mc.player == null) return;

    	if (rage.get_value(true) && (mc.player.moveForward != 0 || mc.player.moveStrafing != 0)) {
			mc.player.setSprinting(true);
		} else mc.player.setSprinting(mc.player.moveForward > 0 || mc.player.moveStrafing > 0);
		
	}


}