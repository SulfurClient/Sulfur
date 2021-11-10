package gg.sulfur.client.impl.utils.world

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos

/**
 * @author Kansio
 * @created 3:58 PM
 * @project Client
 */
object BlockUtils {

    //Skidded from liquidbounce

    @JvmStatic
    fun getBlock(blockPos: BlockPos?): Block? = Minecraft.getMinecraft().theWorld.getBlockState(blockPos)?.block

    @JvmStatic
    fun getMaterial(blockPos: BlockPos?): Material? = getBlock(blockPos)?.material

}