import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GrafiskInterface extends JFrame {

    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private int score = 0; //Start score from 0
    private JPanel finalScorePanel;

    public GrafiskInterface() {
        setTitle("Quiz Kampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel = createStartPanel();
        quizPanel = createQuizPanel();

        setContentPane(startPanel);
        setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("QuizKampen");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel,BorderLayout.NORTH);

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

        panel.add(buttonPanel,BorderLayout.CENTER);

        return panel;
    }
    private JPanel createQuizPanel() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //meaning components added to this panel will be arranged vertically (from top to bottom).
        //add(mainPanel, BorderLayout.CENTER);

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Historiens vingslag");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel);

        // Question Panel with light blue background and border
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        questionPanel.setBackground(new Color(255, 255, 224)); // Light yellow background

        JLabel questionLabel = new JLabel("Vem var kung i Sverige 1656?");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel, BorderLayout.CENTER);

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

        JButton answerButton1 = new JButton("Gustav II Adolf");
        JButton answerButton2 = new JButton("Karl XI");
        JButton answerButton3 = new JButton("Karl X Gustav");
        JButton answerButton4 = new JButton("Karl XII");

        String correctAnswer = "Karl XI";

        //Each button is linked to an ActionListener that checks if the clicked answer is correct.
        addAnswerButtonListener(answerButton1, correctAnswer);
        addAnswerButtonListener(answerButton2, correctAnswer);
        addAnswerButtonListener(answerButton3, correctAnswer);
        addAnswerButtonListener(answerButton4, correctAnswer);

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
        scorePanel.add(scoreLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        mainPanel.add(scorePanel);

        return mainPanel;
    }

    private void addAnswerButtonListener(JButton button, String correctAnswer) {
        button.addActionListener(e -> handleAnswerSelection(button, correctAnswer));
    }

    private void handleAnswerSelection(JButton selectedButton, String correctAnswer) {
        if (selectedButton.getText().equals(correctAnswer)) {
            selectedButton.setBackground(Color.GREEN); // Highlight correct answer
            score++; // Increment score
        } else {
            selectedButton.setBackground(Color.RED); // Highlight incorrect answer
        }
        scoreLabel.setText("Poäng: " + score); // Update score label
    }

    public static void main(String[] args) {
        new GrafiskInterface();
    }
}