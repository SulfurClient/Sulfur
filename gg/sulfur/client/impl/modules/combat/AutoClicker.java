package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.combat.MathUtils;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.network.play.client.C02PacketUseEntity;

/**
 * @author Kansio
 * @created 3:24 PM
 * @project Client
 */
public class AutoClicker extends Module {

    private Stopwatch attackTimer = new Stopwatch();

    private NumberValue cps = new NumberValue("CPS", this, 10, 0, 20);
    private NumberValue rand = new NumberValue("Randomization", this, 3, 0, 6);

    private NumberValue particleMultiplier = new NumberValue("Particle Multiplier", this, 1, 1, 5);
    private BooleanValue alwaysCrit = new BooleanValue("Crit Particles", this, false);
    private BooleanValue sharpn = new BooleanValue("Sharp Particles", this, true);

    public AutoClicker(ModuleData moduleData) {
        super(moduleData);
        register(cps, rand);
    }

    @Override
    public void onEnable() {

    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.gameSettings.keyBindAttack.pressed) {
            double aps = cps.getValue() + MathUtils.getRandomInRange(0, rand.getValue());

            if (this.attackTimer.timeElapsed((long) (1000L / aps))) {
                this.attackTimer.resetTime();

                if (mc.thePlayer.isBlocking()) return;

                mc.thePlayer.swingItem();

                if (mc.objectMouseOver.entityHit != null) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));

                    for (int i = 0; i < cps.getValue() / 2; i++) {
                        if (!mc.thePlayer.onGround || (alwaysCrit.getValue())) {
                            mc.thePlayer.onCriticalHit(mc.objectMouseOver.entityHit);
                        }

                        if (sharpn.getValue() || (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().isItemEnchanted())) {
                            mc.thePlayer.onEnchantmentCritical(mc.objectMouseOver.entityHit);
                        }
                    }
                }
            }
        }
    }
}
