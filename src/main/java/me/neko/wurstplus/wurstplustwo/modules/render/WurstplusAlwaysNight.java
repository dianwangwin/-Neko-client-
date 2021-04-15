package me.neko.wurstplus.wurstplustwo.modules.render;

import me.neko.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class WurstplusAlwaysNight extends Module {

    public WurstplusAlwaysNight() {
        super(Category.render);

        this.name = "Always Night";
        this.tag = "AlwaysNight";
        this.description = "see even less";
    }

    @EventHandler
    private Listener<WurstplusEventRender> on_render = new Listener<>(event -> {
        if (mc.world == null) return;
        mc.world.setWorldTime(18000);
    });

    @Override
    public void update() {
        if (mc.world == null) return;
        mc.world.setWorldTime(18000);
    }
}
