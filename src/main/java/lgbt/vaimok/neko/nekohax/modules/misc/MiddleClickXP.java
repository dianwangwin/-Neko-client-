package lgbt.vaimok.neko.nekohax.modules.misc;

import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MiddleClickXP extends Module {
    public MiddleClickXP() {
        super(Category.misc);
        this.name = "Middle Click XP";
        this.tag = "MiddleClickXP";
        this.description = "xp go brrrrrr";
    }

    public void update() {
        if (mc.player != null && mc.world != null) {
            int xp = InventoryUtil.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
            if (xp != -1 && Mouse.isButtonDown(2)) {
                InventoryUtil.switchToSlot(xp);
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            }

        }
    }
}
 
