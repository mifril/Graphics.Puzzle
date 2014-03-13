package ru.nsu.vakhrushev.puzzle.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

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
    private Triangle [] triangles;
    private Triangle triangleUnderCursor;
    private Drawer drawer;
    private boolean needAlphaBlend = false;
    private boolean needBilinearFiltering = false;
    private Random random;

    public Model (String imageFileName) {
        try {
            assert (imageFileName != null);
            random = new Random( new Date().getTime() );

            readImage = ImageIO.read(new File(imageFileName));
            readImageWidth = readImage.getWidth();
            readImageHeight = readImage.getHeight();
            imageWidth = (readImageWidth / 4) * 9;
            imageHeight = (readImageHeight / 4) * 9;
            widthOffset = (imageWidth / 9) * 2;
            heightOffset = (imageHeight / 9) * 2;

            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
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
            drawer.drawTriangle(triangle, this);
        }
    }

    public void initTrianglesFinishPositions() {
        for (Triangle triangle : triangles) {
            triangle.initFinishPosition();
        }
    }

    public void moveTriangles(int position) {
        for (Triangle triangle : triangles) {
            triangle.moveTriangle(position);
        }
    }

    public void findTriangleUnderCursor(int x, int y) {
        for (Triangle triangle : triangles) {
            if (triangle.containsPoint(x, y)) {
                triangleUnderCursor = triangle;
                return;
            }
        }
        triangleUnderCursor = null;
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

    public BufferedImage getReadImage() {
        return readImage;
    }

    public Triangle getTriangleUnderCursor() {
        return triangleUnderCursor;
    }

    private void setTriangles() {
        int heightAddition = readImageHeight / 4;
        int widthAddition = readImageWidth / 4;
        double [] pointsX = new double[4];
        double [] pointsY = new double[4];
        double centerU = 0;
        double centerV = 0;
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

                centerU = curU + 0.25 / 3;
                centerV = curV + 0.25 / 3;

                pointsX[0] = heightOffset + x + heightAddition / 3; //center X
                pointsX[1] = heightOffset + x;                      //vertexes X
                pointsX[2] = heightOffset + x;
                pointsX[3] = heightOffset + x + heightAddition;

                pointsY[0] = widthOffset + y + heightAddition / 3;  //center Y
                pointsY[1] = widthOffset + y;                       //vertexes Y
                pointsY[2] = widthOffset + y + widthAddition;
                pointsY[3] = widthOffset + y;
                triangles[trianglesCount++] = new Triangle(centerU, centerV, pointsX, pointsY, 0, imageWidth, imageHeight, random);
            }
            /*
             * triangles  *
             *           **
             *          ***
             */
            for (int x = 0; x < readImageWidth; x += widthAddition) {
                double curV = (double)x / readImageWidth;

                centerU = curU + 0.5 / 3;
                centerV = curV + 0.5 / 3;

                pointsX[0] = heightOffset + x + heightAddition * 2 / 3; //center X
                pointsX[1] = heightOffset + x + heightAddition;         //vertexes X
                pointsX[2] = heightOffset + x;
                pointsX[3] = heightOffset + x + heightAddition;

                pointsY[0] = widthOffset + y + heightAddition * 2 / 3;  //center Y
                pointsY[1] = widthOffset + y;                           //vertexes Y
                pointsY[2] = widthOffset + y + widthAddition;
                pointsY[3] = widthOffset + y + widthAddition;

                triangles[trianglesCount++] = new Triangle(centerU, centerV, pointsX, pointsY, 0, imageWidth, imageHeight, random);
            }
        }
    }
}
