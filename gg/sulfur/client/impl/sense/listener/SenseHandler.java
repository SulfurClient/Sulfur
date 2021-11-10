package gg.sulfur.client.impl.sense.listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import gg.sulfur.client.impl.sense.Channels;
import gg.sulfur.client.impl.sense.SenseUser;
import gg.sulfur.client.impl.sense.Start;

public class SenseHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentStripped().split(" ");

        if (message.length == 3) {
            String clientName = message[0];
            String accountName = message[1];
            String server = message[2];

            if (event.getChannel().getIdLong() == Channels.SENSE) {
                Start.getInstance().getSenseManager().updateUser(new SenseUser(clientName, accountName, server));
            }
        }
    }
}
