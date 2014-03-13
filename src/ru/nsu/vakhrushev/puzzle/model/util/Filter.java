package ru.nsu.vakhrushev.puzzle.model.util;

import ru.nsu.vakhrushev.puzzle.model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Maxim Vakhrushev on 27.02.14 17:23.
 */
public class Filter {
    public static int ARGB_ALPHA_RANGE = 0xFF000000;
    public static int ARGB_RED_RANGE = 0x00FF0000;
    public static int ARGB_GREEN_RANGE = 0x0000FF00;
    public static int ARGB_BLUE_RANGE = 0x000000FF;
    public static int RGB_RED_RANGE = 0xFF0000;
    public static int RGB_GREEN_RANGE = 0x00FF00;
    public static int RGB_BLUE_RANGE = 0x0000FF;

    private static int ALPHA_FACTOR = 256;

    public static int getBilinearFilteredPixel(BufferedImage image, double x, double y) {
        int width = image.getWidth();
        int height = image.getHeight();

        double deltaX = x - Math.floor(x);
        double deltaY = y - Math.floor(y);

        int prevX = (int)x;
        int prevY = (int)y;

        if (prevX >= width) {
            prevX = width - 1;
        }
        if (prevY >= height) {
            prevY = height - 1;
        }
        if (prevX < 0) {
            prevX = 0;
        }
        if (prevY < 0) {
            prevY = 0;
        }

        int nextX = (prevX < width - 1) ? (prevX + 1) : (prevX);
        int nextY = (prevY < height - 1) ? (prevY + 1) : (prevY);

        double factor1 = (1 - deltaX) * (1 - deltaY);
        double factor2 = deltaX * (1 - deltaY);
        double factor3 = (1 - deltaX) * deltaY;
        double factor4 = deltaX * deltaY;
        int pixelNW = image.getRGB(prevX, prevY);
        int pixelNE = image.getRGB(nextX, prevY);
        int pixelSW = image.getRGB(prevX, nextY);
        int pixelSE = image.getRGB(nextX, nextY);
        int pixel = 0;

        double colorComponent = 0;
        for (int i = 0; i < 4; i++) {
            colorComponent = ((pixelNE >> i*8) & 0xFF) * factor1 + ((pixelNW >> i*8) & 0xFF) * factor2 +
                                ((pixelSE >> i*8) & 0xFF) * factor3 + ((pixelSW >> i*8) & 0xFF) * factor4;
            pixel |= ((int)colorComponent << i*8);
        }

        return pixel;
    }

    public static int getAlphaBlendedPixel(int pixel) {
        Color pixelColor = new Color(pixel, true);
        Color backgroundColor = new Color(Model.BACKGROUND_COLOR);
        int red = (int) (backgroundColor.getRed() + (pixelColor.getRed() - backgroundColor.getRed()) *
                                                        ((double)pixelColor.getAlpha() / ALPHA_FACTOR));
        int green = (int) (backgroundColor.getGreen() + (pixelColor.getGreen() - backgroundColor.getGreen()) *
                                                        ((double)pixelColor.getAlpha() / ALPHA_FACTOR));
        int blue = (int) (backgroundColor.getBlue() + (pixelColor.getBlue() - backgroundColor.getBlue()) *
                                                        ((double)pixelColor.getAlpha() / ALPHA_FACTOR));
        return (red << 16) |  (green << 8) | (blue);
    }
}
