package me.neko.wurstplus.wurstplustwo.modules.chat;

import me.neko.wurstplus.wurstplustwo.guiscreen.settings.Setting;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntiRacist extends Module {
    
    public AntiRacist() {
        super(Category.chat);

        this.name = "Anti Racist";
        this.tag = "AntiRacist";
        this.description = "i love black squares (circles on the other hand...)";
    }

    Setting delay = create("Delay", "AntiRacistDelay", 10, 0, 100);

    List<String> chants = new ArrayList<>();

    Random r = new Random();

    int tick_delay;

    @Override
    protected void enable() {
        
        tick_delay = 0;

        chants.add("<player> you fucking racist");
        chants.add("RIP GEORGE FLOYD");
        chants.add("#BLM");
        chants.add("#ICANTBREATHE");
        chants.add("#NOJUSTICENOPEACE");
        chants.add("IM NOT BLACK BUT I STAND WITH YOU");
        chants.add("END RACISM, JOIN EMPERIUM");
        chants.add("DEFUND THE POLICE");
        chants.add("<player> I HOPE YOU POSTED YOUR BLACK SQUARE");
        chants.add("RESPECT BLM");
        chants.add("IF YOURE NOT WITH US, YOURE AGAINST US");
        chants.add("DEREK CHAUVIN WAS A RACIST");

    }

    @Override
    public void update() {
        
        tick_delay++;
        if (tick_delay < delay.get_value(1)*10) return;

        String s = chants.get(r.nextInt(chants.size()));
        String name =  get_random_name();
        
        if (name == mc.player.getName()) return;

        mc.player.sendChatMessage(s.replace("<player>", name));

        tick_delay = 0;

    }

    public String get_random_name() {

        List<EntityPlayer> players = mc.world.playerEntities;

        return players.get(r.nextInt(players.size())).getName();

    }

}