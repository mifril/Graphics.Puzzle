package ru.nsu.vakhrushev.puzzle;

import ru.nsu.vakhrushev.puzzle.controller.Controller;
import ru.nsu.vakhrushev.puzzle.model.Model;
import ru.nsu.vakhrushev.puzzle.view.MainFrame;

import javax.swing.*;

/**
 * Created by Maxim Vakhrushev on 23.02.14 17:39.
 */
public class Initializer {
    public static void main(String [] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
//            PrintStream out = new PrintStream(new FileOutputStream("log.txt"));
//            System.setErr(out);
                    Model model = new Model("resources/images/puzzle.png");
                    Controller controller = new Controller(model);
                    JFrame frame = new MainFrame(model, controller);
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });

    }
}
