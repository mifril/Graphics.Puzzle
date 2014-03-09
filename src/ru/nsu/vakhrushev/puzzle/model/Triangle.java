package ru.nsu.vakhrushev.puzzle.model;

import ru.nsu.vakhrushev.puzzle.view.InterfacePanel;

import java.util.Random;

/**
 * Created by Maxim Vakhrushev on 25.02.14 0:24.
 */
public class Triangle {
    public static final int MAX_ANGLE = 360;

    private Random random;

    private int imageWidth;
    private int imageHeight;

    private int [] vertexesX;
    private int [] vertexesY;
    private int curCenterX;
    private int curCenterY;

    private int startCenterX;
    private int startCenterY;
    private int [] startVertexesX;
    private int [] startVertexesY;

    private double deltaX;
    private double deltaY;

    private double [] vertexesU;
    private double [] vertexesV;
    private double centerU;
    private double centerV;

    private double curRotateAngle;
    private double deltaAngle;

    private long totalPixels;
    private long clearPixels;
    private long borderPixels;

    public Triangle (double [] pointsU, double [] pointsV, double [] pointsX, double [] pointsY, double rotateAngle, int imageWidth, int imageHeight, Random random) {
        this.vertexesX = new int[3];
        this.vertexesY = new int[3];
        this.startVertexesX = new int[3];
        this.startVertexesY = new int[3];
        this.vertexesU = new double[3];
        this.vertexesV = new double[3];

        this.curCenterX = (int)pointsX[0];
        this.curCenterY = (int)pointsY[0];
        this.startCenterX = this.curCenterX;
        this.startCenterY = this.curCenterY;

        for (int i = 1; i < pointsX.length; ++i) {
            this.vertexesX[i - 1] = (int)pointsX[i];
        }
        for (int i = 1; i < pointsY.length; ++i) {
            this.vertexesY[i - 1] = (int)pointsY[i];
        }

        this.centerU = pointsU[0];
        this.centerV = pointsV[0];
        System.arraycopy(pointsU, 1, this.vertexesU, 0, pointsU.length - 1);
        System.arraycopy(pointsV, 1, this.vertexesV, 0, pointsV.length - 1);
        this.curRotateAngle = rotateAngle;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.random = random;

        System.arraycopy(this.vertexesX, 0, this.startVertexesX, 0, this.vertexesX.length);
        System.arraycopy(this.vertexesY, 0, this.startVertexesY, 0, this.vertexesY.length);
    }

    public void initFinishPosition() {
//        System.err.println("IMAGE SIZE: " + imageWidth + ", " + imageHeight);
        double finishRotateAngle = random.nextInt(MAX_ANGLE);
        deltaAngle = finishRotateAngle / 180;

        int finishMinCenterX = curCenterX;
        int finishMaxCenterX = curCenterX;
        int finishMinCenterY = curCenterY;
        int finishMaxCenterY = curCenterY;


        int positionNorth = Math.abs(random.nextInt() % 3);
        int positionWest = Math.abs(random.nextInt() % 3);

        finishMinCenterY = imageWidth / 6 + positionNorth * imageWidth / 6;
        finishMaxCenterY = imageWidth / 4 + positionNorth * imageWidth / 6;
        finishMinCenterX = imageHeight / 6 + positionWest * imageWidth / 6;
        finishMaxCenterX = imageHeight / 4 + positionWest * imageWidth / 6;

//        System.err.println("Max x: " + finishMinCenterX + ", " + finishMaxCenterX);


/*
        if (positionNorth == 1) {
            finishMinCenterY = imageWidth / 6;
            finishMaxCenterY = imageWidth / 5;
        } else {
            finishMinCenterY = imageWidth - imageWidth / 5;
            finishMaxCenterY = imageWidth - imageWidth / 6;
        }
        if (positionWest == 1) {
            finishMinCenterX = imageHeight / 6;
            finishMaxCenterX = imageHeight / 5;
        } else {
            finishMinCenterX = imageWidth - imageHeight / 5;
            finishMaxCenterX = imageHeight - imageHeight / 6;
        }
*/
        int finishCenterX = random.nextInt(finishMaxCenterX - finishMinCenterX + 1) + finishMinCenterX;
        int finishCenterY = random.nextInt(finishMaxCenterY - finishMinCenterY + 1) + finishMinCenterY;

//        System.err.println(finishCenterX + ", " + finishCenterY);

        deltaX = (finishCenterX - curCenterX) / 180.0;
        deltaY = (finishCenterY - curCenterY) / 180.0;
//        System.err.println(deltaX + ", " + deltaY);

    }

    public void moveTriangle(int position) {
        if (position <= InterfacePanel.MAX_SLIDER_VALUE / 2) {
            curRotateAngle = position * deltaAngle;
            curCenterX = (int)(Math.round(startCenterX + position * deltaX));
            curCenterY = (int)(Math.round(startCenterY + position * deltaY));
            for (int i = 0; i < vertexesX.length; ++i) {
                vertexesX[i] = (int)(Math.round(startVertexesX[i] + position * deltaX));
                vertexesY[i] = (int)(Math.round(startVertexesY[i] + position * deltaY));
            }
            //System.err.println("1. Position = " + position + ". Changed: " + curCenterX + ", " + curCenterY);
        } else {
            curRotateAngle = (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaAngle;
            curCenterX = (int)(Math.round(startCenterX + (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaX));
            curCenterY = (int)(Math.round(startCenterY + (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaY));
            for (int i = 0; i < vertexesX.length; ++i) {
                vertexesX[i] = (int)(Math.round(startVertexesX[i] + (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaX));
                vertexesY[i] = (int)(Math.round(startVertexesY[i] + (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaY));
            }
            //System.err.println("2. Position = " + position + ". Changed: " + curCenterX + ", " + curCenterY);
        }
    }

    public boolean containsPoint(int x, int y) {
        int x1 = vertexesX[0];
        int y1 = vertexesY[0];
        int x2 = vertexesX[1];
        int y2 = vertexesY[1];
        int x3 = vertexesX[2];
        int y3 = vertexesY[2];

        int mul1 = (x1 - x) * (y2 - y1) - (x2 - x1) * (y1 - y);
        int mul2 = (x2 - x) * (y3 - y2) - (x3 - x2) * (y2 - y);
        int mul3 = (x3 - x) * (y1 - y3) - (x1 - x3) * (y3 - y);
        return (Math.signum(mul1) == Math.signum(mul2) && Math.signum(mul2) == Math.signum(mul3));
    }

    public int getCurCenterX() {
        return curCenterX;
    }

    public int getCurCenterY() {
        return curCenterY;
    }

    public double getCenterU() {
        return centerU;
    }

    public double getCenterV() {
        return centerV;
    }

    public double getCurRotateAngle() {
        return curRotateAngle;
    }

    public void incrementClearPixels() {
        ++clearPixels;
    }

    public void setBorderPixels(long borderPixels) {
        this.borderPixels = borderPixels;
    }

    public void setTotalPixels(long totalPixels) {
        this.totalPixels = totalPixels;
    }

    public void setClearPixels(long clearPixels) {
        this.clearPixels = clearPixels;
    }

    public long getBorderPixels() {
        return borderPixels;
    }

    public long getTotalPixels() {
        return totalPixels;
    }

    public long getClearPixels() {
        return clearPixels;
    }

    public int getFirstVertexX() {
        return vertexesX[0];
    }

    public int getSecondVertexX() {
        return vertexesX[1];
    }

    public int getThirdVertexX() {
        return vertexesX[2];
    }

    public int getFirstVertexY() {
        return vertexesY[0];
    }

    public int getSecondVertexY() {
        return vertexesY[1];
    }

    public int getThirdVertexY() {
        return vertexesY[2];
    }
}
