package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.EventCancellable;
import net.minecraft.client.gui.GuiScreen;

// External.


public class EventGUIScreen extends EventCancellable {
	private final GuiScreen guiscreen;

	public EventGUIScreen(GuiScreen screen) {
		super();

		guiscreen = screen;
	}

	public GuiScreen get_guiscreen() {
		return guiscreen;
	}
}