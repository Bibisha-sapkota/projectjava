
package coursework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Competitor {
    /** The unique ID of the competitor. */
    private int competitorID;
    /** The first name of the competitor. */
    private String firstName;
    /** The last name of the competitor. */
    private String lastName;
    /** The competition level of the competitor. */
    private String level;
    /** The country of the competitor. */
    private String country;
    /** The scores achieved by the competitor in various rounds. */
    private int[] scores;

    /**
     * Constructs a new {@code Competitor} instance with the specified details.
     * 
     * @param competitorID the unique ID of the competitor
     * @param firstName the first name of the competitor
     * @param lastName the last name of the competitor
     * @param level the competition level of the competitor
     * @param country the country of the competitor
     * @param scores an array of scores achieved by the competitor
     */
    public Competitor(int competitorID, String firstName, String lastName, String level, String country, int[] scores) {
        this.competitorID = competitorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.country = country;
        this.scores = scores;
    }

    /**
     * Returns the competitor's unique ID.
     * 
     * @return the competitor ID
     */
    public int getCompetitorID() {
        return competitorID;
    }

    /**
     * Returns the full name of the competitor.
     * 
     * @return the full name as "FirstName LastName"
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the competition level of the competitor.
     * 
     * @return the competition level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Returns the country of the competitor.
     * 
     * @return the competitor's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the scores achieved by the competitor.
     * 
     * @return an array of scores
     */
    public int[] getScores() {
        return scores;
    }

    /**
     * Calculates and returns the total score of the competitor.
     * 
     * @return the total score
     */
    public int getTotalScore() {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return total;
    }

    /**
     * Displays the competitor's details, including ID, full name, level, country,
     * and scores in a formatted manner.
     */
    public void displayCompetitorDetails() {
        System.out.println("Competitor ID: " + competitorID);
        System.out.println("Full Name: " + getFullName());
        System.out.println("Level: " + level);
        System.out.println("Country: " + country);
        System.out.print("Scores: ");
        for (int i = 0; i < scores.length; i++) {
            System.out.print("Score " + (i + 1) + ": " + scores[i] + " ");
        }
        System.out.println();
    }

    /**
     * Saves the competitor's details to a MySQL database.
     * Assumes a database named "CompetitionDB" with a table "Competitors" exists.
     * The table should have columns: competitorID, firstName, lastName, level,
     * country, score1, score2, score3, score4, and score5.
     */
    public void saveToDatabase() {
        String sql = "INSERT INTO Competitors (competitorID, firstName, lastName, level, country, score1, score2, score3, score4, score5) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.competitorID);
            stmt.setString(2, this.firstName);
            stmt.setString(3, this.lastName);
            stmt.setString(4, this.level);
            stmt.setString(5, this.country);
            stmt.setInt(6, this.scores[0]);
            stmt.setInt(7, this.scores[1]);
            stmt.setInt(8, this.scores[2]);
            stmt.setInt(9, this.scores[3]);
            stmt.setInt(10, this.scores[4]);

            stmt.executeUpdate();
            System.out.println("Competitor data saved successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving competitor data: " + e.getMessage());
        }
    }
}