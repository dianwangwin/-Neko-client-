package lgbt.vaimok.neko.nekohax.modules.client;

import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.util.DiscordUtil;

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