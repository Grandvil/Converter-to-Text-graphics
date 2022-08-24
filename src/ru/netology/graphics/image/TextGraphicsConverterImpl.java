package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private TextColorSchema schema = new TextColorSchemaImpl();
    private double maxRatio;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        checkRatio(img);

        int k;
        if (img.getWidth() > img.getHeight()) {
            if (maxWidth > maxHeight) {
                k = img.getWidth() / maxWidth;
            } else {
                k = img.getWidth() / maxHeight;
            }
        } else {
            if (maxWidth > maxHeight) {
                k = img.getHeight() / maxWidth;
            } else {
                k = img.getHeight() / maxHeight;
            }
        }

        int newWidth = checkWidth(k, img);
        int newHeight = checkHeight(k, img);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        char[][] array = new char[newHeight][newWidth];
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                array[h][w] = c;
            }
        }

        return collectingCharacters(newWidth, newHeight, array);
    }

    public double checkRatio(BufferedImage img) throws BadImageSizeException {
        double ratio;
        if (img.getWidth() > img.getHeight()) {
            ratio = (double) img.getWidth() / img.getHeight();
        } else {
            ratio = (double) img.getHeight() / img.getWidth();
        }

        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }
        return ratio;
    }

    public int checkWidth(int k, BufferedImage img) {
        int newWidth;
        if (img.getWidth() > maxWidth) {
            newWidth = img.getWidth() / k;
        } else {
            newWidth = img.getWidth();
        }
        return newWidth;
    }

    public int checkHeight(int k, BufferedImage img) {
        int newHeight;
        if (img.getHeight() > maxHeight) {
            newHeight = img.getHeight() / k;
        } else {
            newHeight = img.getHeight();
        }
        return newHeight;
    }

    public String collectingCharacters(int newWidth, int newHeight, char[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                sb.append(array[h][w]);
                sb.append(array[h][w]);
                sb.append(array[h][w]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
