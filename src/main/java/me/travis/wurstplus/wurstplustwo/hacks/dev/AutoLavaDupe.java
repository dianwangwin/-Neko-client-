package me.travis.wurstplus.wurstplustwo.hacks.dev;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class AutoLavaDupe extends WurstplusHack {

    public AutoLavaDupe() {
        super(WurstplusCategory.WURSTPLUS_BETA);

        this.name        = "Auto Lava Dupe";
        this.tag         = "AutoLavaDupe";
        this.description = "Dupes";
   
    
      
      public void onEnable() {
        if (mc.player != null)
            mc.player.sendChatMessage("/kill");
        disable();
    
    }


 }
