package gg.sulfur.client.api.module.enums;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum ModuleCategory {
    COMBAT('a'), MOVEMENT('b'), PLAYER('c'), MISC('e'), RENDER('g'), EXPLOIT('d'), HIDDEN(' ');

    final char catChar;

    ModuleCategory(char catChar) {
        this.catChar = catChar;
    }

    public String getName() {
        return StringUtils.capitalize(StringUtils.lowerCase(name()));
    }

    public String getChar() {
        return "" + catChar;
    }

    public static List<ModuleCategory> getAllReversed() {
        return Lists.reverse(Arrays.asList(ModuleCategory.values()));
    }
}
