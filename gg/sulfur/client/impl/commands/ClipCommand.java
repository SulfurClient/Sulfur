package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import gg.sulfur.client.impl.utils.networking.ServerUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ClipCommand extends Command {

    public ClipCommand() {
        super(new CommandData("vclip", "hclip"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            switch (command.toLowerCase()) {
                case "hclip": {
                    double amt = Double.parseDouble(args[1]);
                    double[] forward = MotionUtils.teleportForward(amt);
                    mc.thePlayer.setPosition(mc.thePlayer.posX + forward[0], mc.thePlayer.posY, mc.thePlayer.posZ + forward[1]);
//                    Client.INSTANCE.getNotificationManager().postNotification("H-Clipped " + amt + " Blocks", NotificationType.INFO);
                    ChatUtil.displayChatMessage("H-Clipped " + amt + " Blocks");
                    break;
                }
                case "vclip": {
                    double amt = Double.parseDouble(args[1]);

                    if (ServerUtils.onHypixel()) {
                        for (int i = 0; i < 6; i++) {
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + amt, mc.thePlayer.posZ, false));
                        }
                    }

                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + amt, mc.thePlayer.posZ);
                    ChatUtil.displayChatMessage("V-Clipped " + amt + " Blocks");
                    break;
                }
            }
        } catch (NumberFormatException exception) {
            ChatUtil.displayChatMessage("Invalid amount!");
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\n[h/v]clip [distance]");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("An unknown error occurred.");
        }
    }
}
