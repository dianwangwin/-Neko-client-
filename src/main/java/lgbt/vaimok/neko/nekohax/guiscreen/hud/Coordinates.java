package lgbt.vaimok.neko.nekohax.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.guiscreen.render.pinnables.Pinnable;


public class Coordinates extends Pinnable {
	ChatFormatting dg = ChatFormatting.DARK_GRAY;
	ChatFormatting db = ChatFormatting.DARK_BLUE;
	ChatFormatting dr = ChatFormatting.DARK_RED;

	public Coordinates() {
		super("Coordinates", "Coordinates", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = NekoHax.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String x = NekoHax.g + "[" + NekoHax.r + Integer.toString((int) (mc.player.posX)) + NekoHax.g + "]" + NekoHax.r;
		String y = NekoHax.g + "[" + NekoHax.r + Integer.toString((int) (mc.player.posY)) + NekoHax.g + "]" + NekoHax.r;
		String z = NekoHax.g + "[" + NekoHax.r + Integer.toString((int) (mc.player.posZ)) + NekoHax.g + "]" + NekoHax.r;

		String x_nether = NekoHax.g + "[" + NekoHax.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posX / 8) : (mc.player.posX * 8))) + NekoHax.g + "]" + NekoHax.r;
		String z_nether = NekoHax.g + "[" + NekoHax.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posZ / 8) : (mc.player.posZ * 8))) + NekoHax.g + "]" + NekoHax.r;

		String line = "XYZ " + x + y + z + " XZ " + x_nether + z_nether;

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width"));
		this.set_height(this.get(line, "height") + 2);
	}
}