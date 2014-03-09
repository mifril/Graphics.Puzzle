package ru.nsu.vakhrushev.puzzle.model;

import ru.nsu.vakhrushev.puzzle.model.util.Filter;

import java.awt.image.BufferedImage;

/**
 * Created by Maxim Vakhrushev on 25.02.14 3:04.
 */

public class Drawer {
    public void drawLine(BufferedImage image, int startX, int startY, int endX, int endY) {
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
        }
    }

    public void drawTriangleBorders(Triangle triangle, BufferedImage image) {
        double angle = Math.toRadians(triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        int x1 = (int)(Math.cos(angle) * (triangle.getFirstVertexX() - centerX) - Math.sin(angle) * (triangle.getFirstVertexY()  - centerY)) + centerX;
        int x2 = (int)(Math.cos(angle) * (triangle.getSecondVertexX() - centerX)- Math.sin(angle) * (triangle.getSecondVertexY() - centerY)) + centerX;
        int x3 = (int)(Math.cos(angle) * (triangle.getThirdVertexX() - centerX) - Math.sin(angle) * (triangle.getThirdVertexY() - centerY)) + centerX;

        int y1 = (int)(Math.sin(angle) * (triangle.getFirstVertexX() - centerX) + Math.cos(angle) * (triangle.getFirstVertexY() - centerY)) + centerY;
        int y2 = (int)(Math.sin(angle) * (triangle.getSecondVertexX() - centerX) + Math.cos(angle) * (triangle.getSecondVertexY() - centerY)) + centerY;
        int y3 = (int)(Math.sin(angle) * (triangle.getThirdVertexX() - centerX) + Math.cos(angle) * (triangle.getThirdVertexY() - centerY)) + centerY;

        drawLine(image, x1, y1, x2, y2);
        drawLine(image, x1, y1, x3, y3);
        drawLine(image, x3, y3, x2, y2);

    }

    public void drawTriangle(Triangle triangle, BufferedImage readImage, BufferedImage image, boolean needBlend) {
        int color = 0;
        double angle = Math.toRadians(triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        int x1 = (int)(Math.cos(angle) * (triangle.getFirstVertexX() - centerX) - Math.sin(angle) * (triangle.getFirstVertexY()  - centerY)) + centerX;
        int x2 = (int)(Math.cos(angle) * (triangle.getSecondVertexX() - centerX)- Math.sin(angle) * (triangle.getSecondVertexY() - centerY)) + centerX;
        int x3 = (int)(Math.cos(angle) * (triangle.getThirdVertexX() - centerX) - Math.sin(angle) * (triangle.getThirdVertexY() - centerY)) + centerX;

        int y1 = (int)(Math.sin(angle) * (triangle.getFirstVertexX() - centerX) + Math.cos(angle) * (triangle.getFirstVertexY() - centerY)) + centerY;
        int y2 = (int)(Math.sin(angle) * (triangle.getSecondVertexX() - centerX) + Math.cos(angle) * (triangle.getSecondVertexY() - centerY)) + centerY;
        int y3 = (int)(Math.sin(angle) * (triangle.getThirdVertexX() - centerX) + Math.cos(angle) * (triangle.getThirdVertexY() - centerY)) + centerY;

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

        for (int currY = y1; currY < y2; ++currY){
            for (int currX = (int)Math.round(currStartX); currX <= (int)Math.round(currEndX); ++currX){
                if (needBlend) {
                    color = Filter.getAlphaBlendedPixel(getPixelByCoordinates(triangle, readImage, currX, currY));
                } else {
                    color = getPixelByCoordinates(triangle, readImage, currX, currY) & (~Filter.ARGB_ALPHA_RANGE);
                }
                image.setRGB(currX, currY, color);
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
                if (needBlend) {
                    color = Filter.getAlphaBlendedPixel(getPixelByCoordinates(triangle, readImage, currX, currY));
                } else {
                    color = getPixelByCoordinates(triangle, readImage, currX, currY) & (~Filter.ARGB_ALPHA_RANGE);
                }
                image.setRGB(currX, currY, color);
            }
            currStartX += copyDeltaX13;
            currEndX += deltaX23;
        }
        drawTriangleBorders(triangle, image);
    }

    int getPixelByCoordinates(Triangle triangle, BufferedImage readImage, int x, int y) {
        int uFactor = readImage.getWidth();
        int vFactor = readImage.getHeight();
        double angle = Math.toRadians(-triangle.getCurRotateAngle());
        int centerX = triangle.getCurCenterX();
        int centerY = triangle.getCurCenterY();

        int xInCenterCoordinates = (int)(Math.cos(angle) *  (x - centerX) - Math.sin(angle) * (y - centerY));
        int yInCenterCoordinates = (int)(Math.sin(angle) * (x - centerX) + Math.cos(angle) * (y - centerY));

        int u = (int)(yInCenterCoordinates + triangle.getCenterU() * uFactor);
        int v = (int)(xInCenterCoordinates + triangle.getCenterV() * vFactor);

        if (u > 127) {
            u = 127;
        } else if (u < 0) {
            u = 0;
        }
        if (v > 127) {
            v = 127;
        } else if (v < 0) {
            v = 0;
        }
        return readImage.getRGB(v, u);
    }

    void fillBackground(BufferedImage image, int background) {
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                image.setRGB(x, y, background);
            }
        }
    }
}
