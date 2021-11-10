package gg.sulfur.client.impl.sense.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Objects;

public class Util {

    static JDA jda;

    public Util(JDA jdaB) {
        jda = jdaB;
    }

    public static void message(String message, long id) {
        for (Guild guild : jda.getGuilds()) {
            if (guild.getTextChannelById(id) != null) {
                Objects.requireNonNull(guild.getTextChannelById(id)).sendMessage(message).complete();
            }
        }
    }

}
