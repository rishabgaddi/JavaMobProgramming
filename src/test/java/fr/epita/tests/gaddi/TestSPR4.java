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
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class TestSPR4 {
    private static final Logger LOGGER = LogManager.getLogger(TestSPR4.class);

    @Inject
    @Named("main-ds")
    DataSource dataSource;

    @Test
    public void testDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        String schema = connection.getSchema();

        LOGGER.info("Connection - {}", connection);
        LOGGER.info("Schema - {}", schema);

        Assertions.assertEquals("PUBLIC", schema);
        LOGGER.info("Database is ready to use");
        connection.close();
    }
}
