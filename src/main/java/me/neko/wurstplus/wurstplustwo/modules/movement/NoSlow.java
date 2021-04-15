package me.neko.wurstplus.wurstplustwo.modules.movement;

import me.neko.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.InputUpdateEvent;



public class NoSlow extends Module
{
    private boolean sneaking;

    public NoSlow() {
        super(Category.movement);

        this.name        = "No Slow";
        this.tag         = "NoSlow";
        this.description = "Just no slows";
    }


    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    });

    public void onEnable(){
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }
}
