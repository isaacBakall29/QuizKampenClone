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
    private Font NMFDisplay;
    private Font MarioKart;
    private Font MaruMonica;

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    public GrafiskInterface(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        setTitle("QuizKampen");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            InputStream inputStream = getClass().getResourceAsStream("/Fonts/MarioKart.ttf");
            if (inputStream == null) System.out.println("null");
            MarioKart = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            if (inputStream == null) System.out.println("null");
            inputStream= getClass().getResourceAsStream("/Fonts/MaruMonica.ttf");
            MaruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            inputStream.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        startPanel = createStartPanel();
        setContentPane(startPanel);
        setVisible(true);
    }

    //// start panel
    private JPanel createStartPanel() {

        JPanel panel = new ImagePanel("src/Client/Resources/Images/WelcomeToQUZKMMPEN.jpeg");

        panel.setLayout(new BorderLayout());

        JButton startButton = new JButton("START");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setFont(MarioKart.deriveFont(Font.BOLD, 20));
        startButton.setForeground(saddlebrown);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(golden);
        startButton.setBorder(BorderGUI.THIN_BORDER);

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

        JPanel panel = new ImagePanel("src/Client/Resources/Images/VALLYOFCOMPUTERS.jpeg");
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Category"));

        JLabel label = new JLabel("PLEASE CHOOSE A CATEGORY");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(MarioKart.deriveFont(Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(WHITE);
        label.setBorder(BorderGUI.THICK_BORDER);
        panel.add(label, BorderLayout.NORTH);

        // hämta array med kategorier till en lista och blanda
        List<String> categoryList = new ArrayList<>(categories);
        Collections.shuffle(categoryList);

        JButton button1 = new JButton(categoryList.get(0));
        button1.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        button1.setBackground(golden);
        JButton button2 = new JButton(categoryList.get(1));
        button2.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        button2.setBackground(golden);
        JButton button3 = new JButton(categoryList.get(2));
        button3.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        button3.setBackground(golden);

        button1.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button2.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));
        button3.addActionListener(e -> handleCategorySelection((JButton) e.getSource()));

        // lägg till knappar till knapp panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
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


        JPanel mainPanel = new ImagePanel("src/Client/Resources/Images/backgoundpinkclouds.jpeg");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Category
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(question.getCategory());
        titleLabel.setFont(MarioKart.deriveFont(Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        titlePanel.setBackground(header);
        titlePanel.setMaximumSize(new Dimension(350, 30));
        mainPanel.add(titlePanel);

        // Server.Question Panel with light blue background and border
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderGUI.THIN_BORDER);
        questionPanel.setBackground(card_background);

        ////Question
        JTextArea questionTextArea = new JTextArea(question.getQuestionText());
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setBackground(button_default);
        questionTextArea.setFont(MarioKart.deriveFont(Font.BOLD, 20));
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
        answerPanel.setBorder(BorderGUI.THIN_BORDER);
        answerPanel.setBackground(transparent);
        answerPanel.setMaximumSize(new Dimension(350, 200));

        String[] options = question.getOptions();
        JButton answerButton1 = new JButton(options[0]);
        answerButton1.setBackground(golden);
        answerButton1.setBorder(BorderGUI.SIMPLE_BORDER);
        answerButton1.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        JButton answerButton2 = new JButton(options[1]);
        answerButton2.setBackground(golden);
        answerButton2.setBorder(BorderGUI.SIMPLE_BORDER);
        answerButton2.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        JButton answerButton3 = new JButton(options[2]);
        answerButton3.setBackground(golden);
        answerButton3.setBorder(BorderGUI.SIMPLE_BORDER);
        answerButton3.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        JButton answerButton4 = new JButton(options[3]);
        answerButton4.setBackground(golden);
        answerButton4.setBorder(BorderGUI.SIMPLE_BORDER);
        answerButton4.setFont(MarioKart.deriveFont(Font.BOLD, 16));

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
                JOptionPane.showMessageDialog(quizPanel, "UH OH! TIME'S UP! GO TO THE NEXT QUESTION!");
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

        JPanel panel = new ImagePanel("src/Client/Resources/Images/pixelneighbour.jpeg");
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        setVisible(true);
        int gridLayoutRounds = yourScoreBoard.getYourScoreBoard().length;
        int questionsPerRound = yourScoreBoard.getYourScoreBoard()[0].length;


        JPanel headerPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 3));
        headerPanel.setOpaque(true);
        headerPanel.setBorder(BorderGUI.THIN_BORDER);

        for (int i = 0; i < questionsPerRound - 1; i++) {
            headerPanel.add(new JLabel());
        }
        JLabel player1Label = new JLabel("PLAYER 1");
        player1Label.setFont(MarioKart.deriveFont(Font.BOLD, 10));
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        player1Label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        headerPanel.add(player1Label);

        JLabel spaceLabel = new JLabel();
        headerPanel.add(spaceLabel);

        JLabel roundLabel = new JLabel("Round");
        roundLabel.setFont(MarioKart.deriveFont(Font.BOLD, 10));
        roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roundLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(roundLabel);

        for (int i = 0; i < questionsPerRound - 1; i++) {
            headerPanel.add(new JLabel());
        }
        JLabel player2Label = new JLabel("PLAYER 2");
        player2Label.setFont(MarioKart.deriveFont(Font.BOLD, 10));
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
                    label.setText("RIGHT");
                    label.setFont(MarioKart.deriveFont(Font.BOLD, 18));
                    label.setBackground(button_correct);
                    label.setBorder(BorderGUI.THIN_BORDER); //TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("WRONG");
                    label.setFont(MarioKart.deriveFont(Font.BOLD, 18));
                    label.setBackground(button_wrong);
                    label.setBorder(BorderGUI.THIN_BORDER);
                } else {
                    label.setText("");
                    label.setBackground(transparent);
//                    label.setBorder(BorderGUI.THIN_BORDER);
                }
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanel.add(label);
            }

            //TODO add label with round number
            JLabel roundNrPanel = new JLabel(String.valueOf((i) + 1));
            rowPanel.add(roundNrPanel);
            roundNrPanel.setHorizontalAlignment(SwingConstants.CENTER);
            roundNrPanel.setFont(MarioKart.deriveFont(Font.BOLD, 58));
            roundNrPanel.setForeground(golden);
//            roundNrPanel.setBorder(BorderGUI.THIN_BORDER);

            //TODO add player 2 row answers
            Integer[] opponentScore = yourScoreBoard.getOpponentScoreBoard()[i];
            for (int j = 0; j < opponentScore.length; j++) {
                int answered = opponentScore[j];
                JLabel label = new JLabel();
                if (answered == RIGHT) {
                    label.setText("RÄTT");
                    label.setFont(MarioKart.deriveFont(Font.BOLD, 18));
                    label.setBackground(button_correct);
                    label.setBorder(BorderGUI.THIN_BORDER);//TODO change to nice picture
                } else if (answered == WRONG) {
                    label.setText("FEL");
                    label.setFont(MarioKart.deriveFont(Font.BOLD, 18));
                    label.setBackground(button_wrong);
                    label.setBorder(BorderGUI.THIN_BORDER);
                } else {
                    label.setText("");
                    label.setBackground(transparent);
//                    label.setBorder(BorderGUI.THIN_BORDER);
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
        JPanel panel = new ImagePanel("src/Client/Resources/Images/pixelneighbour.jpeg");
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
        JLabel finalScoreLabel = new JLabel("RESLUT", SwingConstants.CENTER);
        finalScoreLabel.setFont(MarioKart.deriveFont(Font.BOLD, 24));
        finalScoreLabel.setOpaque(true);
        finalScoreLabel.setBackground(new Color(144, 238, 144));
        finalScoreLabel.setForeground(WHITE);
        panel.add(finalScoreLabel, BorderLayout.NORTH);

        // visa poängställning
        JPanel scoresPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // två rader poäng resultat
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// lite space runt
        scoresPanel.setOpaque(false);

        // Player 1 och Player 2 poäng
        JLabel scoresLabel = new JLabel(
                "YOUR POINTS: " + yourTotalScore + "  -  OPPOSITIONS POINTS: " + opponentTotalScore,
                SwingConstants.CENTER
        );

        scoresLabel.setFont(MarioKart.deriveFont(Font.BOLD, 20));
        scoresLabel.setOpaque(true);
        scoresLabel.setBorder(BorderGUI.THICK_BORDER);
        scoresLabel.setBackground(golden);
        scoresPanel.add(scoresLabel);

        // visa om spelar vinner förlorar eller om det blev samma poäng
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(MarioKart.deriveFont(Font.BOLD, 20));
        resultLabel.setBorder(BorderGUI.THICK_BORDER);
        resultLabel.setOpaque(true);

        // kolla om client vann förlorade eller samma poäng
        if (yourTotalScore > opponentTotalScore) {
            resultLabel.setText("YOU WON!");
            resultLabel.setBackground(button_correct);
        } else if (yourTotalScore < opponentTotalScore) {
            resultLabel.setText("YOU LOST!");
            resultLabel.setBackground(button_wrong);
        } else {
            resultLabel.setText("IT'S A TIE!");
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
        roundPanel.setBorder(BorderGUI.SIMPLE_BORDER);
        roundPanel.setOpaque(false);

        for (int i = 0; i < gridLayoutRounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, questionsPerRound * 2 + 1, 5, 0));
//            rowPanel.setBorder(BorderGUI.SIMPLE_BORDER);
            rowPanel.setOpaque(false);

            // Player 1 poäng
            Integer[] yourScore = finalScoreBoard.getYourScoreBoard()[i];
            for (int j = 0; j < yourScore.length; j++) {
                int answered = yourScore[j];
                JLabel label = createScoreLabel(answered);
                label.setBorder(BorderGUI.THIN_BORDER);
                rowPanel.add(label);
            }

            // Rond number
            JLabel roundNrLabel = new JLabel("R" + (i + 1), SwingConstants.CENTER);
            roundNrLabel.setFont(MarioKart.deriveFont(Font.BOLD, 14));
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
        exitButton.setFont(MarioKart.deriveFont(Font.BOLD, 16));
        exitButton.setBackground(golden);
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
            displayText = "RIGHT";
            backgroundColor = button_correct;
        } else if (score == WRONG) {
            displayText = "WRONG";
            backgroundColor = button_wrong;
        } else {
            backgroundColor = text_subtitle;
        }

        JLabel label = new JLabel(displayText, SwingConstants.CENTER);
        label.setFont(MarioKart.deriveFont(Font.BOLD, 12));
        label.setBorder(BorderGUI.THIN_BORDER);
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


        JLabel waitingForPlayersLabel = new JLabel("WAITING FOR PLAYERS", SwingConstants.CENTER);
        waitingForPlayersLabel.setFont(MarioKart.deriveFont(Font.BOLD, 20));
        waitingForPlayersLabel.setForeground(getHSBColor(0.6f, 0.5f, 0.5f));
        waitingForPlayersLabel.setBackground(WHITE);
        waitingForPlayersLabel.setBorder(BorderGUI.THIN_BORDER);
        waitingForPlayersLabel.setOpaque(true);
        waitingForPlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForPlayersLabel.setMaximumSize(new Dimension(350, 25));
        waitingForPlayersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startPanel.add(waitingForPlayersLabel, BorderLayout.SOUTH);
        revalidate();
    }
}