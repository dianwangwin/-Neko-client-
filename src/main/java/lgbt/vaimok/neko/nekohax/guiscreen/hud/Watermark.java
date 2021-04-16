package lgbt.vaimok.neko.nekohax.guiscreen.hud;


import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.guiscreen.render.pinnables.Pinnable;
import com.mojang.realmsclient.gui.ChatFormatting;


public class Watermark extends Pinnable {
	public Watermark() {
		super("Watermark", "Watermark", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(255);
		int nl_a = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(220);

		String line = ChatFormatting.LIGHT_PURPLE + "NekoHax" + ChatFormatting.RESET + NekoHax.g + " v" + NekoHax.get_version();

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}
}
