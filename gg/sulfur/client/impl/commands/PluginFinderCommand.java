package gg.sulfur.client.impl.commands;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

/**
 * Credit: Arithmo.
 */
public class PluginFinderCommand extends Command {

    public PluginFinderCommand() {
        super(new CommandData("plugins"));

    }

    private Stopwatch timer = new Stopwatch();

    @Subscribe
    public void onEvent(PacketEvent event) {
        try {
            PacketEvent ep = event;
            if (ep.getPacket() instanceof S3APacketTabComplete) {
                S3APacketTabComplete packet = ep.getPacket();
                String[] commands = packet.func_149630_c();
                String message = "";
                int size = 0;

                for (String command : commands) {
                    String pluginName = command.split(":")[0].substring(1);

                    if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
                        size++;
                        if (message.isEmpty()) {
                            message += pluginName;
                        } else {
                            message += "\2478, \247a" + pluginName;
                        }
                    }
                }

                if (!message.isEmpty()) {
                    ChatUtil.displayChatMessage("\2477Plugins (\247f" + size + "\2477): \247a " + message + "\2477.");
                } else {
                    ChatUtil.displayChatMessage("Plugins: none.");
                }
                Sulfur.getInstance().getEventBus().unregister(this);
                event.setCancelled(true);
            }

            if (timer.timeElapsed(20000)) {
                Sulfur.getInstance().getEventBus().unregister(this);
                ChatUtil.displayChatMessage("Took too long (20s)!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void run(String command, String... args) {
        try {
            timer.resetTime();
            Sulfur.getInstance().getEventBus().register(this);
            PacketUtil.sendPacket(new C14PacketTabComplete("/"));
            ChatUtil.displayChatMessage("Listening for a S3APacketTabComplete for 20s!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}