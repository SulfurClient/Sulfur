package gg.sulfur.client.impl.utils.networking;

import net.minecraft.network.Packet;
import gg.sulfur.client.api.util.Util;

public class PacketUtil implements Util {

    public static void sendPacket(Packet packet) {
        mc.getNetHandler().addToSendQueue(packet);
    }

    public static void sendPacketNoEvent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static void sendPacketSilent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}