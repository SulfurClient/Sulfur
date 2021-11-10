package gg.sulfur.client.impl.utils.player;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.util.Util;

public class PlayerUtil implements Util {
    private static double lastDist;

    static {
        Sulfur.getInstance().getEventBus().post(new PlayerSP());
    }

    public static double getLastDist() {
        return lastDist;
    }

    public static class PlayerSP {


        @Subscribe
        public void sp(UpdateEvent event) {
            if (event.isPre()) {
                double xDif = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
                double zDif = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
                lastDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);
            }
        }
    }
}
