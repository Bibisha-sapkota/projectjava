package coursework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    /**
     * The main method establishes a connection to MySQL, creates the database,
     * and initializes tables for the quiz competition.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";  
        String user = "root"; 
        String password = "";  
        String dbName = "CompetitionDB"; 

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish initial connection to MySQL
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            // Create database if it does not exist
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbName;
            stmt.executeUpdate(createDatabase);
            System.out.println("Database '" + dbName + "' created or already exists.");

            stmt.close();
            conn.close();

            // Connect to the newly created database
            String newUrl = "jdbc:mysql://localhost:3306/" + dbName;
            conn = DriverManager.getConnection(newUrl, user, password);
            stmt = conn.createStatement();

            // Drop and recreate QuizQuestions table
            stmt.executeUpdate("DROP TABLE IF EXISTS QuizQuestions");
            String createQuizTable = "CREATE TABLE QuizQuestions ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "question TEXT NOT NULL,"
                    + "option1 VARCHAR(255) NOT NULL,"
                    + "option2 VARCHAR(255) NOT NULL,"
                    + "option3 VARCHAR(255) NOT NULL,"
                    + "option4 VARCHAR(255) NOT NULL,"
                    + "correct_option INT NOT NULL,"
                    + "difficulty ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL"
                    + ")";
            stmt.executeUpdate(createQuizTable);
            System.out.println("Table 'QuizQuestions' recreated successfully.");

            // Drop and recreate Competitors table
            stmt.executeUpdate("DROP TABLE IF EXISTS Competitors");
            String createCompetitorsTable = "CREATE TABLE Competitors ("
                    + "competitorID INT AUTO_INCREMENT PRIMARY KEY,"
                    + "firstName VARCHAR(50) NOT NULL,"
                    + "lastName VARCHAR(50) NOT NULL,"
                    + "level ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL,"
                    + "country VARCHAR(50) NOT NULL DEFAULT 'Unknown',"
                    + "score1 INT DEFAULT 0,"
                    + "score2 INT DEFAULT 0,"
                    + "score3 INT DEFAULT 0,"
                    + "score4 INT DEFAULT 0,"
                    + "score5 INT DEFAULT 0"
                    + ")";
            stmt.executeUpdate(createCompetitorsTable);
            System.out.println("Table 'Competitors' recreated successfully.");

            // Create Admin table if not exists
            String createAdminTable = "CREATE TABLE IF NOT EXISTS Admin ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password VARCHAR(255) NOT NULL"
                    + ")";
            stmt.executeUpdate(createAdminTable);
            System.out.println("Table 'Admin' created or already exists.");

            // Create Players table if not exists
            String createPlayersTable = "CREATE TABLE IF NOT EXISTS Players ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "score INT NOT NULL"
                    + ")";
            stmt.executeUpdate(createPlayersTable);
            System.out.println("Table 'Players' created or already exists.");

            // Insert default admin credentials if not already present
            String insertAdmin = "INSERT IGNORE INTO Admin (id, username, password) VALUES (1, 'ADMIN', 'quizgame')";
            stmt.executeUpdate(insertAdmin);
            System.out.println("Default admin inserted (if not already exists).");

            stmt.close();
            conn.close();
            System.out.println("Setup complete.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
