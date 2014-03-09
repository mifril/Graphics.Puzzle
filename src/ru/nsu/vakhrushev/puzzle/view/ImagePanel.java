package ru.nsu.vakhrushev.puzzle.view;

import ru.nsu.vakhrushev.puzzle.model.Model;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */

public class ImagePanel extends JPanel {

    private Model model;

    public ImagePanel(final Model model, final MainFrame mainFrame) {
        this.model = model;
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                model.findTriangleUnderCursor(e.getX(), e.getY());
                mainFrame.updateInfoBar();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        model.paintTrianglesInImage();
        BufferedImage image = model.getImage();
        if (model.isNeedBilinearFiltering()) {
        //    Filter.useBilinearFilter(image);
        }
        g.drawImage(image, 0, 0, null);
    }
}
