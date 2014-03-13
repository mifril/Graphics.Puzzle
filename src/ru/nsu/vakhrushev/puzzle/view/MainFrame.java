package ru.nsu.vakhrushev.puzzle.view;

import ru.nsu.vakhrushev.puzzle.controller.Controller;
import ru.nsu.vakhrushev.puzzle.model.Model;
import ru.nsu.vakhrushev.puzzle.model.Triangle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */
public class MainFrame extends JFrame {
    private Model model;
    private JLabel infoBar;

    public MainFrame(Model model, Controller controller) {
        this.model = model;

        JPanel imagePanel = new ImagePanel(model, this);
        JPanel interfacePanel = new InterfacePanel(controller, this);
        infoBar = new JLabel("Triangle info");

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(infoBar);
        add(imagePanel);
        add(interfacePanel);

        setPreferredSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(500, 500));
        setSize(new Dimension(600, 600));

        setTitle("Puzzle");
        controller.initAnimationParameters();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void updateInfoBar() {
        Triangle triangleUnderCursor = model.getTriangleUnderCursor();
        if (triangleUnderCursor == null) {
            infoBar.setText("No information");
        } else {
            infoBar.setText("Border pixels: " + triangleUnderCursor.getBorderPixels() + ". " +
                    "Clear pixels: " + triangleUnderCursor.getClearPixels() + ". " +
                    "Total pixels: " + triangleUnderCursor.getTotalPixels());
        }
    }
}
