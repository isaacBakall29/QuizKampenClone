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
    private JPanel scoreBetweenRoundPanel;
    TimerQuestionPanel timerQuestionPanel;

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    public GrafiskInterface(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        setTitle("QuizKampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel = createStartPanel();

        setContentPane(startPanel);
        setVisible(true);
    }

    //// start panel
    private JPanel createStartPanel() {


        JPanel panel = new ImagePanel("https://loremflickr.com/400/500");

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
        buttonPanel.setOpaque(false);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /// /Category Panel
    public void createCategory(List<String> categories) {

        JPanel panel = new ImagePanel("https://loremflickr.com/400/500");
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
    public JPanel createQuizPanel(Question question) {


        JPanel mainPanel = new ImagePanel("https://loremflickr.com/400/500");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Category
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(question.getCategory());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(header);
        titlePanel.setMaximumSize(new Dimension(350, 30));
        mainPanel.add(titlePanel);

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
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 20));
        questionTextArea.setMargin(new Insets(30, 20, 20, 20));
        questionPanel.add(questionTextArea, BorderLayout.CENTER);

        // Add image to the right
        if (question.getImage() != null) {
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(question.getImage());
            Image scaledImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            questionPanel.add(imageLabel, BorderLayout.EAST);
        }

        questionPanel.setMaximumSize(new Dimension(350, 120));
        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(questionPanel);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2, 10, 10));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        answerPanel.setMaximumSize(new Dimension(350, 200));

        String[] options = question.getOptions();
        JButton answerButton1 = new JButton(options[0]);
        JButton answerButton2 = new JButton(options[1]);
        JButton answerButton3 = new JButton(options[2]);
        JButton answerButton4 = new JButton(options[3]);

        String correctAnswer = options[question.getCorrectOption()];

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

        return mainPanel;
    }

    public void updateQuizPanel(Question question) {
        quizPanel = createQuizPanel(question);
        setContentPane(quizPanel);
        revalidate();
        repaint();


    }

    //// Score panel between rounds
    public void scorePanelBetweenRounds(QuizScore yourScoreBoard) {

        JPanel panel = new ImagePanel("https://loremflickr.com/400/500");
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        setVisible(true);
        int gridLayoutRounds = yourScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = yourScoreBoard.getYourScoreBoard()[0].length;


        JPanel headerPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 3));

        for (int i = 0; i < questionsPerRound - 1; i++) {
            headerPanel.add(new JLabel());
        }
        JLabel player1Label = new JLabel("PLAYER 1");
        player1Label.setFont(new Font("Arial", Font.BOLD, 11));
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        player1Label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        headerPanel.add(player1Label);

        JLabel spaceLabel = new JLabel();
        headerPanel.add(spaceLabel);

        JLabel roundLabel = new JLabel("Round");
        roundLabel.setFont(new Font("Arial", Font.BOLD, 11));
        roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roundLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(roundLabel);

        for (int i = 0; i < questionsPerRound - 1; i++) {
            headerPanel.add(new JLabel());
        }
        JLabel player2Label = new JLabel("PLAYER 2");
        player2Label.setFont(new Font("Arial", Font.BOLD, 11

        ));
        player2Label.setHorizontalAlignment(SwingConstants.CENTER);
        player2Label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        headerPanel.add(player2Label);

        for (int i = 0; i < questionsPerRound - 1; i++) {
            headerPanel.add(new JLabel());
        }

        JPanel roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1));
        roundPanel.setOpaque(false);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(roundPanel, BorderLayout.CENTER);


        roundPanel = new JPanel(new GridLayout(gridLayoutRounds, 1));
        roundPanel.setOpaque(false);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(roundPanel, BorderLayout.CENTER);

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
                    label.setBackground(button_correct); //TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("Fel");
                    label.setBackground(button_wrong);
                } else {
                    label.setText("");
                    label.setBackground(text_subtitle);
                }
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanel.add(label);
            }

            //TODO add label with round number
            JLabel roundNrPanel = new JLabel(String.valueOf((i) + 1));
            rowPanel.add(roundNrPanel);
            roundNrPanel.setHorizontalAlignment(SwingConstants.CENTER);

            //TODO add player 2 row answers
            Integer[] opponentScore = yourScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = new JLabel();
                if (answered == RIGHT) {
                    label.setText("Rätt");
                    label.setBackground(button_correct); //TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("Fel");
                    label.setBackground(button_wrong);
                } else {
                    label.setText("");
                    label.setBackground(text_subtitle);
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
        JPanel panel = new ImagePanel("https://loremflickr.com/400/500");
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

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
            resultLabel.setBackground(button_correct);
        } else if (yourTotalScore < opponentTotalScore) {
            resultLabel.setText("Du förlorade!");
            resultLabel.setBackground(button_wrong);
        } else {
            resultLabel.setText("Both won!");
            resultLabel.setBackground(button_hover);
        }
        scoresPanel.add(resultLabel);

        // lägg till scoresPanel
        panel.add(scoresPanel, BorderLayout.CENTER);

        // Create a combined panel for round scores and the exit button
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);

        // Display scores for all rounds
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
            roundNrLabel.setBackground(Color.WHITE);
            roundNrLabel.setForeground(Color.BLACK);
            roundNrLabel.setOpaque(true);
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

        southContainer.add(roundPanel, BorderLayout.CENTER);

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        exitButtonPanel.setOpaque(false);

        JButton exitButton = new JButton("Exit the Game");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(192, 192, 192));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(e -> System.exit(0));
        exitButtonPanel.add(exitButton);

        southContainer.add(exitButtonPanel, BorderLayout.SOUTH);
        panel.add(southContainer, BorderLayout.SOUTH);

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
            backgroundColor = button_correct;
        } else if (score == WRONG) {
            displayText = "Fel";
            backgroundColor = button_wrong;
        } else {
            backgroundColor = text_subtitle;
        }

        JLabel label = new JLabel(displayText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(backgroundColor);
        label.setPreferredSize(new Dimension(40, 30));

        return label;
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