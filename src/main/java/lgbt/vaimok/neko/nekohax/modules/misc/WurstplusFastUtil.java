package lgbt.vaimok.neko.nekohax.modules.misc;

import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;


public class WurstplusFastUtil extends Module {

	public WurstplusFastUtil() {
		super(Category.misc);

		this.name        = "Fast Util"; 
		this.tag         = "FastUtil";
		this.description = "use things faster";
	}

	Setting fast_place = create("Place","WurstplusFastPlace", false);
	Setting fast_break = create("Break","WurstplusFastBreak",false);
	Setting crystal = create("Crystal", "WurstplusFastCrystal",false);
	Setting exp = create("EXP","WurstplusFastExp",true);

	@Override
	public void update() {
		Item main = mc.player.getHeldItemMainhand().getItem();
		Item off  = mc.player.getHeldItemOffhand().getItem();

		boolean main_exp = main instanceof ItemExpBottle;
		boolean off_exp  = off instanceof ItemExpBottle;
		boolean main_cry = main instanceof ItemEndCrystal;
		boolean off_cry  = off instanceof ItemEndCrystal;

		if (main_exp | off_exp && exp.get_value(true)) {
			mc.rightClickDelayTimer = 0;
		}

		if (main_cry | off_cry && crystal.get_value(true)) {
			mc.rightClickDelayTimer = 0;
		}

		if (!(main_exp | off_exp | main_cry | off_cry) && fast_place.get_value(true)) {
			mc.rightClickDelayTimer = 0;
		}

		if (fast_break.get_value(true)) {
			mc.playerController.blockHitDelay = 0;
		}
	}
}