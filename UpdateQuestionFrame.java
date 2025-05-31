package coursework;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateQuestionFrame extends JFrame {
    private JTextField txtQuestionID, txtQuestion, txtOption1, txtOption2, txtOption3, txtOption4;
    private JComboBox<String> cbLevel, cbCorrectOption;

    public UpdateQuestionFrame() {
        setTitle("Update Question");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 246, 246));
        setContentPane(mainPanel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        formPanel.setPreferredSize(new Dimension(400, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel lblTitle = new JLabel("Update Question");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 136, 229));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Fields
        String[] labels = { "Question ID:", "Question:", "Option 1:", "Option 2:", "Option 3:", "Option 4:", "Difficulty:", "Correct Option:" };
        JComponent[] inputs = {
            txtQuestionID = new JTextField(),
            txtQuestion = new JTextField(),
            txtOption1 = new JTextField(),
            txtOption2 = new JTextField(),
            txtOption3 = new JTextField(),
            txtOption4 = new JTextField(),
            cbLevel = new JComboBox<>(new String[] { "Beginner", "Intermediate", "Advanced" }),
            cbCorrectOption = new JComboBox<>(new String[] { "1", "2", "3", "4" })
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            formPanel.add(inputs[i], gbc);
        }

        // Update Button
        JButton btnUpdate = new JButton("Update Question");
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnUpdate.setPreferredSize(new Dimension(200, 40));
        btnUpdate.setBackground(new Color(30, 136, 229));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUpdate.setBackground(new Color(25, 118, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUpdate.setBackground(new Color(30, 136, 229));
            }
        });

        btnUpdate.addActionListener(e -> updateQuestion());

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        formPanel.add(btnUpdate, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void updateQuestion() {
        String idText = txtQuestionID.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question ID!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int questionID = Integer.parseInt(idText);
            String question = txtQuestion.getText();
            String option1 = txtOption1.getText();
            String option2 = txtOption2.getText();
            String option3 = txtOption3.getText();
            String option4 = txtOption4.getText();
            String level = (String) cbLevel.getSelectedItem();
            int correctOption = Integer.parseInt((String) cbCorrectOption.getSelectedItem());

            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to update this question?", 
                "Confirm Update", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Connect to the database and update the question
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "")) {
                    String sql = "UPDATE QuizQuestions SET question = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, correct_option = ?, difficulty = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, question);
                    stmt.setString(2, option1);
                    stmt.setString(3, option2);
                    stmt.setString(4, option3);
                    stmt.setString(5, option4);
                    stmt.setInt(6, correctOption);
                    stmt.setString(7, level);
                    stmt.setInt(8, questionID);

                    
                    System.out.println("Executing SQL: " + sql);
                    System.out.println("Parameters: " + question + ", " + option1 + ", " + option2 + ", " + option3 + ", " + option4 + ", " + correctOption + ", " + level + ", " + questionID);

                    int rowsAffected = stmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Question updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No question found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                    txtQuestionID.setText("");
                    txtQuestion.setText("");
                    txtOption1.setText("");
                    txtOption2.setText("");
                    txtOption3.setText("");
                    txtOption4.setText("");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating question: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID! Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
