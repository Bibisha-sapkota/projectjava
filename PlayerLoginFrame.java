package coursework;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PlayerLoginFrame extends JFrame {
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtCountry;
    private JComboBox<String> cmbLevel;

    public PlayerLoginFrame() {
        setTitle("Player Login");
        setSize(400, 350);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(42, 87, 141));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        
        JLabel lblTitle = new JLabel("Player Login");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 30))); 

        
        JLabel lblFirstName = new JLabel("First Name");
        lblFirstName.setForeground(Color.WHITE);
        lblFirstName.setFont(new Font("Verdana", Font.PLAIN, 16));
        panel.add(lblFirstName);
        txtFirstName = createTextField();
        panel.add(txtFirstName);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        
        JLabel lblLastName = new JLabel("Last Name");
        lblLastName.setForeground(Color.WHITE);
        lblLastName.setFont(new Font("Verdana", Font.PLAIN, 16));
        panel.add(lblLastName);
        txtLastName = createTextField();
        panel.add(txtLastName);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        
        JLabel lblCountry = new JLabel("Country");
        lblCountry.setForeground(Color.WHITE);
        lblCountry.setFont(new Font("Verdana", Font.PLAIN, 16));
        panel.add(lblCountry);
        txtCountry = createTextField();
        panel.add(txtCountry);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        
        JLabel lblLevel = new JLabel("Select Level");
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setFont(new Font("Verdana", Font.PLAIN, 16));
        panel.add(lblLevel);

        cmbLevel = new JComboBox<>(new String[] {"Beginner", "Intermediate", "Advanced"});
        cmbLevel.setFont(new Font("Verdana", Font.PLAIN, 16));
        cmbLevel.setPreferredSize(new Dimension(250, 35));
        cmbLevel.setBackground(Color.WHITE);
        cmbLevel.setFocusable(false);

        JPanel levelPanel = new JPanel(new BorderLayout());
        levelPanel.setOpaque(false);
        levelPanel.add(cmbLevel, BorderLayout.CENTER);
        panel.add(levelPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); 

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Verdana", Font.BOLD, 18));
        btnLogin.setBackground(new Color(70, 130, 180)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(250, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setOpaque(true);

        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLogin.setBackground(new Color(55, 95, 150)); 
            }
            public void mouseExited(MouseEvent evt) {
                btnLogin.setBackground(new Color(70, 130, 180)); 
            }
        });

        btnLogin.addActionListener(e -> handleLogin());

        panel.add(btnLogin);

        
        add(panel);
        setVisible(true);
    }

    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Verdana", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(250, 35));
        textField.setBackground(new Color(255, 255, 255));
        textField.setForeground(new Color(70, 130, 180)); 
        textField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true)); 
        return textField;
    }

    // Handle login and player validation
    private void handleLogin() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String playerLevel = cmbLevel.getSelectedItem().toString();
        String playerCountry = txtCountry.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || playerCountry.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all the fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Competitors WHERE firstName = ? AND lastName = ?")) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int playerID = rs.getInt("competitorID");
                System.out.println("Player exists. Moving to game frame.");
                
               
                PlayerGameFrame gameFrame = new PlayerGameFrame(playerID, firstName + " " + lastName, playerLevel, playerCountry);
                gameFrame.setVisible(true);  
                this.dispose();  

            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO Competitors (firstName, lastName, level, country, score1, score2, score3, score4, score5) VALUES (?, ?, ?, ?, 0, 0, 0, 0, 0)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, firstName);
                insertStmt.setString(2, lastName);
                insertStmt.setString(3, playerLevel);
                insertStmt.setString(4, playerCountry);
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int playerID = generatedKeys.getInt(1);
                    System.out.println("New player added. Moving to game frame.");
                    
                    
                    PlayerGameFrame gameFrame = new PlayerGameFrame(playerID, firstName + " " + lastName, playerLevel, playerCountry);
                    gameFrame.setVisible(true);  
                    this.dispose();  
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerLoginFrame());
    }
}

