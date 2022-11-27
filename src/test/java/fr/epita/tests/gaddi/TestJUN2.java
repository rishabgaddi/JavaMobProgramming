package fr.epita.tests.gaddi;

import fr.epita.datamodel.Competitor;
import fr.epita.services.CompetitorDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

public class TestJUN2 {
    private static final Logger LOGGER = LogManager.getLogger(TestJUN2.class);
    private static final File FILE = new File("src/test/resources/contest-tokyo-grand-slam-2017.json");
    private CompetitorDAO competitorDAO;
    private Set<Competitor> competitors;

    @BeforeEach
    public void setup() {
        competitorDAO = new CompetitorDAO();
        competitors = competitorDAO.getCompetitors(FILE);
    }

    @Test
    public void test() {
        for (Competitor competitor : competitors) {
            LOGGER.info(competitor);
            Assertions.assertNotNull(competitor);
        }
        Assertions.assertEquals(competitors.size(), 370);
    }
}
