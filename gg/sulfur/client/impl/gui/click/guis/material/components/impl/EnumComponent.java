package gg.sulfur.client.impl.gui.click.guis.material.components.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.gui.click.guis.material.components.Component;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.impl.utils.MouseUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class EnumComponent extends Component {
    private EnumValue enumValue;
    private boolean extended;
    final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
    final CustomFontRenderer font2 = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();

    public EnumComponent(EnumValue enumValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(enumValue.getName(), posX, posY, offsetX, offsetY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        font.drawStringWithShadow(getLabel(), getPosX(), getPosY() + 3, new Color(229, 229, 223, 255).getRGB());
        if (isExtended()) {
            setHeight(20 + (15 * (enumValue.getValues().size() - 1)));
            RenderUtils.drawRoundedRect(getPosX() + getWidth() - 80, getPosY() + 13.5f, 72, (15 * (enumValue.getValues().size() - 1)), 3, new Color(55, 55, 55, 255).getRGB());
            RenderUtils.drawOutlinedRoundedRect(getPosX() + getWidth() - 80, getPosY() + 12.5f, 72, 1 + (15 * (enumValue.getValues().size() - 1)), 3, 0.5f, new Color(0xff689FFF).getRGB());
            float enumY = getPosY() + 20f;
            for (Object enoom : enumValue.getValues()) {
                if (enoom instanceof INameable && enoom instanceof Enum) {
                    if (enoom != enumValue.getValue()) {
                        font.drawStringWithShadow(StringUtils.capitalize(((INameable) enoom).getDisplayName().toLowerCase()), getPosX() + getWidth() - 44 - font.getWidth(StringUtils.capitalize(((INameable) enoom).getDisplayName().toLowerCase())) / 2, enumY, new Color(229, 229, 223, 255).getRGB());
                        enumY += 15;
                    }
                }
            }
        } else {
            setHeight(20);
        }
        RenderUtils.drawRoundedRect(getPosX() + getWidth() - 84, getPosY() - 1.5f, 80, 15, 3, new Color(50, 50, 50, 255).getRGB());
        RenderUtils.drawOutlinedRoundedRect(getPosX() + getWidth() - 84, getPosY() - 1.5f, 80, 15, 3, 0.5f, new Color(0xff689FFF).getRGB());
        font.drawStringWithShadow(StringUtils.capitalize(enumValue.getValue().toString().toLowerCase()), getPosX() + getWidth() - 82, getPosY() + 4, new Color(229, 229, 223, 255).getRGB());
        RenderUtils.drawArrow(getPosX() + getWidth() - 14, getPosY() + 4, isExtended(), new Color(229, 229, 223, 255).getRGB());
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + getWidth() - 84, getPosY() - 1.5f, 80, 15)) {
            setExtended(!isExtended());
        }
        if (button == 0 && isExtended()) {
            float enumY = getPosY() + 20f;
            for (Object enoom : enumValue.getValues()) {
                if (enoom instanceof INameable && enoom instanceof Enum) {
                    if (enoom != enumValue.getValue()) {
                        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + getWidth() - 80, enumY - 4, 72, 15)) {
                            enumValue.setValue(enoom);
                            setExtended(false);
                        }
                        enumY += 15;
                    }
                }
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public EnumValue getEnumValue() {
        return enumValue;
    }
}
