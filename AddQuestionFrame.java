package coursework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddQuestionFrame extends JFrame {
    private JTextField txtQuestion, txtOption1, txtOption2, txtOption3, txtOption4;
    private JComboBox<String> cbLevel, cbCorrectOption;

    public AddQuestionFrame() {
        setTitle("Add New Question");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(244, 246, 246)); 
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        formPanel.setPreferredSize(new Dimension(450, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel lblTitle = new JLabel("Add New Question");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 120, 108));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Question:"), gbc);
        
        gbc.gridx = 1;
        txtQuestion = new JTextField();
        txtQuestion.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtQuestion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Option 1:"), gbc);

        gbc.gridx = 1;
        txtOption1 = new JTextField();
        formPanel.add(txtOption1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Option 2:"), gbc);

        gbc.gridx = 1;
        txtOption2 = new JTextField();
        formPanel.add(txtOption2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Option 3:"), gbc);

        gbc.gridx = 1;
        txtOption3 = new JTextField();
        formPanel.add(txtOption3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Option 4:"), gbc);

        gbc.gridx = 1;
        txtOption4 = new JTextField();
        formPanel.add(txtOption4, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Difficulty:"), gbc);

        gbc.gridx = 1;
        cbLevel = new JComboBox<>(new String[] {"Beginner", "Intermediate", "Advanced"});
        formPanel.add(cbLevel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Correct Option:"), gbc);

        gbc.gridx = 1;
        cbCorrectOption = new JComboBox<>(new String[] {"1", "2", "3", "4"});
        formPanel.add(cbCorrectOption, gbc);

        // Save Button
        JButton btnSave = new JButton("Save Question");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.setBackground(new Color(93, 163, 250)); // Soft blue
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorder(null);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSave.setBackground(new Color(64, 123, 255)); // Darker blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSave.setBackground(new Color(93, 163, 250)); // Original pastel blue
            }
        });

        btnSave.addActionListener(e -> addQuestion());

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(btnSave, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void addQuestion() {
        String question = txtQuestion.getText();
        String option1 = txtOption1.getText();
        String option2 = txtOption2.getText();
        String option3 = txtOption3.getText();
        String option4 = txtOption4.getText();
        String level = (String) cbLevel.getSelectedItem();
        int correctOption = Integer.parseInt((String) cbCorrectOption.getSelectedItem());

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert question into the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "")) {
            String sql = "INSERT INTO QuizQuestions (question, option1, option2, option3, option4, correct_option, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, question);
            stmt.setString(2, option1);
            stmt.setString(3, option2);
            stmt.setString(4, option3);
            stmt.setString(5, option4);
            stmt.setInt(6, correctOption);
            stmt.setString(7, level);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields after adding a question
            txtQuestion.setText("");
            txtOption1.setText("");
            txtOption2.setText("");
            txtOption3.setText("");
            txtOption4.setText("");
            cbLevel.setSelectedIndex(0);
            cbCorrectOption.setSelectedIndex(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding question: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}