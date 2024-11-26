package Client;

import Messages.QuizAnswer;
import Messages.QuizScore;
import Server.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


import static Client.ColorGUI.*;
import static java.awt.Color.*;


public class GrafiskInterface extends JFrame {

    public static final int RIGHT = 2;
    public static final int WRONG = 1;
    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scorePanel;
    private JPanel scoreBetweenRoundPanel;
    private JPanel finalScorePanel;
    private JLabel scoreLabel;
    private int player1Score;
    private int player2Score;
    TimerQuestionPanel timerQuestionPanel;

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    public GrafiskInterface(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        setTitle("Quiz Kampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel = createStartPanel();
        scorePanel = createScorePanel();

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

        // hämta array med kategorier till en lista och blanda
        List<String> categoryList = new ArrayList<>(categories);
        Collections.shuffle(categoryList);

        JButton button1 = new JButton(categoryList.get(0));
        JButton button2 = new JButton(categoryList.get(1));
        JButton button3 = new JButton(categoryList.get(2));

        button1.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button2.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button3.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));

        // lägg till knappar till knapp panel
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
    }

    //// quiz panel
    public void updateQuizPanel(Question question) {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        ////Category
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(question.getCategory());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(header);
        titlePanel.setMaximumSize(new Dimension(350, 30));
        mainPanel.add(titlePanel);
        updateScorePanel(player1Score, player2Score);
        mainPanel.add(scorePanel);

        // Server.Question Panel with light blue background and border
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(GRAY, 2));
        questionPanel.setBackground(card_background);

        ////Question
        JTextArea questionTextArea = new JTextArea(question.getQuestionText());
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setBackground(button_default);
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

    /// / Score panel mellan ronder
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

    //// Score panel between rounds
    public void scorePanelBetweenRounds(QuizScore yourScoreBoard) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        setVisible(true);
        int gridLayoutRounds = yourScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = yourScoreBoard.getYourScoreBoard()[0].length;

        JPanel headerPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1));
        JLabel player1Label = new JLabel("Dina svar");
        headerPanel.add(player1Label);
        JLabel spaceLabel = new JLabel();
        headerPanel.add(spaceLabel);
        JLabel roundLabel = new JLabel("Round");
        headerPanel.add(roundLabel);
        JLabel player2Label = new JLabel("Motståndarens svar");
        headerPanel.add(player2Label);

        JPanel roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1));
        roundPanel.setOpaque(false);

        for (int i = 0; i < gridLayoutRounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1));
            rowPanel.setOpaque(false);

            // dina egna svar
            Integer[] yourScore = yourScoreBoard.getYourScoreBoard()[i];
            for (int j = 0; j < yourScore.length; j++) {
                int answered = yourScore[j];
                JLabel label = new JLabel();
                if (answered == RIGHT) {
                    label.setText("Rätt");
                    label.setBackground(GREEN); //TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("Fel");
                    label.setBackground(RED);
                } else {
                    label.setText("");
                    label.setBackground(GRAY);
                }
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
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
                JLabel label = new JLabel();
                if (answered == RIGHT) {
                    label.setText("Rätt");
                    label.setBackground(GREEN); //TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("Fel");
                    label.setBackground(RED);
                } else {
                    label.setText("");
                    label.setBackground(GRAY);
                }
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanel.add(label);
            }
            roundPanel.add(rowPanel);
        }

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(roundPanel, BorderLayout.CENTER);
        scoreBetweenRoundPanel = panel;
        setContentPane(scoreBetweenRoundPanel);
        revalidate();
        repaint();
    }


    public void finalScorePanel(QuizScore finalScoreBoard) {
        // skapa main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // räkna poängställning
        int yourTotalScore = 0;
        int opponentTotalScore = 0;

        for (Integer[] roundScores : finalScoreBoard.getYourScoreBoard()) {
            for (int score : roundScores) {
                if (score == RIGHT) {
                    yourTotalScore++;
                }
            }
        }

        for (Integer[] roundScores : finalScoreBoard.getOpponentScoreBoard()) {
            for (int score : roundScores) {
                if (score == RIGHT) {
                    opponentTotalScore++;
                }
            }
        }

        // final score högst upp
        JLabel finalScoreLabel = new JLabel("RESULTAT", SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        finalScoreLabel.setOpaque(true);
        finalScoreLabel.setBackground(new Color(192, 192, 192));
        finalScoreLabel.setForeground(Color.WHITE);
        panel.add(finalScoreLabel, BorderLayout.NORTH);

        // visa poängställning
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // två rader poäng resultat
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // lite space runt
        scoresPanel.setOpaque(false);

        // Player 1 och Player 2 poäng
        JLabel scoresLabel = new JLabel(
                "Dina poäng: " + yourTotalScore + "  -  Motståndarens poäng: " + opponentTotalScore,
                SwingConstants.CENTER
        );

        scoresLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoresLabel.setOpaque(true);
        scoresLabel.setBackground(Color.LIGHT_GRAY);
        scoresPanel.add(scoresLabel);

        // visa om spelar vinner förlorar eller om det blev samma poäng
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setOpaque(true);

        // kolla om client vann förlorade eller samma poäng
        if (yourTotalScore > opponentTotalScore) {
            resultLabel.setText("Du vann!");
            resultLabel.setBackground(Color.GREEN);
        } else if (yourTotalScore < opponentTotalScore) {
            resultLabel.setText("Du förlorade!");
            resultLabel.setBackground(Color.RED);
        } else {
            resultLabel.setText("Both won!");
            resultLabel.setBackground(Color.YELLOW);
        }
        scoresPanel.add(resultLabel);

        // lägg till scoresPanel
        panel.add(scoresPanel, BorderLayout.CENTER);

        // visa poängen för alla rundor
        int gridLayoutRounds = finalScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = finalScoreBoard.getYourScoreBoard()[0].length;

        JPanel roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1, 5, 5));
        roundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        roundPanel.setOpaque(false);

        for (int i = 0; i < gridLayoutRounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1, 5, 0));
            rowPanel.setOpaque(false);

            // Player 1 poäng
            Integer[] yourScore = finalScoreBoard.getYourScoreBoard()[i];
            for (int j = 0; j < yourScore.length; j++) {
                int answered = yourScore[j];
                JLabel label = createScoreLabel(answered);
                rowPanel.add(label);
            }

            // Rond number
            JLabel roundNrLabel = new JLabel("R" + (i + 1), SwingConstants.CENTER);
            roundNrLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rowPanel.add(roundNrLabel);

            // Player 2 poäng
            Integer[] opponentScore = finalScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = createScoreLabel(answered);
                rowPanel.add(label);
            }

            roundPanel.add(rowPanel);
        }

        // lägg till panel som visar poäng för alla ronder
        panel.add(roundPanel, BorderLayout.SOUTH);

        // visa panel
        setContentPane(panel);
        revalidate();
        repaint();
    }

    // method för att byta 1 och 2 mot "fel" och "rätt"
    private JLabel createScoreLabel(int score) {
        String displayText = ""; // Default to blank
        Color backgroundColor;

        if (score == RIGHT) {
            displayText = "Rätt";
            backgroundColor = GREEN;
        } else if (score == WRONG) {
            displayText = "Fel";
            backgroundColor = RED;
        } else {
            backgroundColor = GRAY;
        }

        JLabel label = new JLabel(displayText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(backgroundColor);
        label.setPreferredSize(new Dimension(40, 30));

        return label;
    }




    private void addAnswerButtonListener(JButton button, String correctAnswer) { //TODO ta bort
        button.addActionListener(e -> handleAnswerSelection(button, correctAnswer));
    }

    private void handleAnswerSelection(JButton selectedButton, String correctAnswer) {

        if (selectedButton.getText().equals(correctAnswer)) {
            selectedButton.setBackground(button_correct);
        } else {
            selectedButton.setBackground(button_wrong);
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
        waitingForPlayersLabel.setBackground(button_default);

        startPanel.add(waitingForPlayersLabel, BorderLayout.SOUTH);
        revalidate();
    }


}