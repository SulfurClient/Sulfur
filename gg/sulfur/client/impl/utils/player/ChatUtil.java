package gg.sulfur.client.impl.utils.player;

import net.minecraft.util.ChatComponentText;
import gg.sulfur.client.api.util.Util;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class ChatUtil implements Util {

    public static void displayChatMessage(final String message) {
        if (mc.thePlayer == null) {
            System.out.println(String.join(" ", "[Sulfur]", message));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText(String.join(" ", "§9[Client]§f", message)));
        }
    }

    public static void displayMessage(final String message) {
        if (mc.thePlayer == null) {
            System.out.println(String.join(" ", "[Sulfur]", message));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void displayFlag(final String name, final String cheat, final int vl) {
        if (mc.thePlayer == null) {
            System.out.println(String.join(" ", "[Sulfur]", name + " flagged " + cheat + " - VL: " + vl + "."));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText(String.join(" ", "\u00a77[\u00a7cSulfur Client\u00a77]", name + " flagged \u00a7c" + cheat + "\u00a77 - VL: \u00a7c" + vl + "\u00a77.")));
        }
    }

    public static String clearColor(String string) {
        String newString = "";
        List<String> colors = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "d", "m", "l", "o", "n", "e", "k", "f");
        for (String color : colors) {
            newString = string.replaceAll("§" + color, "");
        }
        return newString;
    }
}
