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
        setBackground(Color.GRAY);
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
        g.drawImage(image, this.getWidth() / 2 - image.getWidth() / 2, this.getHeight() / 2 - image.getHeight() / 2,
                                                                                                                null);
    }
}
