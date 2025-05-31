package coursework;




import java.sql.*;

public class AdminAuthenticator {

    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false; 
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Admin WHERE username=? AND password=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

