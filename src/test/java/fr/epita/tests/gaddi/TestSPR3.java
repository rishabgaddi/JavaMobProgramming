package fr.epita.tests.gaddi;

import fr.epita.datamodel.Competitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import javax.inject.Named;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class TestSPR3 {
    private static final Logger LOGGER = LogManager.getLogger(TestSPR3.class);

    @Inject
    @Named("create-competitor")
    Competitor competitor;

    @Test
    public void test() {
        Assertions.assertNotNull(competitor);
        Assertions.assertEquals("Gaddi", competitor.getFamilyName());
        LOGGER.info(competitor);
    }
}
