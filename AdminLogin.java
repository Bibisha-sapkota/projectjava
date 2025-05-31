package coursework;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminLogin frame = new AdminLogin();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminLogin() {
        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(93, 163, 250);
                Color color2 = new Color(44, 120, 220);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setBackground(new Color(0, 255, 255));
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(38, 23, 392, 307);
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(0, 255, 0));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setOpaque(true);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        backgroundPanel.add(loginPanel);

       
        JLabel lblHeader = new JLabel("Admin Login");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHeader.setForeground(new Color(44, 120, 108)); 
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBounds(0, 10, 380, 30);
        loginPanel.add(lblHeader);

        
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblUsername.setBounds(30, 60, 100, 25);
        loginPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBounds(140, 60, 200, 30);
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        loginPanel.add(txtUsername);

        // Password Label & Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblPassword.setBounds(30, 110, 100, 25);
        loginPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBounds(140, 110, 200, 30);
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        loginPanel.add(txtPassword);

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setBounds(140, 170, 200, 35);
        btnLogin.setBackground(new Color(93, 163, 250)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(null);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(64, 123, 255)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(93, 163, 250)); 
            }
        });

        btnLogin.addActionListener(e -> authenticateAdmin());
        loginPanel.add(btnLogin);
    }

    private void authenticateAdmin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", ""); 
            String sql = "SELECT * FROM Admin WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminPanel().setVisible(true); 
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
