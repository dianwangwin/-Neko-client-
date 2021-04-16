package lgbt.vaimok.neko.nekohax.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.command.Command;
import lgbt.vaimok.neko.nekohax.util.EzMessageUtil;
import lgbt.vaimok.neko.nekohax.util.MessageUtil;

public class EzMessage extends Command {

    public EzMessage() {
        super("ezmessage", "Set ez mode");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder ez = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                ez.append(word).append(" ");
            }
            EzMessageUtil.set_message(ez.toString());
            MessageUtil.send_client_message("ez message changed to " + ChatFormatting.BOLD + ez.toString());
            NekoHax.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
