package fr.epita.tests.gaddi;

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
public class TestSPR2 {
    private static final Logger LOGGER = LogManager.getLogger(TestSPR2.class);
    @Inject
    @Named("my-first-bean")
    String myFirstBean;

    @Test
    public void test() {
        Assertions.assertNotNull(myFirstBean);
        LOGGER.info(myFirstBean);
    }
}
