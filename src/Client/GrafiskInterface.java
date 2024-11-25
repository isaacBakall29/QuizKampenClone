package Client;

import Messages.QuizAnswer;
import Messages.QuizScore;
import Server.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import static Client.ColorGUI.*;
import static java.awt.Color.*;
import static java.awt.Transparency.OPAQUE;

public class GrafiskInterface extends JFrame {

    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scorePanel;
    private JPanel scoreBetweenRoundPanel;
    private JPanel finalScorePanel;
    private JLabel scoreLabel;
    private int player1Score = 0;
    private int player2Score = 0;
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
        scorePanel = createScorePanel();
//        finalScorePanel = createFinalScorePanel(0,0);
        //quizPanel = createQuizPanel();

        setContentPane(startPanel);
        setVisible(true);
    }

    /// / start panelg
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
        buttonPanel.setBackground(ColorGUI.background);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /// /Category Panel
    public void createCategory(List<String> categories) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Category"));

        JLabel label = new JLabel("Please choose a category");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        // read array of categories into a list and shuffles it
        List<String> categoryList = new ArrayList<>(categories);
        Collections.shuffle(categoryList);

        JButton button1 = new JButton(categoryList.get(0));
        JButton button2 = new JButton(categoryList.get(1));
        JButton button3 = new JButton(categoryList.get(2));

        button1.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button2.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button3.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));

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

    /// / Handle Category Selection in the future
    private void handleCategorySelection(JButton categorySelection) {
        System.out.println("Button clicked: " + categorySelection.getText());
        try {
            objectOutputStream.writeObject(categorySelection.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Transition to Quiz Panel (or any other action)
        //JPanel quizPanel = new JPanel();
       // JLabel label = new JLabel("Selected: " + categorySelection);
        //quizPanel.add(label);

        //setContentPane(quizPanel);
        //revalidate();
       // repaint();

    }

    /// / quiz panel
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
        updateScorePanel(player1Score, player2Score);
        mainPanel.add(scorePanel);

        // Server.Question Panel with light blue background and border
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(GRAY, 2));
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


        answerButton1.addActionListener(answerListener);
        answerButton2.addActionListener(answerListener);
        answerButton3.addActionListener(answerListener);
        answerButton4.addActionListener(answerListener);

        answerPanel.add(answerButton1);
        answerPanel.add(answerButton2);
        answerPanel.add(answerButton3);
        answerPanel.add(answerButton4);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(answerPanel);

        quizPanel = mainPanel;
        setContentPane(quizPanel);
        revalidate();
        repaint();
    }

    private JPanel createScorePanel() {
        scorePanel = new JPanel();
        scoreLabel = new JLabel("0 - 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scorePanel.add(scoreLabel);
        scorePanel.setMaximumSize(new Dimension(350, 25));
        return scorePanel;
    }

    public void updateScorePanel(int player1Score, int player2Score) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        scoreLabel.setText(player1Score + " - " + player2Score);
        scorePanel.revalidate();
        scorePanel.repaint();
    }

    /// / Score panel between rounds
    public void scorePanelBetweenRounds(QuizScore yourScoreBoard) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setOpaque(false);

        setVisible(true);
        int gridLayoutRounds = yourScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = yourScoreBoard.getYourScoreBoard()[0].length;

        JPanel roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1));
        roundPanel.setOpaque(false);
        for (int i = 0; i < gridLayoutRounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1));
            rowPanel.setOpaque(false);
            //TODO add player 1 row answers
            Integer[] yourScore = yourScoreBoard.getYourScoreBoard()[i];
            for (int j = 0; j < yourScore.length; j++) {
                int answered = yourScore[j];
                JLabel label = new JLabel(String.valueOf(answered));

                if (answered == 2) {
                    label.setBackground(GREEN); //TODO change to nice picture
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else if (answered == 1) {
                    label.setBackground(RED);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    label.setBackground(GRAY);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                rowPanel.add(label);
            }

            //TODO add label with round number
            JLabel roundNrPanel = new JLabel(String.valueOf(i));
            rowPanel.add(roundNrPanel);
            roundNrPanel.setHorizontalAlignment(SwingConstants.CENTER);

            //TODO add player 2 row answers
            Integer[] opponentScore = yourScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = new JLabel(String.valueOf(answered));

                if (answered == 2) {
                    label.setBackground(GREEN); //TODO change to nice picture
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else if (answered == 1) {
                    label.setBackground(RED);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    label.setBackground(GRAY);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                rowPanel.add(label);
            }
            roundPanel.add(rowPanel);
        }
        panel.add(roundPanel, BorderLayout.CENTER);
        scoreBetweenRoundPanel = panel;
        setContentPane(scoreBetweenRoundPanel);
        revalidate();
        repaint();
    }

    private JPanel createFinalScorePanel(int player1Score, int player2Score) {
        finalScorePanel = new JPanel();
        scoreLabel = new JLabel(player1Score + " - " + player2Score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scorePanel.add(scoreLabel);
        scorePanel.setMaximumSize(new Dimension(350, 25));
        //TODO lägga till mer text och vem som vinner
        return scorePanel;
    }

    private void addAnswerButtonListener(JButton button, String correctAnswer) {
        button.addActionListener(e -> handleAnswerSelection(button, correctAnswer));
    }

    private void handleAnswerSelection(JButton selectedButton, String correctAnswer) {

        if (selectedButton.getText().equals(correctAnswer)) {
            selectedButton.setBackground(BUTTON_CORRECT);
        } else {
            selectedButton.setBackground(BUTTON_WRONG);
        }

        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setAnswer(selectedButton.getText());

        try {
            objectOutputStream.writeObject(quizAnswer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayWaitingForPlayers() {
        JLabel waitingForPlayersLabel = new JLabel("väntar på motståndare");
        waitingForPlayersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        waitingForPlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForPlayersLabel.setMaximumSize(new Dimension(350, 25));
        waitingForPlayersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingForPlayersLabel.setBackground(BUTTON_DEFAULT);

        startPanel.add(waitingForPlayersLabel, BorderLayout.SOUTH);
        revalidate();
    }


}