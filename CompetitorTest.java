package coursework;
import java.util.ArrayList;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompetitorTest {
    private Competitor competitor1;
    private Competitor competitor2;

    @BeforeEach
    void setUp() {
        
        int[] scores1 = {10, 20, 30, 40, 50};
        competitor1 = new Competitor(1, "John", "Doe", "Beginner", "USA", scores1);

        int[] scores2 = {15, 25, 35, 45, 55};
        competitor2 = new Competitor(2, "Jane", "Smith", "Intermediate", "Canada", scores2);
    }

    @Test
    void testGetFullName() {
        
        assertEquals("John Doe", competitor1.getFullName());
        assertEquals("Jane Smith", competitor2.getFullName());
    }

    @Test
    void testGetTotalScore() {
        
        assertEquals(150, competitor1.getTotalScore());
        assertEquals(175, competitor2.getTotalScore());
    }

    @Test
    void testDisplayCompetitorDetails() {
        assertNotNull(competitor1.getCompetitorID());
        assertNotNull(competitor1.getFullName());
    }
}
