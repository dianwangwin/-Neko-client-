package me.neko.wurstplus.wurstplustwo.modules.combat;

import me.neko.wurstplus.wurstplustwo.guiscreen.settings.Setting;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.neko.wurstplus.wurstplustwo.util.BreakUtil;
import me.neko.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.neko.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class WurstplusAutoMine extends Module {

    public WurstplusAutoMine() {
        super(Category.combat);

        this.name        = "Auto Mine";
        this.tag         = "AutoMine";
        this.description = "jumpy is now never going to use the client again";
    }

    Setting end_crystal = create("End Crystal", "MineEndCrystal", false);
    Setting range = create("Range", "MineRange", 4, 0, 6);

    @Override
    protected void enable() {

        BlockPos target_block = null;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (mc.player.getDistance(player) > range.get_value(1)) continue;

            BlockPos p = WurstplusEntityUtil.is_cityable(player, end_crystal.get_value(true));

            if (p != null) {
                target_block = p;
            }
        }

        if (target_block == null) {
            WurstplusMessageUtil.send_client_message("cannot find block");
            this.disable();
        }

        BreakUtil.set_current_block(target_block);

    }

    @Override
    protected void disable() {
        BreakUtil.set_current_block(null);
    }
}
