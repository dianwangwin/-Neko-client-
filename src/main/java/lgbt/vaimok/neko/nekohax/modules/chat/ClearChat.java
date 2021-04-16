package lgbt.vaimok.neko.nekohax.modules.chat;


import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;

public class ClearChat extends Module {
    public ClearChat() {
        super(Category.chat);

        this.name = "Clear Chatbox";
        this.tag = "ClearChatbox";
        this.description = "Removes the default minecraft chat outline.";
    }
}