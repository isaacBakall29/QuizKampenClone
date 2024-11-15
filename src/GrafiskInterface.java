import javax.swing.*;
import java.awt.*;

public class GrafiskInterface extends JFrame {

    public GrafiskInterface() {
        setTitle("Quiz Kampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //meaning components added to this panel will be arranged vertically (from top to bottom).
        add(mainPanel, BorderLayout.CENTER);

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

        // Add buttons to the answer panel
        answerPanel.add(answerButton1);
        answerPanel.add(answerButton2);
        answerPanel.add(answerButton3);
        answerPanel.add(answerButton4);

        // Add answerPanel to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(answerPanel);

        // Progress Bar Panel
        JPanel progressPanel = new JPanel();
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(20);  // Example progress value
        progressPanel.add(progressBar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(progressPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GrafiskInterface();
    }
}
