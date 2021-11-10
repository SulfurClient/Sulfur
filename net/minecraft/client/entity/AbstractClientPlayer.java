package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.impl.events.SprintEvent;

public abstract class AbstractClientPlayer extends EntityPlayer {
    private NetworkPlayerInfo field_175157_a;
    private ResourceLocation locationOfCape = null;
    private String nameClear;
    // private static final String __OBFID = "CL_00000935";

    public AbstractClientPlayer(World worldIn, GameProfile p_i45074_2_) {
        super(worldIn, p_i45074_2_);
        this.nameClear = p_i45074_2_.getName();

        if (this.nameClear != null && !this.nameClear.isEmpty()) {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }

        CapeUtils.downloadCape(this);
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    public boolean func_175149_v() {
        try {
            NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getGameProfile().getId());
            return var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR;
        } catch (Exception nigger) {
            return false;
        }
    }

    public boolean hasCape() {
        return this.func_175155_b() != null;
    }

    protected NetworkPlayerInfo func_175155_b() {
        if (this.field_175157_a == null) {
            this.field_175157_a = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getUniqueID());
        }

        return this.field_175157_a;
    }


    public boolean hasSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 != null && var1.func_178856_e();
    }

    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : var1.func_178837_g();
    }

    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        } else if (this.locationOfCape != null) {
            return this.locationOfCape;
        } else {
            NetworkPlayerInfo var1 = this.func_175155_b();
            return var1 == null ? null : var1.func_178861_h();
        }
    }

    public static void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
        ITextureObject var3 = var2.getTexture(resourceLocationIn);

        if (var3 == null) {
            var3 = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
            var2.loadTexture(resourceLocationIn, var3);
        }

    }

    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String func_175154_l() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : var1.func_178851_f();
    }

    public float func_175156_o() {
        float var1 = 1.0F;

        if (this.capabilities.isFlying) {
            var1 *= 1.1F;
        }

        IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        var1 = (float) ((double) var1 * ((var2.getAttributeValue() / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
            var1 = 1.0F;
        }

        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            int var3 = this.getItemInUseDuration();
            float var4 = (float) var3 / 20.0F;

            if (var4 > 1.0F) {
                var4 = 1.0F;
            } else {
                var4 *= var4;
            }

            var1 *= 1.0F - var4 * 0.15F;
        }

        return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, this, var1) : var1;
    }

    public String getNameClear() {
        return this.nameClear;
    }

    public void setLocationOfCape(ResourceLocation locationOfCape) {
        this.locationOfCape = locationOfCape;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        SprintEvent sprint = new SprintEvent(sprinting);
        Sulfur.getInstance().getEventBus().post(sprint);
        sprinting = sprint.isSprinting();

        super.setSprinting(sprinting);
        IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (var2.getModifier(EntityLivingBase.sprintingSpeedBoostModifierUUID) != null) {
            var2.removeModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
        if (sprinting) {
            var2.applyModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
    }
}
