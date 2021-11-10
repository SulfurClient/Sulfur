package gg.sulfur.client.impl.commands;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.player.ChatUtil;

import java.util.NoSuchElementException;

public class FriendCommand extends Command {

    public FriendCommand() {
        super(new CommandData("friend", "f"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            switch (command.toLowerCase()) {
                case "friend":
                case "f":
                    switch (args.length) {
                        case 2: {
                            String name = args[1];
                            if (Sulfur.getInstance().getFriendManager().getObjects().contains(name.toLowerCase())) {
                                Sulfur.getInstance().getFriendManager().getObjects().remove(name.toLowerCase());
                                ChatUtil.displayChatMessage("Removed " + name + " from the friends list.");
                                return;
                            }
                            Sulfur.getInstance().getFriendManager().getObjects().add(name.toLowerCase());
                            ChatUtil.displayChatMessage("Added " + name + " to the friends list.");
                            break;
                        }
                        case 3: {
                            String name1 = args[2];
                            if (!Sulfur.getInstance().getFriendManager().getObjects().contains(name1.toLowerCase()) && args[1].equalsIgnoreCase("add")) {
                                Sulfur.getInstance().getFriendManager().getObjects().add(name1.toLowerCase());
                                ChatUtil.displayChatMessage("Added " + name1 + " to the friends list.");
                                return;
                            } else if (Sulfur.getInstance().getFriendManager().getObjects().contains(name1.toLowerCase()) && args[1].equalsIgnoreCase("remove")) {
                                Sulfur.getInstance().getFriendManager().getObjects().remove(name1.toLowerCase());
                                ChatUtil.displayChatMessage("Removed " + name1 + " from the friends list.");
                                return;
                            }
                            ChatUtil.displayChatMessage("Invalid parameters.");
                        }
                    }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\n[friend/f] [username]");
            exception.printStackTrace();
        } catch (NoSuchElementException exception) {
            ChatUtil.displayChatMessage("Invalid username.");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("The fuck did you do? LOL");
            exception.printStackTrace();
        }
    }
}
