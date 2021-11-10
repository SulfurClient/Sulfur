package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.sense.FunnyUtil;
import gg.sulfur.client.impl.utils.java.NotificationUtil;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import java.io.*;

import java.awt.*;

/**
 * @author Kansio
 * @created 3:38 AM
 * @project Client
 */
public class DownloadCommand extends Command {


    public DownloadCommand() {
        super(new CommandData("download", "porn"));
    }

    @Override
    public void run(String command, String... args) {
        String cat = args[1];
        int amount = Integer.parseInt(args[2]);
        FunnyUtil.custom(amount, cat.replaceAll(",", "+"));
        try {
            NotificationUtil.sendInfo("Download successful!", "You've received some porn.");
        } catch (AWTException e) {
            e.printStackTrace();
        }
        ChatUtil.displayChatMessage("You've received some porn! Check your desktop.");
    }
}
