package net.minecraft.nbt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

public final class NBTUtil {
    // private static final String __OBFID = "CL_00001901";

    /**
     * Reads and returns a GameProfile that has been saved to the passed in NBTTagCompound
     */
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound) {
        String name = null;
        String id = null;

        if (compound.hasKey("Name", 8)) {
            name = compound.getString("Name");
        }

        if (compound.hasKey("Id", 8)) {
            id = compound.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(name) && StringUtils.isNullOrEmpty(id)) {
            return null;
        } else {
            UUID uuid;

            try {
                uuid = UUID.fromString(id);
            } catch (Throwable var12) {
                uuid = null;
            }

            GameProfile gameProfile = new GameProfile(uuid, name);

            if (compound.hasKey("Properties", 10)) {
                NBTTagCompound properties = compound.getCompoundTag("Properties");
                Iterator propertyIterator = properties.getKeySet().iterator();

                while (propertyIterator.hasNext()) {
                    String var7 = (String) propertyIterator.next();
                    NBTTagList var8 = properties.getTagList(var7, 10);

                    for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                        NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                        String var11 = var10.getString("Value");

                        if (var10.hasKey("Signature", 8)) {
                            gameProfile.getProperties().put(var7, new Property(var7, var11, var10.getString("Signature")));
                        } else {
                            gameProfile.getProperties().put(var7, new Property(var7, var11));
                        }
                    }
                }
            }

            return gameProfile;
        }
    }

    public static NBTTagCompound writeGameProfile(NBTTagCompound p_180708_0_, GameProfile p_180708_1_) {
        if (!StringUtils.isNullOrEmpty(p_180708_1_.getName())) {
            p_180708_0_.setString("Name", p_180708_1_.getName());
        }

        if (p_180708_1_.getId() != null) {
            p_180708_0_.setString("Id", p_180708_1_.getId().toString());
        }

        if (!p_180708_1_.getProperties().isEmpty()) {
            NBTTagCompound var2 = new NBTTagCompound();
            Iterator var3 = p_180708_1_.getProperties().keySet().iterator();

            while (var3.hasNext()) {
                String var4 = (String) var3.next();
                NBTTagList var5 = new NBTTagList();
                NBTTagCompound var8;

                for (Iterator var6 = p_180708_1_.getProperties().get(var4).iterator(); var6.hasNext(); var5.appendTag(var8)) {
                    Property var7 = (Property) var6.next();
                    var8 = new NBTTagCompound();
                    var8.setString("Value", var7.getValue());

                    if (var7.hasSignature()) {
                        var8.setString("Signature", var7.getSignature());
                    }
                }

                var2.setTag(var4, var5);
            }

            p_180708_0_.setTag("Properties", var2);
        }

        return p_180708_0_;
    }
}
