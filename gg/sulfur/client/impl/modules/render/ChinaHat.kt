package gg.sulfur.client.impl.modules.render

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.ColorValue
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.Render3DEvent
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import org.lwjgl.util.glu.GLU
import java.awt.Color


/**
 * @author Kansio
 * @created 2:57 AM
 * @project Client
 */

class ChinaHat(data: ModuleData) : Module(data) {

    private val color = ColorValue("Color", this, Color(255, 1, 1))
    private val transparency = NumberValue("Transparency", this, 80.0, 1.0, 255.0)

    @Subscribe
    fun onRender3D(event: Render3DEvent) {
        GL11.glPushMatrix()
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)
        GL11.glColor4f(color.value.red/255f, color.value.green/255f, color.value.blue/255f, transparency.value.toInt()/255f)
        GL11.glTranslatef(0f, mc.thePlayer.height+0.4f, 0f)
        GL11.glRotatef(90f, 1f, 0f, 0f)

        val cylinder = Cylinder()
        cylinder.drawStyle = GLU.GLU_FILL
        cylinder.draw(0f, 0.7f, 0.3f, 30, 1)

        GlStateManager.disableColorMaterial()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
    }

    init {
        register(color, transparency)
    }

}