package gg.sulfur.client.impl.modules.render;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;

/**
 * @author Aidan
 */

public class HurtColor extends Module {

    public final BooleanValue booleanValue = new BooleanValue("Health Color", this, false);
    public final NumberValue red = new NumberValue("Red", this, 255, 0, 255, true);
    public final NumberValue green = new NumberValue("Green", this, 50, 0, 255, true);
    public final NumberValue blue = new NumberValue("Blue", this, 50, 0, 255, true);
    public final NumberValue alpha = new NumberValue("Alpha", this, 125, 0, 255, true);
    public final BooleanValue rainbow = new BooleanValue("Rainbow", this, false);

    public HurtColor(ModuleData moduleData) {
        super(moduleData);
        register(red, green, blue, alpha, rainbow, booleanValue);
    }
}
