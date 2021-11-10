package gg.sulfur.client.impl.commands;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super(new CommandData("bind", "bind del"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            if (command.equalsIgnoreCase("bind")) {
                if (args[1].equalsIgnoreCase("del")) {
                    Module module = Sulfur.getInstance().getModuleManager().getByNameIgnoreSpaceCaseInsensitive(args[2]);
                    ChatUtil.displayChatMessage("Deleted the bind.");
                    module.setKeyBind(0);
                } else {
                    Module module = Sulfur.getInstance().getModuleManager().getByNameIgnoreSpaceCaseInsensitive(args[1]);
                    if (module != null) {
                        int key = Keyboard.getKeyIndex(args[2].toUpperCase());
                        if (key != -1) {
                            ChatUtil.displayChatMessage("The new bind is " + Keyboard.getKeyName(key) + ".");
                            module.setKeyBind(key);
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\nbind [module] [key]");
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\nbind del [module]");
        } catch (Exception exception) {
            exception.printStackTrace();
//            ChatUtil.displayChatMessage("Invalid arguments.");
        }
    }
}
