package gg.sulfur.client.impl.modules.render

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.ColorValue
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.RenderHUDEvent
import gg.sulfur.client.impl.utils.render.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.sqrt


/**
 * @author Kansio
 * @created 4:13 PM
 * @project Client
 */

class SpeedGraph(moduleData: ModuleData) : Module(moduleData) {

    private val yMultiplier = NumberValue("Y Multiplier", this, 1.0, 1.0, 2.0)
    private val height = NumberValue("Height", this, 100.0, 30.0, 400.0)
    private val width = NumberValue("Width", this, 150.0, 100.0, 300.0)
    private val thickness = NumberValue("Thickness", this, 0.5, 0.0, 1.0)
    private val smoothness = NumberValue("Smoothness", this, 0.5, 0.0, 1.0)
    private val color = ColorValue("Color", this, Color(255, 255, 255))

    private val speedList = ArrayList<Double>()
    private var lastTick = -1
    private var lastSpeed = 0.01

    @Subscribe
    fun onRenderHUD(event: RenderHUDEvent) {
        if (mc.currentScreen != null) return
        if (!isToggled) return

        GL11.glPopMatrix()

        val width = width.value.toInt()
        if (lastTick != mc.thePlayer.ticksExisted) {
            lastTick = mc.thePlayer.ticksExisted
            val z2 = mc.thePlayer.posZ
            val z1 = mc.thePlayer.prevPosZ
            val x2 = mc.thePlayer.posX
            val x1 = mc.thePlayer.prevPosX
            var speed = sqrt((z2 - z1) * (z2 - z1) + (x2 - x1) * (x2 - x1))
            if (speed < 0)
                speed = -speed
            speed = (lastSpeed * 0.9 + speed * 0.1) * smoothness.value + speed * (1 - smoothness.value)
            lastSpeed = speed
            speedList.add(speed)
            while (speedList.size > width) {
                speedList.removeAt(0)
            }
        }
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(thickness.value.toFloat())
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)

        GL11.glBegin(GL11.GL_LINES)

        val size = speedList.size

        val start = (if (size > width) size - width else 0)
        for (i in start until size - 1) {
            val y = speedList[i] * 10 * yMultiplier.value
            val y1 = speedList[i + 1] * 10 * yMultiplier.value

            RenderUtils.glColor(color.value, 255)
            GL11.glVertex2d(i.toDouble() - start, height.value + 1 - y.coerceAtMost(height.value.toDouble()))
            GL11.glVertex2d(i + 1.0 - start, height.value + 1 - y1.coerceAtMost(height.value.toDouble()))
        }

        GL11.glEnd()

        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPushMatrix()

    }

    init {
        register(color, yMultiplier, height, width, thickness, smoothness)
    }
}
