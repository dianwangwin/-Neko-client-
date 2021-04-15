package me.neko.wurstplus.wurstplustwo.modules.chat;


import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;

public class ClearChat extends Module {
    public ClearChat() {
        super(Category.chat);

        this.name = "Clear Chatbox";
        this.tag = "ClearChatbox";
        this.description = "Removes the default minecraft chat outline.";
    }
}