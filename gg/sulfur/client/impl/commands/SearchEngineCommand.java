package gg.sulfur.client.impl.commands;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.api.file.MFile;
import gg.sulfur.client.impl.commands.engine.SearchEngineThread;
import gg.sulfur.client.impl.utils.player.ChatUtil;

public class SearchEngineCommand extends Command {

    boolean threadRunning;
    public static boolean postMode;

    SearchEngineThread s = new SearchEngineThread();
    Thread t2 = new Thread(s);

    public static String key = "1234565798";

    public SearchEngineCommand() {
        super(new CommandData("engine", "yanchop", "search", "scan", "dox"));
    }

    @Override
    public void run(String commandName, String... args) {

        if (!threadRunning) {
            t2.start();
            threadRunning = true;
        }

        if (args[1].startsWith("key=")) {
            key = args[1].substring(4);
            sendMsg("Key updated.");
            Sulfur.getInstance().getFileManager().getObjects().forEach(MFile::save);
            return;
        }

        if (key.length() == 0) {
            sendMsg("Error: key missing, please do key=(key) or buy one from https://shoppy.gg/@Yanchop");
            return;
        }

        if (args[1].equals("post=false")) {
            postMode = false;
            sendMsg("Post mode set to false.");
            return;
        }

        if (args[1].equals("post=true")) {
            postMode = true;
            sendMsg("Post mode set to true, you will now post the search results in public chat.");
            return;
        }

        if (args[0].startsWith("!check") || args[0].startsWith("!status")) {
            if (key.length() > 8) {
                SearchEngineThread.checkStatus();
            } else {
                sendMsg("Please activate a key by doing key=(key)");
            }

            return;
        }

        if (args.length == 2) {
            s.mojangQueue.add(args[1]);
            return;
        }

        if (args.length == 3) {
            s.searchQueue.add(args[1] + ":" + args[2]);
            return;
        }

        sendMsg("Invalid arguments.");
    }

    public static void sendMsg(String msg) {
        ChatUtil.displayChatMessage(msg);
    }
}
