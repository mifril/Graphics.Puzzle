package ru.nsu.vakhrushev.puzzle.view;

import ru.nsu.vakhrushev.puzzle.controller.Controller;
import ru.nsu.vakhrushev.puzzle.model.Model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Maxim Vakhrushev on 23.02.14 13:47.
 */

public class InterfacePanel extends JPanel {
    public static final int MIN_SLIDER_VALUE = 0;
    public static final int MAX_SLIDER_VALUE = 360;
    public static final int INIT_SLIDER_VALUE = 0;

    private Model model;

    public InterfacePanel (final Controller controller, Model model, final JFrame mainFrame) {
        this.model = model;

        JCheckBox filterCheckBox = new JCheckBox("Filter");
        JCheckBox blendCheckBox = new JCheckBox("Blend");
        JButton startButton = new JButton("Start/Stop");
        JButton initButton = new JButton("Init");
        final JSlider slider = new JSlider();
        slider.setMajorTickSpacing(MAX_SLIDER_VALUE / 2);
        slider.setMinimum(MIN_SLIDER_VALUE);
        slider.setMaximum(MAX_SLIDER_VALUE);
        slider.setValue(INIT_SLIDER_VALUE);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        filterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.resetBilinearFilteringFlag();
                mainFrame.repaint();
            }
        });
        blendCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.resetAlphaBlendingFlag();
                mainFrame.repaint();
            }
        });
        initButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slider.setValue(0);
                controller.resetTimer();
                controller.initAnimationParameters();
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.isTimerRunning()) {
                    controller.stopTimer();
                } else {
                    controller.startTimer(slider, mainFrame);
                }
            }
        });
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.animationForceMove(slider.getValue());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainFrame.repaint();                 }
                });
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addComponent(filterCheckBox)
                .addComponent(blendCheckBox)
                .addComponent(slider)
                .addComponent(startButton)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(initButton))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(initButton)
                    .addComponent(filterCheckBox)
                    .addComponent(blendCheckBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(slider))
        );
    }


}
