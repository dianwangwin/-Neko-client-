package lgbt.vaimok.neko.nekohax.modules.misc;

import lgbt.vaimok.neko.nekohax.Modules.Category;
import lgbt.vaimok.neko.nekohax.Modules.Module;
import lgbt.vaimok.neko.nekohax.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public MiddleClickXP() {
        super(Category.misc);

        this.name        = "MiddleclickXP";
        this.tag         = "MiddleclickXP";
        this.description = "xp go brrrrrr";
}

        public void update() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         int xp = InventoryUtil.getHotbarItemSlot(Items.field_151062_by);
         if (xp != -1 && Mouse.isButtonDown(2)) {
            InventoryUtil.switchToSlot(xp);
            mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71441_e, EnumHand.MAIN_HAND);
         }

      }
   }
}
