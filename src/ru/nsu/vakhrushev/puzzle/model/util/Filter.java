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

    public static void useBilinearFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int [] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        for (int y = 1; y < height - 1; ++y) {
            for (int x = 1; x < width - 1; ++x) {
                double factor1 = (1 - 0.5) * (1 - 0.5);
                double factor2 = 0.5 * (1 - 0.5);
                double factor3 = (1 - 0.5) * 0.5;
                double factor4 = 0.5 * 0.5;
                int pixelNW = pixels[(y - 1) * width + x - 1];
                int pixelNE = pixels[(y - 1) * width + x + 1];
                int pixelSW = pixels[(y + 1) * width + x - 1];
                int pixelSE = pixels[(y + 1) * width + x + 1];
                Color colorNW = new Color(pixelNW);
                Color colorNE = new Color(pixelNE);
                Color colorSW = new Color(pixelSW);
                Color colorSE = new Color(pixelSE);
                int red = (int) (colorNW.getRed() * factor1 + colorNE.getRed() * factor2 + colorSW.getRed() * factor3 + colorSE.getRed() * factor4);
                int green = (int) (colorNW.getGreen() * factor1 + colorNE.getGreen() * factor2 + colorSW.getGreen() * factor3 + colorSE.getGreen() * factor4);
                int blue = (int) (colorNW.getBlue() * factor1 + colorNE.getBlue() * factor2 + colorSW.getBlue() * factor3 + colorSE.getBlue() * factor4);
                pixels[y * width + x] = (red << 16) |  (green << 8) | (blue);
/*
                int red = (pixelSW & RGB_RED_RANGE) * factor1 + (pixelNW & RGB_RED_RANGE) * factor2 +
                            (pixelSE & RGB_RED_RANGE) * factor3 + (pixelNE & RGB_RED_RANGE) * factor4;
                int green = (pixelSW & RGB_GREEN_RANGE) * factor1 + (pixelNW & RGB_GREEN_RANGE) * factor2 +
                            (pixelSE & RGB_GREEN_RANGE) * factor3 + (pixelNE & RGB_GREEN_RANGE) * factor4;
                int blue = (pixelSW & RGB_BLUE_RANGE) * factor1 + (pixelNW & RGB_BLUE_RANGE) * factor2 +
                            (pixelSE & RGB_BLUE_RANGE) * factor3 + (pixelNE & RGB_BLUE_RANGE) * factor4;
                pixels[y * width + x] = (red << 16) |  (green << 8) | (blue);
*/
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
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
