package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kansio
 * @created 2:46 PM
 * @project Client
 */

public class KillsultCommand extends Command {

    public KillsultCommand() {
        super(new CommandData("killsult", "addks"));
    }

    @Override
    public void run(String command, String... args) {
        try {

            StringBuilder str = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                str.append(args[i] + " ");
            }

            String msg = str.toString();

            File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "settings.cum");
            Writer w = new BufferedWriter(new FileWriter(file, true));
            w.write(msg);
            w.close();

            ChatUtil.displayChatMessage("Added '" + msg + "' to the killsult list.");
            for (String ks : getInsults()) {
                ChatUtil.displayChatMessage("-> " + ks);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("what the fuck did u do");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("ok something didn't work fuck.");
        }
    }

    public List<String> getInsults() throws IOException {
        List<String> insults = new ArrayList<>();

        File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "killsults.cum");

        if (!file.exists()) {
            File cum = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "killsults.cum");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write("<player> got cummed on ez");
            w.close();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            insults.add(st);
        }
        return insults;
    }
}