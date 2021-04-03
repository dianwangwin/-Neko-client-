package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.Wurstplus;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class WurstplusChatSuffix extends WurstplusHack {
    
    public WurstplusTotempop() {
	        super(WurstplusCategory.WURSTPLUS_CHAT);
      
                this.name = "NekoSuffix";
		this.tag         = "NekoSuffix";
		this.description = "nekohax strong hax";
  }
  
  @SubscribeEvent
  public void onChat(ClientChatEvent event) {
    String NekoChat = " | ɴᴇᴋᴏʜᴀx" ;
    if (event.getMessage().startsWith("/") || event.getMessage().startsWith(".") || event
      .getMessage().startsWith(",") || event.getMessage().startsWith("-") || event
      .getMessage().startsWith("$") || event.getMessage().startsWith("*"))
      return; 
    event.setMessage(event.getMessage() + NekoChat);
  }
}		
