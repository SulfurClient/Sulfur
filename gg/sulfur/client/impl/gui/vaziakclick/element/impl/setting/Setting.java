package gg.sulfur.client.impl.gui.vaziakclick.element.impl.setting;

import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.impl.gui.vaziakclick.element.Element;

public abstract class Setting extends Element {
    protected Value value;
    public static int SETTING_PADDING = PADDING;

    @Override
    public int getHeight() {
        return 16;
    }
}
