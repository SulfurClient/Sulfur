package gg.sulfur.client.impl.notification;

import gg.sulfur.client.api.notification.AbstractNotification;

public class Warning extends AbstractNotification {

    public Warning(String message, int x, int y, long endTime) {
        super(message, x, y, endTime);
    }

    @Override
    public void update() {

    }
}
