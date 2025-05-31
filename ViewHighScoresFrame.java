package coursework;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewHighScoresFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewHighScoresFrame() {
        setTitle("High Scores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80)); 
        setContentPane(mainPanel);

        
        JLabel lblTitle = new JLabel("🏆 High Scores");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(255, 215, 0)); // Gold
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        
        String[] columns = { "Player ID", "Total Score" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setEnabled(false); 
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 152, 219)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(new Color(236, 240, 241)); 
        table.setSelectionBackground(new Color(189, 195, 199)); 

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        
        JButton btnLoadHighScores = new JButton("🔄 Refresh");
        btnLoadHighScores.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLoadHighScores.setPreferredSize(new Dimension(150, 40));
        btnLoadHighScores.setBackground(new Color(46, 204, 113)); 
        btnLoadHighScores.setForeground(Color.WHITE);
        btnLoadHighScores.setBorder(null);
        btnLoadHighScores.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        btnLoadHighScores.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLoadHighScores.setBackground(new Color(39, 174, 96)); 
            }
            public void mouseExited(MouseEvent evt) {
                btnLoadHighScores.setBackground(new Color(46, 204, 113)); 
            }
        });

        btnLoadHighScores.addActionListener(e -> loadHighScores());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.add(btnLoadHighScores);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        loadHighScores();
    }

    private void loadHighScores() {
        tableModel.setRowCount(0); 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "")) {
            String sql = "SELECT competitorID, (score1 + score2 + score3 + score4 + score5) AS total_score " +
                         "FROM Competitors ORDER BY total_score DESC LIMIT 10"; 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int competitorId = rs.getInt("competitorID"); 
                int totalScore = rs.getInt("total_score");
                tableModel.addRow(new Object[] { competitorId, totalScore });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading high scores: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewHighScoresFrame());
    }
}
