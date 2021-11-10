package gg.sulfur.client.impl.modules.misc

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.combat.Aura
import gg.sulfur.client.impl.notification.type.NotificationType


/**
 * @author Kansio
 * @created 5:43 PM
 * @project Client
 */

class AutoToggle(moduleData: ModuleData) : Module(moduleData) {

    val toggleAura = BooleanValue("Toggle Aura", this, true);
    val auraToggleDist = NumberValue("Distance", this, 8.0, 0.0, 30.0);

    init {
        register(
            toggleAura, auraToggleDist
        )
    }

    override fun onEnable() {

    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        var aura = Sulfur.getInstance().moduleManager.getByName("Kill Aura") as Aura

        if (checkEnemiesNearby() && toggleAura.value && !aura.isToggled) {
            aura.toggle()
            Sulfur.getInstance().notificationManager.postNotification("Kill Aura has been toggled since there is an enemy nearby.", NotificationType.INFO)
        }
    }

    private fun checkEnemiesNearby(): Boolean {
        for (ent in mc.theWorld.playerEntities) {
            if (ent == mc.thePlayer) continue

            if (ent.getDistanceSqToEntity(mc.thePlayer) <= auraToggleDist.value) {
                return true
            }
        }
        return false
    }
}