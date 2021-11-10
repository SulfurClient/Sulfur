package gg.sulfur.client.impl.commands;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import gg.sulfur.client.impl.utils.pathfinding.DortPathFinder;
import gg.sulfur.client.impl.utils.pathfinding.Vec3;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class DamageTeleportCommand extends Command {

    public DamageTeleportCommand() {
        super(new CommandData("damageteleport", "dtp"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            ChatUtil.displayChatMessage(Arrays.toString(args));
            if (command.equalsIgnoreCase("tp") || command.equalsIgnoreCase("teleport")) {
                switch (args[2]) {
                    case "xyz": {
                        ArrayList<Vec3> path;

                        path = DortPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5])));

                        int i = 0;

                        if (args[1].equalsIgnoreCase("verus")) {
                            MotionUtils.damageVerus();
                        } else if (args[1].equalsIgnoreCase("normal")) {
                            MotionUtils.damagePlayer(mc.thePlayer.onGround);
                        } else {
                            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\nteleport [verus/normal] [player/xyz] [username/x] [y] [z]");
                        }
                        for (Vec3 vector : path) {
                            i++;
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vector.getX(), vector.getY(), vector.getZ(), true));
                            mc.thePlayer.setPosition(Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
//                            ChatUtil.displayChatMessage("Teleported " + i + " time(s).");
                        }

                        ChatUtil.displayChatMessage("Teleported to " + args[3] + " " + args[4] + " " + args[5] + " in " + i + " teleports.");
                    }
                    break;


                    case "player": {
                        String name = args[3];
                        Entity entity = mc.theWorld.getPlayerEntityByName(name, true);
                        ArrayList<Vec3> path;

                        path = DortPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(entity.posX, entity.posY, entity.posZ));

                        int i = 0;
                        for (Vec3 vector : path) {
                            i++;
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vector.getX(), vector.getY(), vector.getZ(), true));
                            mc.thePlayer.setPosition(entity.posX, entity.posY, entity.posZ);
                        }

                        ChatUtil.displayChatMessage("Teleported to " + entity.getName() + " in " + i + " teleports.");
                    }
                    break;

                    default:
                        ChatUtil.displayChatMessage("Invalid option.");
                        break;
//                }
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.displayChatMessage("Not enough arguments\nUsage:\nteleport [verus/normal] [player/xyz] [username/x] [y] [z]");
        } catch (Exception exception) {
            ChatUtil.displayChatMessage("Invalid arguments or entity is not in range.");
        }
    }

    private boolean isUnderBlock() {
        for (int i = (int) (Minecraft.getMinecraft().thePlayer.posY + 2); i < 255; ++i) {
            BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ);
            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockFenceGate || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockSign || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockButton)
                continue;
            return true;
        }
        return false;
    }

    private float getRotationFromPosition(final double x, final double z) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final float yaw = (float) Math.atan2(zDiff, xDiff) - 1.57079632679f;
        return yaw;
    }
}