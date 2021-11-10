package gg.sulfur.client.impl.commands;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;

import java.util.NoSuchElementException;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super(new CommandData("toggle", "t"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            switch (command.toLowerCase()) {
                case "t":
                case "toggle":
                    Sulfur.getInstance().getModuleManager().getByNameIgnoreSpaceCaseInsensitive(args[1]).toggle();
                    ChatUtil.displayChatMessage("Toggled.");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\n[toggle/t] [module]");
        } catch (NoSuchElementException exception) {
            ChatUtil.displayChatMessage("Invalid module.");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("The fuck did you do? LOL");
        }
    }
}
