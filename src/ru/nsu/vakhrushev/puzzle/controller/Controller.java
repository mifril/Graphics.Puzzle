package ru.nsu.vakhrushev.puzzle.controller;

import ru.nsu.vakhrushev.puzzle.model.Model;
import ru.nsu.vakhrushev.puzzle.view.InterfacePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Maxim Vakhrushev on 23.02.14 13:44.
 */
public class Controller {
    private static final int DELAY = 30;

    private Model model;
    private Timer timer = null;
    private boolean timerRunning = false;

    public Controller(Model model) {
        this.model = model;
    }

    public void resetAlphaBlendingFlag() {
        model.resetNeedBlend();
    }

    public void resetBilinearFilteringFlag() {
        model.resetNeedBilinearFiltering();
    }

    public void initAnimationParameters() {
        model.initTrianglesFinishPositions();
    }

    public void animationForceMove(int position) {
        model.forceMove(position);
    }

    public void setTimer(final JSlider slider, final JFrame mainFrame) {
        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sliderValue = slider.getValue();
                if (sliderValue != InterfacePanel.MAX_SLIDER_VALUE) {
                    int rotateDirection = (sliderValue <= InterfacePanel.MAX_SLIDER_VALUE / 2) ? (1) : (-1);
                    model.moveTriangles(rotateDirection);
                    slider.setValue(sliderValue + 1);
                } else {
                    stopTimer();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainFrame.repaint();

                    }
                });
            }
        });
    }

    public void resetTimer() {
        stopTimer();
        timer = null;
    }

    public void startTimer (final JSlider slider, final JFrame mainFrame) {
        if (timer == null) {
            setTimer(slider, mainFrame);
        }
        timerRunning = true;
        timer.start();
    }

    public void stopTimer() {
        timerRunning = false;
        if (timer != null) {
            timer.stop();
        }
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}
