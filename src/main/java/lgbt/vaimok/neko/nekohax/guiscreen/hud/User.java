package lgbt.vaimok.neko.nekohax.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.guiscreen.render.Draw;
import lgbt.vaimok.neko.nekohax.guiscreen.render.pinnables.Pinnable;
import lgbt.vaimok.neko.nekohax.util.TimeUtil;
import net.minecraft.util.math.MathHelper;


public class User extends Pinnable {
	public User() {
		super("User", "User", 1, 0, 0);
	}

	private int scaled_width;
	private int scaled_height;
	private int scale_factor;

	@Override
	public void render() {
		updateResolution();
		int nl_r = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		int time = TimeUtil.get_hour();
		String line;

		if (time >= 0 && time < 12) {
			line = "morning, " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD + mc.player.getName() + ChatFormatting.RESET + " nekohax too good :)";
		} else if (time >= 12 && time < 16) {
			line = "Hi, " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD +  mc.player.getName() + ChatFormatting.RESET + " you are hot asf";
		} else if (time >= 16 && time < 24) {
			line = "welcome back " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD +  mc.player.getName() + ChatFormatting.RESET + " we love you";
		} else {
			line = "Welcome, " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD +  mc.player.getName() + ChatFormatting.RESET + " <3";
		}

		mc.fontRenderer.drawStringWithShadow(line, scaled_width / 2f - mc.fontRenderer.getStringWidth(line) / 2f, 20f, new Draw.ClientColor(nl_r,nl_g,nl_b,nl_a).hex());

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}

	public void updateResolution() {
		this.scaled_width = mc.displayWidth;
		this.scaled_height = mc.displayHeight;
		this.scale_factor = 1;
		final boolean flag = mc.isUnicode();
		int i = mc.gameSettings.guiScale;
		if (i == 0) {
			i = 1000;
		}
		while (this.scale_factor < i && this.scaled_width / (this.scale_factor + 1) >= 320 && this.scaled_height / (this.scale_factor + 1) >= 240) {
			++this.scale_factor;
		}
		if (flag && this.scale_factor % 2 != 0 && this.scale_factor != 1) {
			--this.scale_factor;
		}
		final double scaledWidthD = this.scaled_width / (double)this.scale_factor;
		final double scaledHeightD = this.scaled_height / (double)this.scale_factor;
		this.scaled_width = MathHelper.ceil(scaledWidthD);
		this.scaled_height = MathHelper.ceil(scaledHeightD);
	}

}
