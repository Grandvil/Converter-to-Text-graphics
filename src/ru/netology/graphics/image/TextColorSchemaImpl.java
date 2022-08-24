package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    private static final String DENSITY = "#$@%*+-'";
    private static final double CALCULATION = 1.0 / 255 * (DENSITY.length() - 1);

    @Override
    public char convert(int color) {
        int charColor = (int) Math.round(color * CALCULATION);
        return DENSITY.charAt(charColor);
    }
}
