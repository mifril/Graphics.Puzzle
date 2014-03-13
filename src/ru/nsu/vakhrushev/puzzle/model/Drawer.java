package ru.nsu.vakhrushev.puzzle.model;

import ru.nsu.vakhrushev.puzzle.model.util.Filter;

import java.awt.image.BufferedImage;

/**
 * Created by Maxim Vakhrushev on 25.02.14 3:04.
 */

public class Drawer {
    public long drawLine(BufferedImage image, int startX, int startY, int endX, int endY) {
        int fixingDeltaX = 0;
        int fixingDeltaY = 0;
        int fixingDeltaError = 0;

        int deltaError = 0;
        int deltaX = endX - startX;
        int deltaY = endY - startY;
        int signX = (int)Math.signum(deltaX);
        int signY = (int)Math.signum(deltaY);
        deltaX = Math.abs(deltaX);
        deltaY = Math.abs(deltaY);

        if (deltaX > deltaY) {
	        fixingDeltaX = signX;
            fixingDeltaY = 0;
            fixingDeltaError = deltaY;
            deltaError = deltaX;
        } else {
            fixingDeltaX = 0;
            fixingDeltaY = signY;
            fixingDeltaError = deltaX;
            deltaError = deltaY;
        }

        int currX = startX;
        int currY = startY;
        int error = deltaError/2;
        long pixelsDrew = 1;

        image.setRGB(currX, currY, 0);

        for (int i = 1; i < deltaError; ++i) {
            error -= fixingDeltaError;
            if (error < 0) {
                error += deltaError;
                currX += signX;
                currY += signY;
            } else {
                currX += fixingDeltaX;
                currY += fixingDeltaY;
            }
            image.setRGB(currX, currY, 0);
            ++pixelsDrew;
        }
        return pixelsDrew;
    }

    public long drawTriangleBorders(Triangle triangle, BufferedImage image) {
        double angle = Math.toRadians(triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        int x1 = (int)(Math.cos(angle) * (triangle.getFirstVertexX() - centerX) - Math.sin(angle) * (triangle.getFirstVertexY()  - centerY)) + centerX;
        int x2 = (int)(Math.cos(angle) * (triangle.getSecondVertexX() - centerX)- Math.sin(angle) * (triangle.getSecondVertexY() - centerY)) + centerX;
        int x3 = (int)(Math.cos(angle) * (triangle.getThirdVertexX() - centerX) - Math.sin(angle) * (triangle.getThirdVertexY() - centerY)) + centerX;

        int y1 = (int)(Math.sin(angle) * (triangle.getFirstVertexX() - centerX) + Math.cos(angle) * (triangle.getFirstVertexY() - centerY)) + centerY;
        int y2 = (int)(Math.sin(angle) * (triangle.getSecondVertexX() - centerX) + Math.cos(angle) * (triangle.getSecondVertexY() - centerY)) + centerY;
        int y3 = (int)(Math.sin(angle) * (triangle.getThirdVertexX() - centerX) + Math.cos(angle) * (triangle.getThirdVertexY() - centerY)) + centerY;

        long bordersPixels = 0;
        bordersPixels += drawLine(image, x1, y1, x2, y2);
        bordersPixels += drawLine(image, x1, y1, x3, y3);
        bordersPixels += drawLine(image, x3, y3, x2, y2);
        return bordersPixels;
    }

    public void drawTriangle(Triangle triangle, Model model) {
        BufferedImage image = model.getImage();
        double angle = Math.toRadians(triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        int x1 = (int)(Math.cos(angle) * (triangle.getFirstVertexX() - centerX) -
                        Math.sin(angle) * (triangle.getFirstVertexY()  - centerY)) + centerX;
        int x2 = (int)(Math.cos(angle) * (triangle.getSecondVertexX() - centerX)-
                        Math.sin(angle) * (triangle.getSecondVertexY() - centerY)) + centerX;
        int x3 = (int)(Math.cos(angle) * (triangle.getThirdVertexX() - centerX) -
                        Math.sin(angle) * (triangle.getThirdVertexY() - centerY)) + centerX;

        int y1 = (int)(Math.sin(angle) * (triangle.getFirstVertexX() - centerX) +
                        Math.cos(angle) * (triangle.getFirstVertexY() - centerY)) + centerY;
        int y2 = (int)(Math.sin(angle) * (triangle.getSecondVertexX() - centerX) +
                        Math.cos(angle) * (triangle.getSecondVertexY() - centerY)) + centerY;
        int y3 = (int)(Math.sin(angle) * (triangle.getThirdVertexX() - centerX) +
                        Math.cos(angle) * (triangle.getThirdVertexY() - centerY)) + centerY;


        if (y2 < y1) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
            tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y3 < y1) {
            int tmp = y1;
            y1 = y3;
            y3 = tmp;
            tmp = x1;
            x1 = x3;
            x3 = tmp;
        }
        if (y2 > y3) {
            int tmp = y2;
            y2 = y3;
            y3 = tmp;
            tmp = x2;
            x2 = x3;
            x3 = tmp;
        }
        double deltaX13 = 0;
        double deltaX12 = 0;
        double deltaX23 = 0;

        if (y3 != y1) {
            deltaX13 = (x3 - x1 + 0.) / (y3 - y1);
        }
        if (y2 != y1) {
            deltaX12 = (x2 - x1+ 0.) / (y2 - y1);
        }
        if (y3 != y2) {
            deltaX23 = (x3 - x2+ 0.) / (y3 - y2);
        }

        double currStartX = x1;
        double currEndX = currStartX;
        double copyDeltaX13 = deltaX13;

        if (deltaX13 > deltaX12) {
            double tmp = deltaX12;
            deltaX12 = deltaX13;
            deltaX13 = tmp;
        }

        if (y1 == y2){
            currStartX = x1;
            currEndX = x2;
            if (currEndX < currStartX) {
                double tmp = currStartX;
                currStartX = currEndX;
                currEndX = tmp;
            }
        }

        long totalPixels = 0;
        long clearPixels = 0;

        for (int currY = y1; currY < y2; ++currY){
            for (int currX = (int)Math.round(currStartX); currX <= (int)Math.round(currEndX); ++currX){
                if (setPixel(triangle, model, currX, currY) == Model.BACKGROUND_COLOR) {
                    ++clearPixels;
                }
                ++totalPixels;
            }
            currStartX += deltaX13;
            currEndX += deltaX12;
        }

        if (copyDeltaX13 < deltaX23) {
            double tmp = deltaX23;
            deltaX23 = copyDeltaX13;
            copyDeltaX13 = tmp;
        }
        for (int currY = y2; currY <= y3; ++currY){
            for (int currX = (int)Math.round(currStartX); currX <= (int)Math.round(currEndX); ++currX){
                if (setPixel(triangle, model, currX, currY) == Model.BACKGROUND_COLOR) {
                    ++clearPixels;
                }
                ++totalPixels;
            }
            currStartX += copyDeltaX13;
            currEndX += deltaX23;
        }
        long borderPixels = drawTriangleBorders(triangle, image);

        triangle.setBorderPixels(borderPixels);
        triangle.setClearPixels(clearPixels);
        triangle.setTotalPixels(totalPixels);
    }

    /*
     * @return int set pixel
     */
    private int setPixel(Triangle triangle, Model model, int x, int y) {
        BufferedImage readImage = model.getReadImage();
        BufferedImage image = model.getImage();

        int pixel = getPixelByCoordinates(triangle, readImage, x, y, model.isNeedBilinearFiltering());
        if (model.isNeedAlphaBlend()) {
            pixel = Filter.getAlphaBlendedPixel(pixel);
        } else {
            pixel &= (~Filter.ARGB_ALPHA_RANGE);
        }
        if (x < image.getWidth() && y < image.getHeight()) {
            image.setRGB(x, y, pixel);
        }
        return pixel;
    }

    private int getPixelByCoordinates(Triangle triangle, BufferedImage readImage, int x, int y, boolean needFiltering) {
        int readImageWidth = readImage.getWidth();
        int readImageHeight = readImage.getHeight();
        double angle = Math.toRadians(-triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        double xInCenterCoordinates = (Math.cos(angle) *  (x - centerX) - Math.sin(angle) * (y - centerY));
        double yInCenterCoordinates = (Math.sin(angle) * (x - centerX) + Math.cos(angle) * (y - centerY));

        double u = (yInCenterCoordinates + triangle.getCenterU() * readImageWidth);
        double v = (xInCenterCoordinates + triangle.getCenterV() * readImageHeight);

        if (u > readImageWidth - 1) {
            u = readImageWidth - 1;
        } else if (u < 0) {
            u = 0;
        }
        if (v > readImageHeight - 1) {
            v = readImageHeight - 1;
        } else if (v < 0) {
            v = 0;
        }

        int pixel = 0;
        if (needFiltering) {
            pixel = Filter.getBilinearFilteredPixel(readImage, v, u);
        } else {
            pixel = readImage.getRGB((int)v, (int)u);
        }
       return pixel;
    }

    public void fillBackground(BufferedImage image, int background) {
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                image.setRGB(x, y, background);
            }
        }
    }
}
