package gg.sulfur.client.api.property.impl;

/**
 * @author Kansio
 * @created 5:37 PM
 * @project Client
 */
import gg.sulfur.client.api.property.Value;

import java.awt.*;

public final class ColorValue extends Value<Color> {

    private Color color;
    private int alpha;

    private float sat;
    private float light;

    public ColorValue(String name, Object owner, Color color) {
        super(name, owner, color);
        this.color = color;

        this.sat = 100;
        this.light = 50;
    }

    @Override
    public Color getValue() {
        return super.getValue();
    }

    public float getLight() {
        return light;
    }

    public float getSat() {
        return sat;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public void setSat(float sat) {
        this.sat = sat;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || getValue().equals(o);
    }

}
