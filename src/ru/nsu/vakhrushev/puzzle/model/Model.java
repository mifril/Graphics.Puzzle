package ru.nsu.vakhrushev.puzzle.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */
public class Model {
    public final static int BACKGROUND_COLOR = 0x00FFFFFF;

    private BufferedImage image;
    private BufferedImage readImage;
    private int readImageWidth;
    private int readImageHeight;
    private int imageWidth;
    private int imageHeight;
    private int widthOffset;
    private int heightOffset;
    private int pixels[];
    private Triangle [] triangles;
    private Drawer drawer;
    private boolean needAlphaBlend = false;
    private boolean needBilinearFiltering = false;

    public Model (String imageFileName) {
        try {
            assert (imageFileName != null);
            readImage = ImageIO.read(new File(imageFileName));
            readImageWidth = readImage.getWidth();
            readImageHeight = readImage.getHeight();
            imageWidth = (readImageWidth / 4) * 9;
            imageHeight = (readImageHeight / 4) * 9;
            widthOffset = (imageWidth / 9) * 2;
            heightOffset = (imageHeight / 9) * 2;

            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            pixels = new int [imageWidth * imageHeight];
            triangles = new Triangle[32];
            drawer = new Drawer();

            setTriangles();
            paintTrianglesInImage();

        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public void paintTrianglesInImage() {
        drawer.fillBackground(image, BACKGROUND_COLOR);
        for(Triangle triangle : triangles) {
            drawer.drawTriangle(triangle, readImage, image, needAlphaBlend);
        }
    }

    public void initTrianglesFinishPositions() {
        for (Triangle triangle : triangles) {
            triangle.initFinishPosition();
        }
    }

    public void moveTriangles(int rotateDirection) {
        for (Triangle triangle : triangles) {
            triangle.moveTriangle(rotateDirection);
        }
    }

    public void forceMove(int position) {
        for (Triangle triangle : triangles) {
            triangle.moveTriangleForce(position);
        }
    }

    public void resetNeedBlend() {
        this.needAlphaBlend = !this.needAlphaBlend;
    }

    public void resetNeedBilinearFiltering() {
        this.needBilinearFiltering = !this.needBilinearFiltering;
    }

    public boolean isNeedBilinearFiltering() {
        return needBilinearFiltering;
    }

    public boolean isNeedAlphaBlend() {
        return needAlphaBlend;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int[] getPixels() {
        return pixels;
    }

    private void setTriangles() {
        int heightAddition = readImageHeight / 4;
        int widthAddition = readImageWidth / 4;
        double [] pointsU = new double[4];
        double [] pointsV = new double[4];
        double [] pointsX = new double[4];
        double [] pointsY = new double[4];
        int trianglesCount = 0;
        for (int y = 0; y < readImageHeight; y += heightAddition) {
            double curU = (double)y /readImageHeight;
            /*
             * triangles ***
             *           **
             *           *
             */
            for (int x = 0; x < readImageWidth; x += widthAddition) {
                double curV = (double)x / readImageWidth;
//                System.err.println("1. curU = " + curU + ", curV = " + curV);

                pointsU[0] = curU + 0.25 / 3; //center U
                pointsU[1] = curU;            //vertexes U
                pointsU[2] = curU + 0.25;
                pointsU[3] = curU;

                pointsV[0] = curV + 0.25 / 3; //center V
                pointsV[1] = curV;            //vertexes V
                pointsV[2] = curV;
                pointsV[3] = curV + 0.25;

                pointsX[0] = heightOffset + x + heightAddition / 3; //center X
                pointsX[1] = heightOffset + x;                      //vertexes X
                pointsX[2] = heightOffset + x;
                pointsX[3] = heightOffset + x + heightAddition;

                pointsY[0] = widthOffset + y + heightAddition / 3;  //center Y
                pointsY[1] = widthOffset + y;                       //vertexes Y
                pointsY[2] = widthOffset + y + widthAddition;
                pointsY[3] = widthOffset + y;
 //               System.err.println("1. Expected center U = " + pointsU[0]);
 //               System.err.println("1. Expected center V = " + pointsV[0]);
                triangles[trianglesCount++] = new Triangle(pointsU, pointsV, pointsX, pointsY, 0);
            }
            /*
             * triangles  *
             *           **
             *          ***
             */
            for (int x = 0; x < readImageWidth; x += widthAddition) {
                double curV = (double)x / readImageWidth;

                pointsU[0] = curU + 0.5 / 3;  //center U
                pointsU[1] = curU + 0.25;     //vertexes U
                pointsU[2] = curU;
                pointsU[3] = curU + 0.25;

                pointsV[0] = curV + 0.5 / 3;  //center V
                pointsV[1] = curV;            //vertexes V
                pointsV[2] = curV + 0.25;
                pointsV[3] = curV + 0.25;

                pointsX[0] = heightOffset + x + heightAddition * 2 / 3; //center X
                pointsX[1] = heightOffset + x + heightAddition;         //vertexes X
                pointsX[2] = heightOffset + x;
                pointsX[3] = heightOffset + x + heightAddition;

                pointsY[0] = widthOffset + y + heightAddition * 2 / 3;  //center Y
                pointsY[1] = widthOffset + y;                           //vertexes Y
                pointsY[2] = widthOffset + y + widthAddition;
                pointsY[3] = widthOffset + y + widthAddition;
//                System.err.println("2. Expected center U = " + pointsU[0]);
//                System.err.println("2. Expected center V = " + pointsV[0]);

                triangles[trianglesCount++] = new Triangle(pointsU, pointsV, pointsX, pointsY, 0);
            }
        }
    }
}
