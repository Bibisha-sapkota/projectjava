package coursework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteQuestionFrame extends JFrame {
    private JTextField txtQuestionID;

    public DeleteQuestionFrame() {
        setTitle("Delete Question");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
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
        formPanel.setPreferredSize(new Dimension(350, 150));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel lblTitle = new JLabel("Delete Question");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(200, 50, 50)); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Enter Question ID:"), gbc);

        
        gbc.gridx = 1;
        txtQuestionID = new JTextField();
        txtQuestionID.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtQuestionID, gbc);

        
        JButton btnDelete = new JButton("Delete Question");
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDelete.setPreferredSize(new Dimension(200, 40));
        btnDelete.setBackground(new Color(220, 53, 69)); // Soft red
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBorder(null);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(200, 30, 50)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDelete.setBackground(new Color(220, 53, 69)); 
            }
        });

        btnDelete.addActionListener(e -> deleteQuestion());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(btnDelete, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void deleteQuestion() {
        String idText = txtQuestionID.getText();

        // Check if input is empty
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question ID!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int questionID = Integer.parseInt(idText);

            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this question?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Connect to the database and delete the question
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "")) {
                    String sql = "DELETE FROM QuizQuestions WHERE id = ?";  
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, questionID);
                    int rowsAffected = stmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Question deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No question found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                    txtQuestionID.setText("");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting question: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID! Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
