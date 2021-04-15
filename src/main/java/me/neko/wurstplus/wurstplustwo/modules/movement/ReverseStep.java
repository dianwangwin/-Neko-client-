package me.neko.wurstplus.wurstplustwo.modules.movement;

import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;

public class ReverseStep extends Module {
    public ReverseStep() {
        super(Category.movement);
        this.name = "Reverse Step";
        this.tag = "ReverseStep";
        this.description = "Step, but in reverse";
    }

    @Override
    public void update() {
        if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) return;
        if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) return;

        mc.player.motionY = -1;
    }
}
