package coursework;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminPanel extends JFrame {
    private JPanel contentPane;

    public AdminPanel() {
        setTitle("Admin Homepage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(240, 240, 240));
        setContentPane(contentPane);

        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 255, 0));
        headerPanel.setPreferredSize(new Dimension(800, 70));

        JLabel headerLabel = new JLabel("Welcome, Admin!", JLabel.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10));  
        buttonPanel.setBackground(new Color(128, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

       
        String[] buttonNames = {
            "Add Question", "Delete Question", "Update Question",
            "View Player Details", "View High Scores", "Play Quiz", "Logout"
        };

        ActionListener[] buttonActions = {
            e -> new AddQuestionFrame().setVisible(true),
            e -> new DeleteQuestionFrame().setVisible(true),
            e -> new UpdateQuestionFrame().setVisible(true),
            e -> new ViewPlayerDetailsFrame().setVisible(true),
            e -> new ViewHighScoresFrame().setVisible(true),
            e -> new PlayerLoginFrame().setVisible(true),  // Opens Player Login
            e -> {
                dispose();
                new AdminLogin().setVisible(true);
            }
        };

        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = new JButton(buttonNames[i]);
            button.setFont(new Font("SansSerif", Font.BOLD, 16));
            button.setBackground(new Color(93, 163, 250));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(64, 123, 255));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(93, 163, 250));
                }
            });

            button.addActionListener(buttonActions[i]);
            buttonPanel.add(button);
        }

        contentPane.add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminPanel adminHomepage = new AdminPanel();
            adminHomepage.setVisible(true);
        });
    }
}

