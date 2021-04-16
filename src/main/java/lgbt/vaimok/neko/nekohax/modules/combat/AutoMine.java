package lgbt.vaimok.neko.nekohax.modules.combat;

import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.util.BreakUtil;
import lgbt.vaimok.neko.nekohax.util.EntityUtil;
import lgbt.vaimok.neko.nekohax.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class AutoMine extends Module {

    public AutoMine() {
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

            BlockPos p = EntityUtil.is_cityable(player, end_crystal.get_value(true));

            if (p != null) {
                target_block = p;
            }
        }

        if (target_block == null) {
            MessageUtil.send_client_message("cannot find block");
            this.disable();
        }

        BreakUtil.set_current_block(target_block);

    }

    @Override
    protected void disable() {
        BreakUtil.set_current_block(null);
    }
}
