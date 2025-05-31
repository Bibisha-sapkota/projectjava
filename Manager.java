/**
 * The Manager class provides a console-based competitor management system.
 * It allows users to generate reports, find the top performer, generate statistics,
 * and search for competitors by ID.
 */
package coursework;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Scanner;

public class Manager {
    private CompetitorList competitorList;
    private Scanner scanner;

    /**
     * Constructs a Manager object, initializes the CompetitorList,
     * loads data from the database, and sets up the scanner.
     */
    public Manager() {
        competitorList = new CompetitorList();
        competitorList.loadFromDatabase(); 
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the Competitor Management System and displays menu options.
     */
    public void start() {
        while (true) {
            System.out.println("\n==== Competitor Management System ====");
            System.out.println("1. Generate Full Report");
            System.out.println("2. Display Top Performer");
            System.out.println("3. Generate Statistics");
            System.out.println("4. Search Competitor by ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    generateFullReport();
                    break;
                case 2:
                    displayTopPerformer();
                    break;
                case 3:
                    generateStatistics();
                    break;
                case 4:
                    searchCompetitorByID();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Generates and displays a full report of all competitors.
     */
    private void generateFullReport() {
        System.out.println("\n---- Full Report of Competitors ----");
        competitorList.displayAllCompetitors();
    }

    /**
     * Identifies and displays the competitor with the highest total score.
     */
    private void displayTopPerformer() {
        Competitor topPerformer = null;
        int highestScore = -1;

        for (Competitor competitor : competitorList.getCompetitors()) {
            int totalScore = competitor.getTotalScore();
            if (totalScore > highestScore) {
                highestScore = totalScore;
                topPerformer = competitor;
            }
        }

        if (topPerformer != null) {
            System.out.println("\n---- Top Performer ----");
            System.out.println("Competitor ID: " + topPerformer.getCompetitorID());
            System.out.println("Name: " + topPerformer.getFullName());
            System.out.println("Total Score: " + highestScore);
        } else {
            System.out.println("No competitors found.");
        }
    }

    /**
     * Generates statistical summaries including total competitors,
     * highest average score, and score distribution.
     */
    private void generateStatistics() {
        int competitorCount = competitorList.getCompetitors().size();
        if (competitorCount == 0) {
            System.out.println("No competitors available for statistics.");
            return;
        }

        Competitor topPerformer = null;
        double highestAvgScore = -1;
        Map<Integer, Integer> scoreFrequency = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            scoreFrequency.put(i, 0);
        }

        for (Competitor competitor : competitorList.getCompetitors()) {
            int[] scores = competitor.getScores();
            double avgScore = Arrays.stream(scores).average().orElse(0);

            if (avgScore > highestAvgScore) {
                highestAvgScore = avgScore;
                topPerformer = competitor;
            }

            for (int score : scores) {
                scoreFrequency.put(score, scoreFrequency.getOrDefault(score, 0) + 1);
            }
        }

        System.out.println("\n---- Statistical Summary ----");
        System.out.println("Total number of competitors: " + competitorCount);
        if (topPerformer != null) {
            System.out.println("Competitor with the highest score: " +
                    topPerformer.getFullName() + " with an overall score of " + highestAvgScore);
        }
        System.out.println("\nFrequency of individual scores:");
        System.out.println("Score:\t   " + scoreFrequency.keySet());
        System.out.println("Frequency: " + scoreFrequency.values());
    }

    /**
     * Searches for a competitor by their ID and displays their details.
     */
    private void searchCompetitorByID() {
        System.out.print("Enter Competitor ID to Search: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        Competitor competitor = competitorList.getCompetitorByID(id);
        if (competitor != null) {
            System.out.println("\n---- Competitor Found ----");
            System.out.println("Competitor ID: " + competitor.getCompetitorID());
            System.out.println("Full Name: " + competitor.getFullName());
            System.out.println("Level: " + competitor.getLevel());
            System.out.println("Country: " + competitor.getCountry());
            System.out.println("Scores: ");
            for (int i = 0; i < 5; i++) {
                System.out.println("Score " + (i + 1) + ": " + competitor.getScores()[i]);
            }
        } else {
            System.out.println("Competitor with ID " + id + " not found.");
        }
    }

    /**
     * Main method to initialize and start the competitor management system.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.start();
    }
}
