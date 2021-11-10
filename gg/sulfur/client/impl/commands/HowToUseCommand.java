package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;

public class HowToUseCommand extends Command {

    public HowToUseCommand(CommandData data) {
        super(data);
    }

    @Override
    public void run(String command, String... args) {
        if (args.length > 1 && args[0].toLowerCase().contains("mmc")) {
            ChatUtil.displayChatMessage("http://zerotwoclient.xyz/howtouse/mmcbridge.html");
        }
    }
}
