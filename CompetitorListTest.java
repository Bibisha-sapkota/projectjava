package coursework;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompetitorListTest {
    private CompetitorList competitorList;
    private Competitor competitor1;
    private Competitor competitor2;

    @BeforeEach
    void setUp() {
        
        competitorList = new CompetitorList();

        int[] scores1 = {10, 20, 30, 40, 50};
        competitor1 = new Competitor(1, "John", "Doe", "Beginner", "USA", scores1);
        
        int[] scores2 = {15, 25, 35, 45, 55};
        competitor2 = new Competitor(2, "Jane", "Smith", "Intermediate", "Canada", scores2);

        competitorList.addCompetitor(competitor1.getCompetitorID(), competitor1.getFullName().split(" ")[0], competitor1.getFullName().split(" ")[1], competitor1.getLevel(), competitor1.getCountry(), competitor1.getScores());
        competitorList.addCompetitor(competitor2.getCompetitorID(), competitor2.getFullName().split(" ")[0], competitor2.getFullName().split(" ")[1], competitor2.getLevel(), competitor2.getCountry(), competitor2.getScores());
    }

    @Test
    void testAddCompetitor() {
        
        assertEquals(2, competitorList.getCompetitors().size());
    }

    @Test
    void testGetCompetitorByID() {
       
        Competitor retrievedCompetitor = competitorList.getCompetitorByID(2);
        assertNotNull(retrievedCompetitor);
        assertEquals("Jane Smith", retrievedCompetitor.getFullName());

        
        Competitor nonExistingCompetitor = competitorList.getCompetitorByID(999);
        assertNull(nonExistingCompetitor);
    }

    @Test
    void testGetTotalScoreForCompetitor() {
        assertEquals(150, competitor1.getTotalScore());
        assertEquals(175, competitor2.getTotalScore());
    }
}
