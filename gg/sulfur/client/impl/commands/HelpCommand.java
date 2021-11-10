package gg.sulfur.client.impl.commands;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(new CommandData("help"));
    }

    @Override
    public void run(String command, String... args) {
        if ("help".equals(command)) {
            ChatUtil.displayMessage("§7§m----------------------------------------");
            ChatUtil.displayMessage("§9§lList of all the commands:");
            for (Command c : Sulfur.getInstance().getCommandManager().getObjects()) {
                ChatUtil.displayMessage("  §7- §b" + c.getData().getName());
            }
            ChatUtil.displayMessage("§7§m----------------------------------------");
        }
    }
}
