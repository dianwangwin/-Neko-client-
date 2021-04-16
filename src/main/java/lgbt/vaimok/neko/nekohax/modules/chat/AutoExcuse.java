package lgbt.vaimok.neko.nekohax.modules.chat;

import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;

import java.util.Random;

public class AutoExcuse extends Module
{
    int diedTime;
    
    public AutoExcuse() {
        super(Category.chat);
        this.diedTime = 0;
        this.name = "Excuse Aura";
        this.tag = "AutoExcuse";
        this.description = "tell people why you died";
    }
    
    @Override
    public void update() {
        if (this.diedTime > 0) {
            --this.diedTime;
        }
        if (AutoExcuse.mc.player.isDead) {
            this.diedTime = 500;
        }
        if (!AutoExcuse.mc.player.isDead && this.diedTime > 0) {
            final Random rand = new Random();
            final int randomNum = rand.nextInt(6) + 1;
            if (randomNum == 1) {
                AutoExcuse.mc.player.sendChatMessage("ping player!");
            }
            if (randomNum == 2) {
                AutoExcuse.mc.player.sendChatMessage("i was afk dude!");
            }
            if (randomNum == 3) {
                AutoExcuse.mc.player.sendChatMessage("i dont have a proper config!");
            }
            if (randomNum == 5) {
                AutoExcuse.mc.player.sendChatMessage("nekohax users are unkillable, wym?");
            }
            if (randomNum == 6) {
                AutoExcuse.mc.player.sendChatMessage("stop cheating thats unfair");
            }
            this.diedTime = 0;
        }
    }
}
