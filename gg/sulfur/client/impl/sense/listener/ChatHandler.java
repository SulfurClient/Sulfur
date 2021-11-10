package gg.sulfur.client.impl.sense.listener;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import gg.sulfur.client.impl.sense.Channels;

public class ChatHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getChannel().getIdLong() == Channels.CHAT && !event.getMessage().getContentStripped().contains("§aChat §7» §7" + Sulfur.getInstance().getUser() + ":")) {
            ChatUtil.displayMessage(event.getMessage().getContentStripped().replaceAll("&", "§"));
        }
    }
}