package gg.sulfur.client.impl.managers;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.config.ConfigCommand;
import gg.sulfur.client.api.manager.Manager;
import gg.sulfur.client.impl.commands.*;
import java.util.ArrayList;

public class CommandManager extends Manager<Command> {

    public CommandManager() {
        super(new ArrayList<>());
    }

    @Override
    public void onCreated() {
        this.add(new HelpCommand());
        this.add(new ClipCommand());
        this.add(new UsersCommand());
        this.add(new KillsultCommand());
        this.add(new SearchEngineCommand());
        this.add(new NameProtectCommand());
        this.add(new ConfigCommand());
        this.add(new NameCommand());
        this.add(new SayCommand());
        this.add(new ToggleCommand());
        this.add(new BindCommand());
        this.add(new DownloadCommand());
        this.add(new FriendCommand());
        this.add(new DiscordCommand());
        this.add(new PluginFinderCommand());
        this.add(new GayRateCommand());
        this.add(new TeleportCommand());
        this.add(new RenameCommand());
        this.add(new DamageTeleportCommand());
        this.add(new SpamMessageCommand());
//        Client.INSTANCE.getModuleManager().getObjects().forEach(module -> {
//            if (!Client.INSTANCE.getValueManager().hasValues(module))
//                return;
//            this.add(new ModuleCommandBase(new CommandData(module.getModuleData().getName().toLowerCase().replace(" ", "")), module));
//        });
    }
}
