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

    public static final int RIGHT = 2;
    public static final int WRONG = 1;
    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scoreBetweenRoundPanel;
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

        setContentPane(startPanel);
        setVisible(true);
    }

    /// / start panel
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

    /// Category Panel
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

        // lägg till knapp panel till main panel
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

        // Byt till quiz panel
        JPanel quizPanel = new JPanel();
        JLabel label = new JLabel("Selected: " + categorySelection);
        quizPanel.add(label);

        setContentPane(quizPanel);
        revalidate();
        repaint();

    }

    // quiz panel
    public void updateQuizPanel(Question question) {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // lägga till uppifrån och ner

        // Title Panel med category
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(question.getCategory());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(HEADER);
        titlePanel.setMaximumSize(new Dimension(350, 30));
        mainPanel.add(titlePanel);

        // panel med frågor
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(GRAY, 2));
        questionPanel.setBackground(CARD_BACKGROUND);

        // design för question area
        JTextArea questionTextArea = new JTextArea(question.getQuestionText());
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setBackground(BUTTON_DEFAULT);
        questionPanel.add(questionTextArea, BorderLayout.CENTER);
        questionPanel.setMaximumSize(new Dimension(350, 120));

        questionPanel.setMaximumSize(new Dimension(350, 60));
        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // lägg till question panel till main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        mainPanel.add(questionPanel);

        // panel för knappar med svar och style
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2, 10, 10));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        answerPanel.setMaximumSize(new Dimension(350, 200));

        // knappar för svar
        String[] options = question.getOptions();
        JButton answerButton1 = new JButton(options[0]);
        JButton answerButton2 = new JButton(options[1]);
        JButton answerButton3 = new JButton(options[2]);
        JButton answerButton4 = new JButton(options[3]);

        String correctAnswer = options[question.getCorrectOption()];

        ////Timer och action listener
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
    }

    /// / Score panel mellan ronder
    public void scorePanelBetweenRounds(QuizScore yourScoreBoard) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setOpaque(false);

        setVisible(true);
        int gridLayoutRounds = yourScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = yourScoreBoard.getYourScoreBoard()[0].length;

        JPanel headerPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1));
        JLabel player1Label = new JLabel("Player 1");
        headerPanel.add(player1Label);
        JLabel spaceLabel = new JLabel();
        headerPanel.add(spaceLabel);
        JLabel roundLabel = new JLabel("Round");
        headerPanel.add(roundLabel);
        JLabel player2Label = new JLabel("Player 2");
        headerPanel.add(player2Label);
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
                if (answered == RIGHT) {
                    label.setBackground(GREEN); //TODO change to nice picture
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else if (answered == WRONG) {
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
            JLabel roundNrPanel = new JLabel(String.valueOf(i + 1));
            rowPanel.add(roundNrPanel);
            roundNrPanel.setHorizontalAlignment(SwingConstants.CENTER);

            //TODO add player 2 row answers
            Integer[] opponentScore = yourScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = new JLabel(String.valueOf(answered));

                if (answered == RIGHT) {
                    label.setBackground(GREEN); //TODO change to nice picture
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else if (answered == WRONG) {
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
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(roundPanel, BorderLayout.CENTER);
        scoreBetweenRoundPanel = panel;
        setContentPane(scoreBetweenRoundPanel);
        revalidate();
        repaint();
    }


    public void finalScorePanel(QuizScore finalScoreBoard) {
        // skapa main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
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
        JLabel finalScoreLabel = new JLabel("FINAL SCORE", SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font for emphasis
        finalScoreLabel.setOpaque(true);
        finalScoreLabel.setBackground(Color.WHITE);
        panel.add(finalScoreLabel, BorderLayout.NORTH);

        // visa poängställning
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1)); // Two rows: one for the scores, one for the message
        scoresPanel.setOpaque(false);

        // Player 1 och Player 2 poäng
        JLabel scoresLabel = new JLabel(
                "Player 1 score: " + yourTotalScore +
                        "  -  Player 2 score: " + opponentTotalScore,
                SwingConstants.CENTER
        );
        scoresLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoresLabel.setOpaque(true);
        scoresLabel.setBackground(Color.LIGHT_GRAY);
        scoresPanel.add(scoresLabel);

        // visa om spelar vinner förlorar eller om det blev samma poäng
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setOpaque(true);

        // kolla om client vann förlorade eller samma poäng
        if (yourTotalScore > opponentTotalScore) {
            resultLabel.setText("You won!");
            resultLabel.setBackground(Color.GREEN);
        } else if (yourTotalScore < opponentTotalScore) {
            resultLabel.setText("You lost!");
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

        JPanel roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1));
        roundPanel.setOpaque(false);

        for (int i = 0; i < gridLayoutRounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1));
            rowPanel.setOpaque(false);

            // Player 1 poäng
            Integer[] yourScore = finalScoreBoard.getYourScoreBoard()[i];
            for (int j = 0; j < yourScore.length; j++) {
                int answered = yourScore[j];
                JLabel label = new JLabel(String.valueOf(answered));
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                if (answered == RIGHT) {
                    label.setBackground(GREEN);
                } else if (answered == WRONG) {
                    label.setBackground(RED);
                } else {
                    label.setBackground(GRAY);
                }
                rowPanel.add(label);
            }

            // Rond number
            JLabel roundNrPanel = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            rowPanel.add(roundNrPanel);

            // Player 2 poäng
            Integer[] opponentScore = finalScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = new JLabel(String.valueOf(answered));
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                if (answered == RIGHT) {
                    label.setBackground(GREEN);
                } else if (answered == WRONG) {
                    label.setBackground(RED);
                } else {
                    label.setBackground(GRAY);
                }
                rowPanel.add(label);
            }
            roundPanel.add(rowPanel);
        }

        // Add the round-by-round scores to the bottom of the panel
        // lägg till panel som visar poäng för alla ronder
        panel.add(roundPanel, BorderLayout.SOUTH);

        // visa panel
        setContentPane(panel);
        revalidate();
        repaint();
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