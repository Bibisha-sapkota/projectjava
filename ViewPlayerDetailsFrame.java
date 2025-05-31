package coursework;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewPlayerDetailsFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewPlayerDetailsFrame() {
        setTitle("Player Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80)); // Dark Blue-Grey
        setContentPane(mainPanel);

        
        JLabel lblTitle = new JLabel("🎮 Player Details");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(255, 215, 0)); // Gold
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        
        String[] columns = { "Player ID", "Name", "Score" };
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

        
        JButton btnLoadPlayers = new JButton("🔄 Refresh");
        btnLoadPlayers.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLoadPlayers.setPreferredSize(new Dimension(150, 40));
        btnLoadPlayers.setBackground(new Color(46, 204, 113)); 
        btnLoadPlayers.setForeground(Color.WHITE);
        btnLoadPlayers.setBorder(null);
        btnLoadPlayers.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        btnLoadPlayers.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLoadPlayers.setBackground(new Color(39, 174, 96)); 
            }
            public void mouseExited(MouseEvent evt) {
                btnLoadPlayers.setBackground(new Color(46, 204, 113)); 
            }
        });

        btnLoadPlayers.addActionListener(e -> loadPlayerDetails());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.add(btnLoadPlayers);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        loadPlayerDetails();
    }

    private void loadPlayerDetails() {
        tableModel.setRowCount(0); 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/competitiondb", "root", "")) {
            
            String sql = "SELECT competitorID, firstName, lastName, (score1 + score2 + score3 + score4 + score5) AS total_score " +
                         "FROM Competitors"; 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int competitorId = rs.getInt("competitorID"); // Correct column name: 'competitorID'
                String playerName = rs.getString("firstName") + " " + rs.getString("lastName"); // Concatenating first and last names
                int totalScore = rs.getInt("total_score"); // Total score calculation
                tableModel.addRow(new Object[] { competitorId, playerName, totalScore });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading player details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewPlayerDetailsFrame::new);
    }
}
