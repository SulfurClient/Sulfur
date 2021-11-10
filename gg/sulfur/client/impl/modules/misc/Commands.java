package gg.sulfur.client.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.ChatEvent;
import gg.sulfur.client.impl.utils.player.ChatUtil;

public class Commands extends Module {

    public Commands(ModuleData moduleData) {
        super(moduleData);
    }

    @Subscribe
    public void onChat(ChatEvent chatEvent) {
        if (chatEvent.getRawMessage().startsWith(".")) {
            chatEvent.setCancelled(true);
            String[] message = chatEvent.getRawMessage().split(" ");
            String commandName = message[0].substring(1);
            for (Command command : Sulfur.getInstance().getCommandManager().getObjects()) {
                if (command.getData().getName().equalsIgnoreCase(commandName) || contains(command.getData().getAliases(), commandName)) {
                    command.run(commandName, message);
                    return;
                }
            }
            ChatUtil.displayChatMessage("Command not found, use .help for a list of all commands.");
        }
    }

    private boolean contains(Object[] objects, Object object) {
        for (Object o : objects) {
            if (!object.equals(o))
                continue;
            return true;
        }
        return false;
    }

}
