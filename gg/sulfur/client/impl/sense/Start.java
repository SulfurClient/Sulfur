package gg.sulfur.client.impl.sense;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.versioning.BuildType;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.enums.PacketDirection;
import gg.sulfur.client.impl.sense.listener.ChatHandler;
import gg.sulfur.client.impl.sense.listener.CommandHandler;
import gg.sulfur.client.impl.sense.listener.SenseHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import gg.sulfur.client.impl.sense.manager.SenseManager;
import gg.sulfur.client.impl.sense.utils.Util;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import javax.security.auth.login.LoginException;

public class Start implements EventListener {

    private static Start instance;

    private final SenseManager senseManager = new SenseManager();

    public static void launch() throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault("ODc0MTMyMDA2NzY0Njc5MjE5.YRCg3w.A3OzH03y2RILgpOfDkbmPprwS2g");
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("the Sulfur users"));

        instance = new Start();

        builder.addEventListeners(new Start());
        builder.addEventListeners(new ChatHandler());
        builder.addEventListeners(new SenseHandler());
        builder.addEventListeners(new CommandHandler());

        builder.build();
    }

    public SenseManager getSenseManager() {
        return senseManager;
    }

    public static Start getInstance() {
        return instance;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof ReadyEvent) {
            new Util(event.getJDA());
            Util.message(Sulfur.getInstance().getUser() + " has launched the client. ver. " + Sulfur.getInstance().getClientVersion() + (Sulfur.getInstance().getBuildType() == BuildType.NORMAL ? "" : " - " + Sulfur.getInstance().getBuildType().getName()), Channels.STARTUPS);
        }
    }
}
