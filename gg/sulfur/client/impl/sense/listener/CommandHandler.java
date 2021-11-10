package gg.sulfur.client.impl.sense.listener;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.impl.utils.networking.ServerUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import gg.sulfur.client.impl.sense.Channels;
import gg.sulfur.client.impl.sense.Start;
import gg.sulfur.client.impl.sense.utils.Util;

import java.util.Objects;

public class CommandHandler extends ListenerAdapter {

    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentStripped().split(" ");

        switch (message[0]) {
            case "/updatesense": {
                if (ServerUtils.isOnServer()) {
                    Util.message(Sulfur.getInstance().getUser() + " " + Minecraft.getMinecraft().thePlayer.getName() + " " + ServerUtils.getServer(), Channels.SENSE);
                    Start.getInstance().getSenseManager().getSenseUsers().clear();
                }
                break;
            }
            case "/users": {
                if (event.getMessage().getAuthor() == event.getJDA().getSelfUser()) return;
                Util.message(Sulfur.getInstance().getUser() + " - " + System.getProperty("user.name"), Channels.COMMANDS);
                break;
            }
            case "/alert": {
                if (event.getMessage().getAuthor() == event.getJDA().getSelfUser()) return;
                ChatUtil.displayMessage(event.getMessage().getContentStripped().replaceAll("&", "§").replaceAll("/alert ", ""));
                break;
            }
        }
    }
}
