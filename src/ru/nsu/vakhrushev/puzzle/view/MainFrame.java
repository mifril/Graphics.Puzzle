package ru.nsu.vakhrushev.puzzle.view;

import ru.nsu.vakhrushev.puzzle.controller.Controller;
import ru.nsu.vakhrushev.puzzle.model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */
public class MainFrame extends JFrame {
    final JPanel imagePanel;
    JPanel interfacePanel;

    public MainFrame(Model model, Controller controller) {

        imagePanel = new ImagePanel(model);
        interfacePanel = new InterfacePanel(controller, this);
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        add(interfacePanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(400, 400));
        setSize(new Dimension(600, 600));

        controller.initAnimationParameters();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
    }
}
