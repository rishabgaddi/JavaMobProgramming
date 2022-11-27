package fr.epita.tests.gaddi;

import fr.epita.datamodel.Competitor;
import fr.epita.services.CompetitionDataService;
import fr.epita.services.CompetitorDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class TestDS1 {
    private static final Logger LOGGER = LogManager.getLogger(TestDS1.class);
    private static final File FILE = new File("src/test/resources/contest-tokyo-grand-slam-2017.json");
    private Connection connection;

    @Inject
    @Named("main-ds")
    DataSource ds;

    @Inject
    @Named("competition-data-service")
    CompetitionDataService competitionDataService;

    @BeforeEach
    public void setup() throws SQLException {
        connection = ds.getConnection();
        connection.createStatement().execute("DROP TABLE IF EXISTS COMPETITORS");
        String sql =
                "CREATE TABLE COMPETITORS(" +
                        "id INTEGER auto_increment," +
                        "family_name VARCHAR(255)," +
                        "given_name VARCHAR(255)," +
                        "country VARCHAR(255)," +
                        "weight_category VARCHAR(255)," +
                        "age_category VARCHAR(255)," +
                        "PRIMARY KEY (id)" +
                        ")";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void test() throws SQLException {
        // Fill the database with data from the json file
        CompetitorDAO dao = new CompetitorDAO();
        Set<Competitor> competitors = dao.getCompetitors(FILE);
        for (Competitor competitor : competitors) {
            competitionDataService.create(competitor);
        }
        ResultSet resultSet = connection.prepareStatement("SELECT count(1) as cnt FROM COMPETITORS").executeQuery();
        resultSet.next();
        int count = resultSet.getInt("cnt");
        Assertions.assertEquals(competitors.size(), count);

        // a. Most represented weight category
        String mostRepresentedWeightCategory = competitionDataService.getMostRepresentedWeightCategory();
        resultSet = connection.prepareStatement("SELECT weight_category, COUNT(*) FROM COMPETITORS GROUP BY weight_category ORDER BY COUNT(*) DESC LIMIT 1").executeQuery();
        resultSet.next();
        String weightCategory = resultSet.getString("weight_category");
        Assertions.assertEquals(weightCategory, mostRepresentedWeightCategory);

        // b. Distribution of competitors per country
        Map<String, Integer> distributionOfCompetitorsPerCountry = competitionDataService.distributionOfCompetitorsPerCountry();
        resultSet = connection.prepareStatement("SELECT country, COUNT(*) FROM COMPETITORS GROUP BY country ORDER BY COUNT(*) DESC").executeQuery();
        while (resultSet.next()) {
            String country = resultSet.getString("country");
            int numberOfCompetitors = resultSet.getInt("COUNT(*)");
            Assertions.assertEquals(numberOfCompetitors, distributionOfCompetitorsPerCountry.get(country));
        }
        resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT country) FROM COMPETITORS").executeQuery();
        resultSet.next();
        int numberOfCountries = resultSet.getInt("COUNT(DISTINCT country)");
        Assertions.assertEquals(numberOfCountries, distributionOfCompetitorsPerCountry.size());

        // c. Total number of competitors
        int totalNumberOfCompetitors = competitionDataService.totalNumberOfCompetitors();
        resultSet = connection.prepareStatement("SELECT COUNT(*) FROM COMPETITORS").executeQuery();
        resultSet.next();
        int numberOfCompetitors = resultSet.getInt("COUNT(*)");
        Assertions.assertEquals(numberOfCompetitors, totalNumberOfCompetitors);

        // d. Find competitors by family name
        List<Competitor> competitorsByFamilyName = competitionDataService.findByFamilyName("KIM");
        List<Competitor> result = new ArrayList<>();
        resultSet = connection.prepareStatement("SELECT * FROM COMPETITORS WHERE family_name = 'KIM'").executeQuery();
        while (resultSet.next()) {
            String familyName = resultSet.getString("family_name");
            String givenName = resultSet.getString("given_name");
            String country = resultSet.getString("country");
            String weight_Category = resultSet.getString("weight_category");
            String ageCategory = resultSet.getString("age_category");
            Competitor competitor = new Competitor(familyName, givenName, country, weight_Category, ageCategory);
            int id = resultSet.getInt("id");
            competitor.setId(id);
            result.add(competitor);
        }
        Assertions.assertEquals(result, competitorsByFamilyName);
    }
}
