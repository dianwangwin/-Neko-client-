package lgbt.vaimok.neko.nekohax.modules.chat;

import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.event.events.EventPacket;
import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Random;

// Zero alpine manager. 2


public class ChatSuffix extends Module {
	public ChatSuffix() {
		super(Category.chat);

		this.name        = "Chat Suffix";
		this.tag         = "ChatSuffix";
		this.description = "Show off how cool u are using nekohax.";
	}

	Setting ignore = create("Ignore", "ChatSuffixIgnore", true);
	Setting type   = create("Type", "ChatSuffixType", "Default", combobox("Default", "Random"));

	boolean accept_suffix;
	boolean suffix_default;
	boolean suffix_random;

	StringBuilder suffix;

	String[] random_client_name = {
		"trambled",
		"vaimok",
		"niggerkambing",
		"rte",
		"momin",
		"snine19",
		"goldenpancakes",
		"cober",
		"schmoke",
		"perry",
		"rpai"
	};

	String[] random_client_finish = {
		" plus",
		" epic",
		" god",
		" sex",
		" blue",
		" brown",
		" gay",
		" plus",
		" furry"
	};

	@EventHandler
	private Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		// If not be the CPacketChatMessage return.
		if (!(event.get_packet() instanceof CPacketChatMessage)) {
			return;
		}

		// Start event suffix.
		accept_suffix = true;

		// Get value.
		boolean ignore_prefix = ignore.get_value(true);

		String message = ((CPacketChatMessage) event.get_packet()).getMessage();

		// If is with some caracther.
		if (message.startsWith("/")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("\\") && ignore_prefix) accept_suffix = false;
		if (message.startsWith("!")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(":")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(";")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(".")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(",")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("@")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("&")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("*")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("$")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("#")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("(")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(")")  && ignore_prefix) accept_suffix = false;

		// Compare the values 
		if (type.in("Default")) {
			suffix_default = true;
			suffix_random  = false;
		}

		if (type.in("Random")) {
			suffix_default = false;
			suffix_random  = true;
		}

		// If accept.
		if (accept_suffix) {
			if (suffix_default) {
				// Just default.
				message += NekoHax.CLIENT_SIGN + convert_base("nekohax");
			}

			if (suffix_random) {
				// Create first the string builder.
				StringBuilder suffix_with_randoms = new StringBuilder();

				// Convert the base using the CustomFont.
				suffix_with_randoms.append(convert_base(random_string(random_client_name)));
				suffix_with_randoms.append(convert_base(random_string(random_client_finish)));

				message += NekoHax.CLIENT_SIGN + suffix_with_randoms.toString();
			}

			// If message 256 string length substring.
			if (message.length() >= 256) {
				message.substring(0, 256);
			}
		}

		// Send the message.
		((CPacketChatMessage) event.get_packet()).message = message;
	});

	// Get the random values string.
	public String random_string(String[] list) {
		return list[new Random().nextInt(list.length)];
	}

	// Convert the base using the CustomFont.
	public String convert_base(String base) {
		return NekoHax.smoth(base);
	}

	@Override
	public String array_detail() {
		// Update the detail.
		return this.type.get_current_value();
	}
}
