package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.DiscordUtils;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class RichPresence extends WurstplusHack {
	
	public RichPresence() {
		
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "Rich Presence";
        this.tag = "RichPresence";
        this.description = "shows discord rpc";

    }
	
	public void onEnable() {
	    Discord.startRPC();
	}
	
	public void onDisable() {
	    Discord.stopRPC();
		}
	}
