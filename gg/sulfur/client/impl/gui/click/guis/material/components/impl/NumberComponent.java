package gg.sulfur.client.impl.gui.click.guis.material.components.impl;


import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFont;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.utils.MouseUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import net.minecraft.util.MathHelper;
import gg.sulfur.client.impl.gui.click.guis.material.components.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends Component {
    private NumberValue numberValue;
    private boolean sliding;
    final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
    final CustomFontRenderer font2 = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();
    public NumberComponent(NumberValue numberValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(numberValue.getName(), posX, posY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        final float sliderWidth = MathHelper.floor_double(((numberValue.getValue()).floatValue() - numberValue.getMin()) / (numberValue.getMax() - numberValue.getMin()) * (getWidth() - font.getWidth(getLabel()) - 10));
        font.drawStringWithShadow(getLabel(), getPosX(), getPosY() + 3, new Color(229, 229, 223, 255).getRGB());
        RenderUtils.drawRoundedRect(getPosX() + font.getWidth(getLabel()) + 4, getPosY() + 5, getWidth() - font.getWidth(getLabel()) - 10, 2, 2, new Color(55, 55, 55, 255).getRGB());
        RenderUtils.drawRoundedRect(getPosX() + font.getWidth(getLabel()) + 4, getPosY() + 5, sliderWidth, 2, 2, new Color(0xff689FFF).getRGB());
        RenderUtils.drawCircle(getPosX() + font.getWidth(getLabel()) + sliderWidth + 3,getPosY() + 4f,4,new Color(0xff689FFF).getRGB());
        font2.drawStringWithShadow(String.valueOf(numberValue.getValue()),getPosX() + font.getWidth(getLabel()) + sliderWidth + 3,getPosY(),new Color(229, 229, 223, 255).getRGB());
        if (sliding) {
            numberValue.setValueAutoSave(round(((mouseX - (getPosX() + font.getWidth(getLabel()) + 4)) * (numberValue.getMax() - numberValue.getMin()) / ((getWidth() - font.getWidth(getLabel()) - 10)) + numberValue.getMin()), 2));
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + font.getWidth(getLabel()) + 2, getPosY() + 5, getWidth() - font.getWidth(getLabel()) - 8, 2);
        if (button == 0) {
            if (hovered) {
                sliding = true;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && sliding) sliding = false;
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
    }

    private double round(final double val, final int places) {
        final double v = Math.round(val / (numberValue.getMax() - numberValue.getMin())) * (numberValue.getMax() - numberValue.getMin());
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public NumberValue getNumberValue() {
        return numberValue;
    }
}
