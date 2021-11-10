package gg.sulfur.client.impl.modules.render

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.api.property.impl.ColorValue
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.Render3DEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


/**
 * @author Kansio
 * @created 5:01 PM
 * @project Client
 */
class Breadcrumbs(moduleData: ModuleData) : Module(moduleData) {

    private val fadeTime = NumberValue("Fade Time", this, 5.0, 1.0, 20.0)
    
    private val colorValue = ColorValue("Color", this, Color(255, 255, 255))
    private val colorRainbow = BooleanValue("Rainbow", this, false)
    private val fade = BooleanValue("Fade", this,true)

    private val points = mutableListOf<BreadcrumbPoint>()
    private var head=0

    val color = colorValue.value

    @Subscribe
    fun onRender3D(event: Render3DEvent) {
        val fTime=fadeTime.value*1000
        val fadeSec=System.currentTimeMillis()-fTime

        synchronized(points) {
            GL11.glPushMatrix()
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glLineWidth(2F)
            GL11.glBegin(GL11.GL_LINE_STRIP)
            val renderPosX = mc.renderManager.viewerPosX
            val renderPosY = mc.renderManager.viewerPosY
            val renderPosZ = mc.renderManager.viewerPosZ
            for (point in points.map { it }) {
                RenderUtils.glColor(colorValue.value, if(fade.value) {
                    val pct=(point.time - fadeSec).toFloat() / fTime
                    if(pct<0||pct>1){
                        points.remove(point)
                        head=points.indexOf(point) + 1
                        continue
                    }
                    pct
                }else{ 1f })
                //GL11.glColor4f()
                GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
            }
            GL11.glColor4d(1.0,1.0,1.0,1.0)
            GL11.glEnd()
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glPopMatrix()
        }
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        synchronized(points) {
            points.add(BreadcrumbPoint(mc.thePlayer.posX, mc.thePlayer.entityBoundingBox.minY, mc.thePlayer.posZ, System.currentTimeMillis(), color))
        }
    }

    override fun onEnable() {
        head=0
        if (mc.thePlayer == null) return
    }

    override fun onDisable() {
        synchronized(points) {
            points.clear()
            head=0
        }
    }

    fun glColor(color: Color, alpha: Float) {
        val red = color.red / 255f
        val green = color.green / 255f
        val blue = color.blue / 255f
        GlStateManager.color(red, green, blue, alpha)
    }

    init {
        register(fade, fadeTime, colorRainbow, colorValue)
    }

    class BreadcrumbPoint(val x: Double, val y: Double, val z: Double, val time: Long, val color: Color)

}