package me.neko.wurstplus.wurstplustwo.modules.client;

import me.neko.wurstplus.wurstplustwo.util.DiscordUtil;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;

public class RichPresence extends Module {
	
	public RichPresence() {
		
        super(Category.misc);
        this.name = "Rich Presence";
        this.tag = "RichPresence";
        this.description = "shows discord rpc";
	}
    @Override
    public void enable() {
        DiscordUtil.init();
    }
    @Override
    public void disable() {
    	DiscordUtil.shutdown();
    }
}