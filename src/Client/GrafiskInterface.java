package Client;

import Messages.QuizAnswer;
import Server.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static Client.ColorGUI.*;

public class GrafiskInterface extends JFrame {

    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private int score = 0; //Start score from 0
    private JPanel finalScorePanel;
    private boolean isTimerActive;

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    public GrafiskInterface(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        setTitle("Quiz Kampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel = createStartPanel();
        //quizPanel = createQuizPanel();

        setContentPane(startPanel);
        setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("QuizKampen");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("Starta nytt spel");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setContentPane(quizPanel);
                revalidate();
                repaint();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        buttonPanel.setBackground(BACKGROUND);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    public void updateQuizPanel(Question question) {
        isTimerActive = true;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //meaning components added to this panel will be arranged vertically (from top to bottom).
        //add(mainPanel, BorderLayout.CENTER);

        // Title Panel
        ////Category
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(question.getCategory());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(HEADER);
        titlePanel.setMaximumSize(new Dimension(350, 30));
        mainPanel.add(titlePanel);

        ////
        JPanel timerPanel = new JPanel(new BorderLayout());
        JProgressBar timerBar = new JProgressBar(0, 15); // Range from 0 to 15 seconds
        timerBar.setValue(15);
        timerBar.setStringPainted(true);
        timerBar.setForeground(Color.GREEN);
        timerBar.setBackground(Color.RED);
        timerPanel.add(timerBar, BorderLayout.CENTER);
        timerPanel.setMaximumSize(new Dimension(350, 20));
        mainPanel.add(timerPanel);

        // Server.Question Panel with light blue background and border
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        questionPanel.setBackground(CARD_BACKGROUND);

        ////Question
        JTextArea questionTextArea = new JTextArea(question.getQuestionText());
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setBackground(BUTTON_DEFAULT);
        questionPanel.add(questionTextArea, BorderLayout.CENTER);
        questionPanel.setMaximumSize(new Dimension(350, 120));

        //// Set max size for consistent look
        questionPanel.setMaximumSize(new Dimension(350, 60));
        // Center-align within BoxLayout
        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add question panel to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        mainPanel.add(questionPanel);

        // Answer Buttons Panel with GridLayout and Spacing
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 grid with spacing
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        answerPanel.setMaximumSize(new Dimension(350, 200));

        ////answers
        String[] options = question.getOptions();
        JButton answerButton1 = new JButton(options[0]);
        JButton answerButton2 = new JButton(options[1]);
        JButton answerButton3 = new JButton(options[2]);
        JButton answerButton4 = new JButton(options[3]);

        String correctAnswer = options[question.getCorrectOption()];

        ActionListener answerListener = e -> {
            if (isTimerActive) { // Check if the timer is still active
                JButton clickedButton = (JButton) e.getSource();
                handleAnswerSelection(clickedButton, correctAnswer);
            } else {
                JOptionPane.showMessageDialog(quizPanel, "Tiden är ute, inga poäng för detta svar.");
            }
        };

        //Each button is linked to an ActionListener that checks if the clicked answer is correct.
        answerButton1.addActionListener(answerListener);
        answerButton2.addActionListener(answerListener);
        answerButton3.addActionListener(answerListener);
        answerButton4.addActionListener(answerListener);

        answerPanel.add(answerButton1);
        answerPanel.add(answerButton2);
        answerPanel.add(answerButton3);
        answerPanel.add(answerButton4);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        mainPanel.add(answerPanel);

        // Score Panel
        scorePanel = new JPanel();
        scoreLabel = new JLabel("Poäng: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scorePanel.setMaximumSize(new Dimension(350, 25));
        scorePanel.add(scoreLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        mainPanel.add(scorePanel);

        quizPanel = mainPanel;
        setContentPane(quizPanel);
        revalidate();

        //// Timer logic
        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = 15;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerBar.setValue(timeLeft);
                timerBar.setString(timeLeft + " sekunder");

                if (timeLeft <= 0) {
                    isTimerActive = false; // Mark timer as inactive
                    ((Timer) e.getSource()).stop();

                    JOptionPane.showMessageDialog(quizPanel, "Tiden är ute, gå vidare till nästa fråga.");
                    fetchNextQuestion();

                }
            }
        });
        timer.start();

    }

    private void addAnswerButtonListener(JButton button, String correctAnswer) {
        button.addActionListener(e -> handleAnswerSelection(button, correctAnswer));
    }

    private void handleAnswerSelection(JButton selectedButton, String correctAnswer) {
        if (!isTimerActive){
            return;
        }

        isTimerActive = false;

        if (selectedButton.getText().equals(correctAnswer)) {
            selectedButton.setBackground(BUTTON_CORRECT); // Highlight correct answer
            score++; // Increment score
        } else {
            selectedButton.setBackground(BUTTON_WRONG); // Highlight incorrect answer
        }

        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setAnswer(selectedButton.getText());

        try {
            objectOutputStream.writeObject(quizAnswer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        scoreLabel.setText("Poäng: " + score); // Update score label

        Timer delayTimer = new Timer(1000, e -> fetchNextQuestion());
        delayTimer.setRepeats(false); // Execute only once
        delayTimer.start();
    }
    private void fetchNextQuestion() {
        try {
            Question nextQuestion = (Question) objectInputStream.readObject(); // Receive next question from server
            updateQuizPanel(nextQuestion); // Update the panel with the new question
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "error occurred when fetching question");
        }
    }
}