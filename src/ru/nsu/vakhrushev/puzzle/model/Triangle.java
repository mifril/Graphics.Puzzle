package ru.nsu.vakhrushev.puzzle.model;

import ru.nsu.vakhrushev.puzzle.view.InterfacePanel;

/**
 * Created by Maxim Vakhrushev on 25.02.14 0:24.
 */
public class Triangle {
    public static final double MAX_ANGLE = 360;

    private int [] vertexesX;
    private int [] vertexesY;
    private int curCenterX;
    private int curCenterY;
    private int finishCenterX;
    private int finishCenterY;


    private double [] vertexesU;
    private double [] vertexesV;
    private double centerU;
    private double centerV;

    private double curRotateAngle;
    private double finishRotateAngle;
    private double deltaAngle;

    public Triangle (double [] pointsU, double [] pointsV, double [] pointsX, double [] pointsY, double rotateAngle) {
        this.vertexesX = new int[4];
        this.vertexesY = new int[4];
        this.vertexesU = new double[4];
        this.vertexesV = new double[4];

        this.curCenterX = (int)pointsX[0];
        this.curCenterY = (int)pointsY[0];

        for (int i = 1; i < pointsX.length; ++i) {
            this.vertexesX[i - 1] = (int)pointsX[i];
        }
        for (int i = 1; i < pointsY.length; ++i) {
            this.vertexesY[i - 1] = (int)pointsY[i];
        }
//        System.arraycopy(pointsX, 1, this.vertexesX, 0, pointsX.length - 1);
//        System.arraycopy(pointsY, 1, this.vertexesY, 0, pointsY.length - 1);
        this.centerU = pointsU[0];
        this.centerV = pointsV[0];
        System.arraycopy(pointsU, 1, this.vertexesU, 0, pointsU.length - 1);
        System.arraycopy(pointsV, 1, this.vertexesV, 0, pointsV.length - 1);
        this.curRotateAngle = rotateAngle;
//        System.err.println("Center U = " + centerU);
//        System.err.println("Center V = " + centerV);

    }

    public void initFinishPosition() {
        finishRotateAngle = Math.random() * 360;
        deltaAngle = finishRotateAngle / 180;
//        finishRotateAngle = 180 * 90;
//        deltaAngle = 90;
    }

    public void moveTriangle(int rotateDirection) {
        rotateTriangle(rotateDirection);
    }

    public void moveTriangleForce(int position) {
        if (position <= InterfacePanel.MAX_SLIDER_VALUE / 2) {
            curRotateAngle = position * deltaAngle;
        } else {
            curRotateAngle = (InterfacePanel.MAX_SLIDER_VALUE - position) * deltaAngle;
        }
    }

    private void rotateTriangle(int rotateDirection) {
        curRotateAngle += rotateDirection * deltaAngle;
//        System.err.println(curRotateAngle);
/*
        if (Math.abs(curRotateAngle - finishRotateAngle) < deltaAngle) {
            curRotateAngle = finishRotateAngle;
        } else if (Math.abs(curRotateAngle - 0) < deltaAngle) {
            curRotateAngle = 0;
        }
*/
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

    public double getFirstVertexU() {
        return vertexesU[0];
    }

    public double getSecondVertexU() {
        return vertexesU[1];
    }

    public double getThirdVertexU() {
        return vertexesU[2];
    }

    public double getFirstVertexV() {
        return vertexesV[0];
    }

    public double getSecondVertexV() {
        return vertexesV[1];
    }

    public double getThirdVertexV() {
        return vertexesV[2];
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
