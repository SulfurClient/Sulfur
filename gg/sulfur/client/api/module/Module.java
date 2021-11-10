package gg.sulfur.client.api.module;

import gg.sulfur.client.api.module.interfaces.IToggleable;
import gg.sulfur.client.api.property.Value;
import net.minecraft.client.Minecraft;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.file.MFile;
import gg.sulfur.client.impl.utils.combat.MathUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Dort
 */
public abstract class Module implements IToggleable {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static final Random RANDOM = new Random();

    private final ModuleData moduleData;
    private final int color;
    private boolean toggled;
    private int keyBind;

    public Module(ModuleData moduleData) {
        this.moduleData = moduleData;
        int r = MathUtils.getRandomInRange(5, 245);
        int g = MathUtils.getRandomInRange(5, 245);
        int b = MathUtils.getRandomInRange(5, 245);
        System.out.println(r + " " + g + " " + b);
        color = new Color(r, g, b).getRGB();
        keyBind = moduleData.getDefaultBind();
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
        Sulfur.getInstance().getFileManager().getObjects().forEach(MFile::save);
    }

    public void setKeyBindNoCall(int keyBind) {
        this.keyBind = keyBind;
    }

    @Override
    public void toggle() {
        toggled = !toggled;
        mc.timer.timerSpeed = 1F;
        if (toggled) {
            Sulfur.getInstance().getEventBus().register(this);
            onEnable();
        } else {
            Sulfur.getInstance().getEventBus().unregister(this);
            onDisable();
            //mc.thePlayer.speedInAir = 0.02f;
            //mc.thePlayer.setBlocking(false);
        }
        onToggled();

    }

    @Override
    public void onToggled() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getSuffix() {
        return null;
    }

    public void register(Value... properties) {
        Collections.addAll(Sulfur.getInstance().getValueManager().getObjects(), properties);
    }

    public void unRegister(Value... properties) {
        //Collections.addAll(Sulfur.getInstance().getValueManager().getObjects(), properties);
        Sulfur.getInstance().getValueManager().getObjects().removeAll(Arrays.asList(properties));
    }

    public List<Value> getValues() {
        return Sulfur.getInstance().getValueManager().getValuesFromOwner(this);
    }

    public int getColor() {
        return color;
    }
}
