package Client;

import Messages.QuizAnswer;
import Server.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static Client.ColorGUI.*;

public class GrafiskInterface extends JFrame {

    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private int score = 0;
    private JPanel finalScorePanel;
    TimerQuestionPanel timerQuestionPanel;

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

    //// start panel
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

        startButton.addActionListener(e -> {
            try {
                objectOutputStream.writeObject("player ready");

                startButton.setEnabled(false); //disable button after click it once

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        buttonPanel.setBackground(BACKGROUND);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    ////Category Panel
    public void createCategory() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Category"));

        JLabel label = new JLabel("Please choose a category");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        JButton button1 = new JButton("Sverige");
        JButton button2 = new JButton("Musik");
        JButton button3 = new JButton("Java");

        button1.addActionListener(e -> handleCategorySelection((JButton)e.getSource()));
        button2.addActionListener(e -> handleCategorySelection((JButton)e.getSource()));
        button3.addActionListener(e -> handleCategorySelection((JButton)e.getSource()));

        // Add buttons to a sub-panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(panel);
        revalidate();
        repaint();
    }

    //// Handle Category Selection in the future
    private void handleCategorySelection(JButton categorySelection) {
        System.out.println("Button clicked: " + categorySelection.getText());
        try {
            objectOutputStream.writeObject(categorySelection.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Transition to Quiz Panel (or any other action)
        JPanel quizPanel = new JPanel();
        JLabel label = new JLabel("Selected: " + categorySelection);
        quizPanel.add(label);

        setContentPane(quizPanel);
        revalidate();
        repaint();

    }

    //// quiz panel
    public void updateQuizPanel(Question question) {

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

        ////Timer and action listener
        timerQuestionPanel = new TimerQuestionPanel(mainPanel, objectOutputStream, quizPanel);

        ActionListener answerListener = e -> {
            if (timerQuestionPanel.isTimerActive()) {
                timerQuestionPanel.stopTimer();
                JButton clickedButton = (JButton) e.getSource();
                handleAnswerSelection(clickedButton, correctAnswer);
                answerButton1.setEnabled(false);
                answerButton2.setEnabled(false);
                answerButton3.setEnabled(false);
                answerButton4.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(quizPanel, "Tiden är ute, gå vidare till nästa fråga.");
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

        //// Score Panel within quiz panel
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

    }

    // TODO score panel


    private void addAnswerButtonListener(JButton button, String correctAnswer) {
        button.addActionListener(e -> handleAnswerSelection(button, correctAnswer));
    }

    private void handleAnswerSelection(JButton selectedButton, String correctAnswer) {

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

    }

    public void displayWaitingForPlayers (){
        JLabel waitingForPlayersLabel = new JLabel("vänta på motståndare");
        waitingForPlayersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        waitingForPlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForPlayersLabel.setMaximumSize(new Dimension(350, 25));
        waitingForPlayersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingForPlayersLabel.setBackground(BUTTON_DEFAULT);

        startPanel.add(waitingForPlayersLabel, BorderLayout.SOUTH);
        revalidate();
    }

}