package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

//// part of GUI, it handles only time bar block
public class TimerQuestionPanel {

    private boolean isTimerActive = true;
    private Timer timer;

    TimerQuestionPanel(JPanel mainPanel, ObjectOutputStream objectOutputStream, JPanel quizPanel) {
        ////Timer
        JPanel timerPanel = new JPanel(new BorderLayout());
        JProgressBar timerBar = new JProgressBar(0, 15); // 0-15 sek
        timerBar.setValue(15);
        timerBar.setStringPainted(false); //to not show percentage in time bar
        timerBar.setForeground(Color.GREEN);
        timerBar.setBackground(Color.RED);
        timerPanel.add(timerBar, BorderLayout.CENTER);
        timerPanel.setMaximumSize(new Dimension(350, 20));
        mainPanel.add(timerPanel);

        //// Timer logic
        timer = new Timer(1000, new ActionListener() {
            int timeLeft = 15;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerBar.setValue(timeLeft);

                if (timeLeft <= 0) {
                    isTimerActive = false;
                    ((Timer) e.getSource()).stop();

                    try {
                        objectOutputStream.writeObject("Tiden är ute, gå vidare till nästa fråga.");

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    JOptionPane.showMessageDialog(quizPanel, "Tiden är ute, gå vidare till nästa fråga.");
                }
            }
        });
        timer.start();

    }

    public void stopTimer() {
        timer.stop();
    }

    public boolean isTimerActive() {

        return isTimerActive;
    }
}
