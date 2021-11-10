package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.sense.Start;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.sense.SenseUser;

public class UsersCommand extends Command {

    public UsersCommand() {
        super(new CommandData("users", "list"));
    }

    @Override
    public void run(String command, String... args) {
        for (SenseUser senseUser : Start.getInstance().getSenseManager().getSenseUsers()) {
            ChatUtil.displayChatMessage("§a" + senseUser.getClientName() + " §7(§a" + senseUser.getAccountName() + "§7) - §f" + senseUser.getServer());
        }
    }
}

