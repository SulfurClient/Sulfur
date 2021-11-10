package gg.sulfur.client.impl.notification;

import gg.sulfur.client.api.notification.AbstractNotification;

public class Error extends AbstractNotification {

    public Error(String message, int x, int y, long endTime) {
        super(message, x, y, endTime);
    }

    @Override
    public void update() {

    }
}
