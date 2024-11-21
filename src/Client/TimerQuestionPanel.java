package Client;

import Messages.TimeExpired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TimerQuestionPanel {

    private boolean isTimerActive = true;

    TimerQuestionPanel (JPanel mainPanel, ObjectOutputStream objectOutputStream, JPanel quizPanel) {
        ////Timer
        JPanel timerPanel = new JPanel(new BorderLayout());
        JProgressBar timerBar = new JProgressBar(0, 15); // Range from 0 to 15 seconds
        timerBar.setValue(15);
        timerBar.setStringPainted(true);
        timerBar.setForeground(Color.GREEN);
        timerBar.setBackground(Color.RED);
        timerPanel.add(timerBar, BorderLayout.CENTER);
        timerPanel.setMaximumSize(new Dimension(350, 20));
        mainPanel.add(timerPanel);

        //// Timer logic
        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = 15;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerBar.setValue(timeLeft);
                timerBar.setString(timeLeft + " sekunder");

                if (timeLeft <= 0) {
                    isTimerActive = false;
                    ((Timer) e.getSource()).stop();

                    try {
                        objectOutputStream.writeObject(new TimeExpired());

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    JOptionPane.showMessageDialog(quizPanel, "Tiden är ute, gå vidare till nästa fråga.");
                    //fetchNextQuestion();

                }
            }
        });
        timer.start();

    }

    public boolean isTimerActive() {
        return isTimerActive;
    }
}