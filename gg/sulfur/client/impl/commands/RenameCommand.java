package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.minecraft.client.Minecraft;
import gg.sulfur.client.Sulfur;

import java.io.*;

public class RenameCommand extends Command {

    public RenameCommand() {
        super(new CommandData("rename", "clientname"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            StringBuilder str = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }
            String msg = str.toString().replaceAll("&", "§");

            File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "settings.cum");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(msg);
            w.close();

            Sulfur.getInstance().setClientName(msg);

            ChatUtil.displayChatMessage("Done, set the client name to " + msg);
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("what the fuck did u do");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("ok something didn't work fuck.");
        }
    }
}