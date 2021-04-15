package me.neko.wurstplus.wurstplustwo.event.events;

import me.neko.wurstplus.wurstplustwo.event.WurstplusEventCancellable;
import net.minecraft.client.gui.GuiScreen;

// External.


public class WurstplusEventGUIScreen extends WurstplusEventCancellable {
	private final GuiScreen guiscreen;

	public WurstplusEventGUIScreen(GuiScreen screen) {
		super();

		guiscreen = screen;
	}

	public GuiScreen get_guiscreen() {
		return guiscreen;
	}
}