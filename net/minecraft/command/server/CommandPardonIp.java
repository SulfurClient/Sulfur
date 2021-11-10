package net.minecraft.command.server;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.regex.Matcher;

public class CommandPardonIp extends CommandBase {
    // private static final String __OBFID = "CL_00000720";

    public String getCommandName() {
        return "pardon-ip";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(sender);
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.unbanip.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].length() > 1) {
            Matcher var3 = CommandBanIp.field_147211_a.matcher(args[0]);

            if (var3.matches()) {
                MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(args[0]);
                notifyOperators(sender, this, "commands.unbanip.success", args[0]);
            } else {
                throw new SyntaxErrorException("commands.unbanip.invalid");
            }
        } else {
            throw new WrongUsageException("commands.unbanip.usage");
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
    }
}
