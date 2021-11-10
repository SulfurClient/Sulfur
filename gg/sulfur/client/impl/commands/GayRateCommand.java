package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import org.apache.commons.lang3.RandomUtils;

public class GayRateCommand extends Command {

    public GayRateCommand() {
        super(new CommandData("gayrate"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            if ("gayrate".equalsIgnoreCase(command)) {
                if (args[1].length() != 0 && args[1].length() < 16) {
                    int rate = 0;
                    switch (args[1].toLowerCase()) {
                        case "shotbowxd": {
                            rate = 100;
                            break;
                        }
                        case "divine":
                        case "kansio": {
                            rate = 0;
                            break;
                        }
                        default:  {
                            rate = RandomUtils.nextInt(0, 100);
                            break;
                        }
                    }
                    ChatUtil.displayChatMessage(args[1] + " is " + rate + "% gay.");
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\ngayrate [username]");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("An unknown error occurred.");
        }
    }
}
