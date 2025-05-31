package coursework;
import java.sql.*;
import java.util.ArrayList;
/**
 * The CompetitorList class manages a list of Competitor objects.
 * It provides methods to add, retrieve, display, and load competitors from a database.
 * This class also handles saving competitor data into the database.
 */
public class CompetitorList {
    private ArrayList<Competitor> competitors;

    /**
     * Constructs an empty CompetitorList.
     */
    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    /**
     * Retrieves the list of all competitors.
     *
     * @return ArrayList of Competitor objects.
     */
    public ArrayList<Competitor> getCompetitors() {
        return competitors;
    }

    /**
     * Adds a new competitor to the list and saves it to the database.
     *
     * @param competitorID The unique ID of the competitor.
     * @param firstName The first name of the competitor.
     * @param lastName The last name of the competitor.
     * @param level The level of the competitor.
     * @param country The country of the competitor.
     * @param scores An array of 5 integer scores.
     */
    public void addCompetitor(int competitorID, String firstName, String lastName, String level, String country, int[] scores) {
        Competitor competitor = new Competitor(competitorID, firstName, lastName, level, country, scores);
        competitors.add(competitor);
        saveToDatabase(competitor);
    }

    /**
     * Retrieves a competitor by their unique ID.
     *
     * @param id The competitor's ID.
     * @return The Competitor object if found, otherwise null.
     */
    public Competitor getCompetitorByID(int id) {
        for (Competitor c : competitors) {
            if (c.getCompetitorID() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Displays details of all competitors in the list.
     */
    public void displayAllCompetitors() {
        if (competitors.isEmpty()) {
            System.out.println("No competitors found.");
        } else {
            for (Competitor competitor : competitors) {
                competitor.displayCompetitorDetails();
            }
        }
    }

    /**
     * Loads competitor data from the database and populates the list.
     */
    public void loadFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "")) {
            String sql = "SELECT * FROM Competitors";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int competitorID = rs.getInt("competitorID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String level = rs.getString("level");
                String country = rs.getString("country");
                int[] scores = {
                    rs.getInt("score1"),
                    rs.getInt("score2"),
                    rs.getInt("score3"),
                    rs.getInt("score4"),
                    rs.getInt("score5")
                };
                addCompetitor(competitorID, firstName, lastName, level, country, scores);
            }
        } catch (SQLException e) {
            System.out.println("Error loading competitors from the database: " + e.getMessage());
        }
    }

    /**
     * Saves a competitor's details into the database.
     *
     * @param competitor The Competitor object to be saved.
     */
    private void saveToDatabase(Competitor competitor) {
        String sql = "INSERT INTO Competitors (competitorID, firstName, lastName, level, country, score1, score2, score3, score4, score5) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, competitor.getCompetitorID());
            stmt.setString(2, competitor.getFullName().split(" ")[0]);
            stmt.setString(3, competitor.getFullName().split(" ")[1]);
            stmt.setString(4, competitor.getLevel());
            stmt.setString(5, competitor.getCountry());
            int[] scores = competitor.getScores();
            for (int i = 0; i < 5; i++) {
                stmt.setInt(6 + i, scores[i]);
            }
            stmt.executeUpdate();
            System.out.println("Competitor data saved successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving competitor data: " + e.getMessage());
        }
    }
}