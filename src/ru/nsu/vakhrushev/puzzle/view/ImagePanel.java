package ru.nsu.vakhrushev.puzzle.view;

import ru.nsu.vakhrushev.puzzle.model.Model;
import ru.nsu.vakhrushev.puzzle.model.util.Filter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */

public class ImagePanel extends JPanel {

    private Model model;

    public ImagePanel(Model model) {
        this.model = model;
        setBorder(BorderFactory.createLineBorder(Color.black));
        setMinimumSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        model.paintTrianglesInImage();
        BufferedImage image = model.getImage();
        if (model.isNeedBilinearFiltering()) {
            Filter.useBilinearFilter(image);
        }
        g.drawImage(image, 0, 0, null);
    }
}
