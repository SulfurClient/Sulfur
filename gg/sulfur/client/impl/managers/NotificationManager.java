package gg.sulfur.client.impl.managers;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.manager.Manager;
import gg.sulfur.client.api.notification.AbstractNotification;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.notification.Error;
import gg.sulfur.client.impl.notification.Info;
import gg.sulfur.client.impl.notification.Warning;
import gg.sulfur.client.impl.notification.type.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager extends Manager<AbstractNotification> {

    public NotificationManager() {
        super(new CopyOnWriteArrayList<>());
    }

    @Override
    public void onCreated() {
        Sulfur.getInstance().getEventBus().register(this);
    }

    /**
     * Renders all current notifications.
     *
     * @param event - Ignore this, Called by the {@code EventBus}
     */
    @Subscribe
    public void renderAll(RenderHUDEvent event) {
        this.getObjects().forEach(AbstractNotification::update);
    }

    /**
     * Queues a notification for rendering
     *
     * @param message - The message to display
     * @param type    - The notification's {@code NotificationType}
     */
    public void postNotification(String message, NotificationType type) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(message) - 15;
        int height = resolution.getScaledHeight() + getObjects().size() * -21;
        long endTime = System.currentTimeMillis() + 1500L;
        AbstractNotification notification = null;
        switch (type) {
            case ERROR:
                notification = new Error(message, width, height, endTime);
                break;
            case INFO:
                notification = new Info(message, width, height, endTime);
                break;
            case WARNING:
                notification = new Warning(message, width, height, endTime);
                break;
        }
        this.add(notification);
    }

}
