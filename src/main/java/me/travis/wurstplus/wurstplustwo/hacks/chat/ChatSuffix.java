package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.Wurstplus;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class ChatSuffix extends Module {
  public ChatSuffix() {
    super("ChatSuffix", Module.Category.PLAYER);
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
