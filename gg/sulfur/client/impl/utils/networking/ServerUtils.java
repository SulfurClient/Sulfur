package gg.sulfur.client.impl.utils.networking;

import net.minecraft.client.multiplayer.ServerData;
import gg.sulfur.client.api.util.Util;

public class ServerUtils implements Util {

    public static boolean onServer(final String server) {
        final ServerData serverData = mc.getCurrentServerData();
        return serverData != null && serverData.serverIP.toLowerCase().contains(server);
    }

    public static boolean isOnServer() {
        final ServerData serverData = mc.getCurrentServerData();
        return serverData != null;
    }

    public static String getServer() {
        final ServerData serverData = mc.getCurrentServerData();

        if (serverData == null)
            return "Singleplayer";

        return serverData.serverIP;
    }

    public static boolean onHypixel() {
        final ServerData serverData = mc.getCurrentServerData();

        if (serverData == null)
            return false;

        return serverData.serverIP.endsWith("hypixel.net") || serverData.serverIP.endsWith("hypixel.net:25565") || serverData.serverIP.equals("104.17.71.15") || serverData.serverIP.equals("104.17.71.15:25565");
    }
}