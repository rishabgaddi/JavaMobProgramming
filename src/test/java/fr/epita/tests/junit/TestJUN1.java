package fr.epita.tests.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class TestJUN1 {
    private static final Logger LOGGER = LogManager.getLogger(TestJUN1.class);

    @Test
    public void test() {
        LOGGER.info("Hi from JUnit");
    }
}
