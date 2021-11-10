package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.java.NotificationUtil;
import gg.sulfur.client.impl.utils.player.ChatUtil;

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

    }
}
