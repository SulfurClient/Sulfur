package net.minecraft.client.renderer;

import gg.sulfur.client.api.property.impl.interfaces.INameable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import gg.sulfur.client.impl.modules.combat.Aura;
import shadersmod.client.Shaders;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.impl.events.RenderItemEvent;
import gg.sulfur.client.impl.modules.render.Animations;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

    /**
     * A reference to the Minecraft object.
     */
    private final Minecraft mc;
    private ItemStack itemToRender;

    /**
     * How far the current item has been equipped (0 disequipped and 1 fully up)
     */
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager field_178111_g;
    private final RenderItem itemRenderer;

    /**
     * The index of the currently held item (0-8, or -1 if not yet updated)
     */
    private int equippedItemSlot = -1;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.field_178111_g = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityLivingBase, ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        if (itemStack != null) {
            Item var4 = itemStack.getItem();
            Block var5 = Block.getBlockFromItem(var4);
            GlStateManager.pushMatrix();

            if (this.itemRenderer.func_175050_a(itemStack)) {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);

                if (this.isTranslucent(var5)) {
                    GlStateManager.depthMask(false);
                }
            }

            this.itemRenderer.func_175049_a(itemStack, entityLivingBase, transformType);

            if (this.isTranslucent(var5)) {
                GlStateManager.depthMask(true);
            }

            GlStateManager.popMatrix();
        }
    }

    private boolean isTranslucent(Block block) {
        return block != null && block.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void func_178101_a(float p_178101_1_, float p_178101_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(p_178101_1_, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void func_178109_a(AbstractClientPlayer abstractClientPlayer) {
        int var2 = this.mc.theWorld.getCombinedLight(new BlockPos(abstractClientPlayer.posX, abstractClientPlayer.posY + (double) abstractClientPlayer.getEyeHeight(), abstractClientPlayer.posZ), 0);

        if (Config.isDynamicLights()) {
            var2 = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), var2);
        }

        float var3 = (float) (var2 & 65535);
        float var4 = (float) (var2 >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
    }

    private void func_178110_a(EntityPlayerSP p_178110_1_, float p_178110_2_) {
        float var3 = p_178110_1_.prevRenderArmPitch + (p_178110_1_.renderArmPitch - p_178110_1_.prevRenderArmPitch) * p_178110_2_;
        float var4 = p_178110_1_.prevRenderArmYaw + (p_178110_1_.renderArmYaw - p_178110_1_.prevRenderArmYaw) * p_178110_2_;
        GlStateManager.rotate((p_178110_1_.rotationPitch - var3) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((p_178110_1_.rotationYaw - var4) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private float func_178100_c(float p_178100_1_) {
        float var2 = 1.0F - p_178100_1_ / 45.0F + 0.1F;
        var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
        var2 = -MathHelper.cos(var2 * (float) Math.PI) * 0.5F + 0.5F;
        return var2;
    }

    private void func_180534_a(RenderPlayer renderPlayer) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.25F, -0.85F, 0.75F);
        renderPlayer.func_177138_b(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178106_b(RenderPlayer p_178106_1_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.3F, -1.1F, 0.45F);
        p_178106_1_.func_177139_c(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178102_b(AbstractClientPlayer p_178102_1_) {
        this.mc.getTextureManager().bindTexture(p_178102_1_.getLocationSkin());
        Render var2 = this.field_178111_g.getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer var3 = (RenderPlayer) var2;

        if (!p_178102_1_.isInvisible()) {
            this.func_180534_a(var3);
            this.func_178106_b(var3);
        }
    }

    private void renderMap(AbstractClientPlayer abstractClientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_) {
        float var5 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float) Math.PI);
        float var6 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float) Math.PI * 2.0F);
        float var7 = -0.2F * MathHelper.sin(p_178097_4_ * (float) Math.PI);
        GlStateManager.translate(var5, var6, var7);
        float var8 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0F, 0.04F, -0.72F);
        GlStateManager.translate(0.0F, p_178097_3_ * -1.2F, 0.0F);
        GlStateManager.translate(0.0F, var8 * -0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var8 * -85.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        this.func_178102_b(abstractClientPlayer);
        float var9 = MathHelper.sin(p_178097_4_ * p_178097_4_ * (float) Math.PI);
        float var10 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float) Math.PI);
        GlStateManager.rotate(var9 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var10 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var10 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-1.0F, -1.0F, 0.0F);
        GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer var12 = var11.getWorldRenderer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        var12.startDrawingQuads();
        var12.addVertexWithUV(-7.0D, 135.0D, 0.0D, 0.0D, 1.0D);
        var12.addVertexWithUV(135.0D, 135.0D, 0.0D, 1.0D, 1.0D);
        var12.addVertexWithUV(135.0D, -7.0D, 0.0D, 1.0D, 0.0D);
        var12.addVertexWithUV(-7.0D, -7.0D, 0.0D, 0.0D, 0.0D);
        var11.draw();
        MapData var13 = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);

        if (var13 != null) {
            this.mc.entityRenderer.getMapItemRenderer().func_148250_a(var13, false);
        }
    }

    private void func_178095_a(AbstractClientPlayer p_178095_1_, float p_178095_2_, float p_178095_3_) {
        float var4 = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float) Math.PI);
        float var5 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float) Math.PI * 2.0F);
        float var6 = -0.4F * MathHelper.sin(p_178095_3_ * (float) Math.PI);
        GlStateManager.translate(var4, var5, var6);
        GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178095_2_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var7 = MathHelper.sin(p_178095_3_ * p_178095_3_ * (float) Math.PI);
        float var8 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float) Math.PI);
        GlStateManager.rotate(var8 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var7 * -20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(p_178095_1_.getLocationSkin());
        GlStateManager.translate(-1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.translate(5.6F, 0.0F, 0.0F);
        Render var9 = this.field_178111_g.getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer var10 = (RenderPlayer) var9;
        var10.func_177138_b(this.mc.thePlayer);
    }

    private void func_178105_d(float p_178105_1_) {
        float var2 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float) Math.PI);
        float var3 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float) Math.PI * 2.0F);
        float var4 = -0.2F * MathHelper.sin(p_178105_1_ * (float) Math.PI);
        GlStateManager.translate(var2, var3, var4);
    }

    private void func_178104_a(AbstractClientPlayer p_178104_1_, float p_178104_2_) {
        float var3 = (float) p_178104_1_.getItemInUseCount() - p_178104_2_ + 1.0F;
        float var4 = var3 / (float) this.itemToRender.getMaxItemUseDuration();
        float var5 = MathHelper.abs(MathHelper.cos(var3 / 4.0F * (float) Math.PI) * 0.1F);

        if (var4 >= 0.8F) {
            var5 = 0.0F;
        }

        GlStateManager.translate(0.0F, var5, 0.0F);
        float var6 = 1.0F - (float) Math.pow(var4, 27.0D);
        GlStateManager.translate(var6 * 0.6F, var6 * -0.5F, var6 * 0.0F);
        GlStateManager.rotate(var6 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var6 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    public void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float) Math.PI);
        GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    private void func_178098_a(float f, AbstractClientPlayer abstractClientPlayer) {
        GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-0.9F, 0.2F, 0.0F);
        float var3 = (float) this.itemToRender.getMaxItemUseDuration() - ((float) abstractClientPlayer.getItemInUseCount() - f + 1.0F);
        float var4 = var3 / 20.0F;
        var4 = (var4 * var4 + var4 * 2.0F) / 3.0F;

        if (var4 > 1.0F) {
            var4 = 1.0F;
        }

        if (var4 > 0.1F) {
            float var5 = MathHelper.sin((var3 - 0.1F) * 1.3F);
            float var6 = var4 - 0.1F;
            float var7 = var5 * var6;
            GlStateManager.translate(var7 * 0.0F, var7 * 0.01F, var7 * 0.0F);
        }

        GlStateManager.translate(var4 * 0.0F, var4 * 0.0F, var4 * 0.1F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F + var4 * 0.2F);
    }

    public void func_178103_d() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Renders the active item in the player's hand when in first person mode. Args: partialTickTime
     */
    public void renderItemInFirstPerson(float partialTickTime) {
        float var2 = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTickTime);
        EntityPlayerSP var3 = this.mc.thePlayer;
        float var4 = var3.getSwingProgress(partialTickTime);
        float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * partialTickTime;
        float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * partialTickTime;
        this.func_178101_a(var5, var6);
        this.func_178109_a(var3);
        this.func_178110_a(var3, partialTickTime);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {
            Aura aura = Sulfur.getInstance().getModuleManager().get(Aura.class);
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderMap(var3, var5, var2, var4);
            } else if (var3.getItemInUseCount() > 0 || mc.thePlayer.getBlocking() || (Aura.target != null && aura.block.getValue())) {
                EnumAction var7 = this.itemToRender.getItemUseAction();

                switch (ItemRenderer.SwitchEnumAction.field_178094_a[var7.ordinal()]) {
                    case 1:
                        this.func_178096_b(var2, var4);
                        break;

                    case 2:
                    case 3:
                        this.func_178104_a(var3, partialTickTime);
                        this.func_178096_b(var2, var4);
                        break;

                    case 4:
                        //System.out.println("calling");
                        final RenderItemEvent renderItemEvent = new RenderItemEvent(itemToRender.getItem(), RenderItemEvent.ItemUseAction.BLOCK, var2, var4);
                        Sulfur.getInstance().getEventBus().post(renderItemEvent);
                        if (!renderItemEvent.isCancelled()) {
                            this.func_178096_b(var2 - .2f, 0);
                            this.func_178103_d();
                        }
                        break;

                    case 5:
                        this.func_178096_b(var2, var4);
                        this.func_178098_a(partialTickTime, var3);
                }
            } else {
                final Animations animations = Sulfur.getInstance().getModuleManager().get(Animations.class);
                if (!animations.smoothSwing.getValue()) {
                    this.func_178105_d(var4);
                }
                this.func_178096_b(var2, var4);
            }

            this.renderItem(var3, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!var3.isInvisible()) {
            this.func_178095_a(var3, var2, var4);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * Renders all the overlays that are in first person mode. Args: partialTickTime
     */
    public void renderOverlays(float p_78447_1_) {
        GlStateManager.disableAlpha();

        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            BlockPos blockPos = new BlockPos(this.mc.thePlayer);
            IBlockState var2 = this.mc.theWorld.getBlockState(blockPos);
            EntityPlayerSP var3 = this.mc.thePlayer;

            for (int overlayType = 0; overlayType < 8; ++overlayType) {
                double var5 = var3.posX + (double) (((float) ((overlayType) % 2) - 0.5F) * var3.width * 0.8F);
                double var7 = var3.posY + (double) (((float) ((overlayType >> 1) % 2) - 0.5F) * 0.1F);
                double var9 = var3.posZ + (double) (((float) ((overlayType >> 2) % 2) - 0.5F) * var3.width * 0.8F);
                blockPos = new BlockPos(var5, var7 + (double) var3.getEyeHeight(), var9);
                IBlockState var12 = this.mc.theWorld.getBlockState(blockPos);

                if (var12.getBlock().isVisuallyOpaque()) {
                    var2 = var12;
                }
            }

            if (var2.getBlock().getRenderType() != -1) {
                Object var13 = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);

                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, this.mc.thePlayer, p_78447_1_, var13, var2, blockPos)) {
                    this.func_178108_a(this.mc.getBlockRendererDispatcher().func_175023_a().func_178122_a(var2));
                }
            }
        }

        if (!this.mc.thePlayer.func_175149_v()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, this.mc.thePlayer, p_78447_1_)) {
                this.renderWaterOverlayTexture(p_78447_1_);
            }

            if (this.mc.thePlayer.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, this.mc.thePlayer, p_78447_1_)) {
                this.renderFireInFirstPerson();
            }
        }

        GlStateManager.enableAlpha();
    }

    private void func_178108_a(TextureAtlasSprite p_178108_2_) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator var3 = Tessellator.getInstance();
        WorldRenderer var4 = var3.getWorldRenderer();
        float var5 = 0.1F;
        GlStateManager.color(var5, var5, var5, 0.5F);
        GlStateManager.pushMatrix();
        float var6 = -1.0F;
        float var7 = 1.0F;
        float var8 = -1.0F;
        float var9 = 1.0F;
        float var10 = -0.5F;
        float var11 = p_178108_2_.getMinU();
        float var12 = p_178108_2_.getMaxU();
        float var13 = p_178108_2_.getMinV();
        float var14 = p_178108_2_.getMaxV();
        var4.startDrawingQuads();
        var4.addVertexWithUV(var6, var8, var10, var12, var14);
        var4.addVertexWithUV(var7, var8, var10, var11, var14);
        var4.addVertexWithUV(var7, var9, var10, var11, var13);
        var4.addVertexWithUV(var6, var9, var10, var12, var13);
        var3.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound
     * before being called. Used for the water overlay. Args: parialTickTime
     */
    private void renderWaterOverlayTexture(float p_78448_1_) {
        this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        float var4 = this.mc.thePlayer.getBrightness(p_78448_1_);
        GlStateManager.color(var4, var4, var4, 0.5F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        float var5 = 4.0F;
        float var6 = -1.0F;
        float var7 = 1.0F;
        float var8 = -1.0F;
        float var9 = 1.0F;
        float var10 = -0.5F;
        float var11 = -this.mc.thePlayer.rotationYaw / 64.0F;
        float var12 = this.mc.thePlayer.rotationPitch / 64.0F;
        var3.startDrawingQuads();
        var3.addVertexWithUV(var6, var8, var10, var5 + var11, var5 + var12);
        var3.addVertexWithUV(var7, var8, var10, var11, var5 + var12);
        var3.addVertexWithUV(var7, var9, var10, var11, var12);
        var3.addVertexWithUV(var6, var9, var10, var5 + var11, var12);
        var2.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
    }

    /**
     * Renders the fire on the screen for first person mode. Arg: partialTickTime
     */
    private void renderFireInFirstPerson() {
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float var4 = 1.0F;

        for (int var5 = 0; var5 < 2; ++var5) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite var6 = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float var7 = var6.getMinU();
            float var8 = var6.getMaxU();
            float var9 = var6.getMinV();
            float var10 = var6.getMaxV();
            float var11 = (0.0F - var4) / 2.0F;
            float var12 = var11 + var4;
            float var13 = 0.0F - var4 / 2.0F;
            float var14 = var13 + var4;
            float var15 = -0.5F;
            GlStateManager.translate((float) (-(var5 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.rotate((float) (var5 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            var3.startDrawingQuads();
            var3.addVertexWithUV(var11, var13, var15, var8, var10);
            var3.addVertexWithUV(var12, var13, var15, var7, var10);
            var3.addVertexWithUV(var12, var14, var15, var7, var9);
            var3.addVertexWithUV(var11, var14, var15, var8, var9);
            var2.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP var1 = this.mc.thePlayer;
        ItemStack var2 = var1.inventory.getCurrentItem();
        boolean var3 = false;

        if (this.itemToRender != null && var2 != null) {
            if (!this.itemToRender.getIsItemStackEqual(var2)) {
                if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists()) {
                    boolean var4 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, this.itemToRender, var2, this.equippedItemSlot != var1.inventory.currentItem);

                    if (!var4) {
                        this.itemToRender = var2;
                        this.equippedItemSlot = var1.inventory.currentItem;
                        return;
                    }
                }

                var3 = true;
            }
        } else var3 = this.itemToRender != null || var2 != null;

        float var41 = 0.4F;
        float var5 = var3 ? 0.0F : 1.0F;
        float var6 = MathHelper.clamp_float(var5 - this.equippedProgress, -var41, var41);
        this.equippedProgress += var6;

        if (this.equippedProgress < 0.1F) {
            if (Config.isShaders()) {
                Shaders.itemToRender = var2;
            }

            this.itemToRender = var2;
            this.equippedItemSlot = var1.inventory.currentItem;
        }
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress() {
        this.equippedProgress = 0.0F;
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0F;
    }

    static final class SwitchEnumAction {
        static final int[] field_178094_a = new int[EnumAction.values().length];

        static {
            try {
                field_178094_a[EnumAction.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                field_178094_a[EnumAction.EAT.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                field_178094_a[EnumAction.DRINK.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                field_178094_a[EnumAction.BLOCK.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {

            }

            try {
                field_178094_a[EnumAction.BOW.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {

            }
        }
    }
}
