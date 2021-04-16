package lgbt.vaimok.neko.nekohax.command.commands;

import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.command.Command;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.util.DrawnUtil;
import lgbt.vaimok.neko.nekohax.util.MessageUtil;

import java.util.List;

public class Drawn extends Command {
    
    public Drawn() {
        super("drawn", "Hide elements of the array list");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("module name needed");

            return true;
        }

        if (message.length == 2) {

            if (is_module(message[1])) {
                DrawnUtil.add_remove_item(message[1]);
                NekoHax.get_config_manager().save_settings();
            } else {
                MessageUtil.send_client_error_message("cannot find module by name: " + message[1]);
            }
            return true;

        }

        return false;
    
    }

    public boolean is_module(String s) {

        List<Module> modules = NekoHax.get_hack_manager().get_array_hacks();

        for (Module module : modules) {
            if (module.get_tag().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;

    }

}