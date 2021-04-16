package lgbt.vaimok.neko.nekohax.modules.movement;

import lgbt.vaimok.neko.nekohax.event.EventBusTwo;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
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
        EventBusTwo.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        EventBusTwo.EVENT_BUS.unsubscribe(this);
    }
}
