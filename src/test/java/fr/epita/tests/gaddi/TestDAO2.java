package fr.epita.tests.gaddi;

import fr.epita.datamodel.Competitor;
import fr.epita.services.ICompetitorDAO;
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
import java.sql.*;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public class TestDAO2 {
    private Connection connection;

    @Inject
    @Named("main-ds")
    DataSource ds;

    @Inject
    @Named("competitor-jdbc-dao")
    ICompetitorDAO competitorJDBCDAO;

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
    public void testCreate() throws SQLException {
        // given
        Competitor competitor1 = new Competitor("GALBADRAKH", "Otgontsetseg", "Kazakhstan", "52", "18-23");
        Competitor competitor2 = new Competitor("Gaddi", "Rishab", "India", "60", "18-23");

        // when
        competitorJDBCDAO.create(competitor1);
        competitorJDBCDAO.create(competitor2);

        // then
        ResultSet resultSet = connection.prepareStatement("SELECT count(1) as cnt FROM COMPETITORS").executeQuery();
        resultSet.next();
        int cnt = resultSet.getInt("cnt");
        Assertions.assertEquals(2, cnt);
    }

    @Test
    public void testUpdate() throws SQLException {
        // given
        Competitor competitor = new Competitor("GALBADRAKH", "Otgontsetseg", "Kazakhstan", "52", "18-23");
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COMPETITORS(family_name, given_name, country, weight_category, age_category) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, competitor.getFamilyName());
        preparedStatement.setString(2, competitor.getGivenName());
        preparedStatement.setString(3, competitor.getCountry());
        preparedStatement.setString(4, competitor.getWeightCategory());
        preparedStatement.setString(5, competitor.getAgeCategory());
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int id = generatedKeys.getInt("id");
        competitor.setId(id);

        // when
        competitor.setCountry("Mongolia");
        competitor.setWeightCategory("57");
        competitor.setAgeCategory("Seniors");
        competitorJDBCDAO.update(competitor);

        // then
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM COMPETITORS WHERE id = " + competitor.getId()).executeQuery();
        resultSet.next();
        Assertions.assertEquals("Mongolia", resultSet.getString("country"));
        Assertions.assertEquals("57", resultSet.getString("weight_category"));
        Assertions.assertEquals("Seniors", resultSet.getString("age_category"));
    }

    @Test
    public void testDelete() throws SQLException {
        // given
        Competitor competitor = new Competitor("GALBADRAKH", "Otgontsetseg", "Kazakhstan", "52", "18-23");
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COMPETITORS(family_name, given_name, country, weight_category, age_category) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, competitor.getFamilyName());
        preparedStatement.setString(2, competitor.getGivenName());
        preparedStatement.setString(3, competitor.getCountry());
        preparedStatement.setString(4, competitor.getWeightCategory());
        preparedStatement.setString(5, competitor.getAgeCategory());
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int id = generatedKeys.getInt("id");
        competitor.setId(id);

        // when
        competitorJDBCDAO.delete(competitor);

        // then
        ResultSet resultSet = connection.prepareStatement("SELECT count(1) as cnt FROM COMPETITORS WHERE id = " + competitor.getId()).executeQuery();
        resultSet.next();
        int cnt = resultSet.getInt("cnt");
        Assertions.assertEquals(0, cnt);
    }

    @Test
    public void testSearch() throws SQLException {
        // given
        Competitor competitor1 = new Competitor("GALBADRAKH", "Otgontsetseg", "Kazakhstan", "52", "18-23");
        Competitor competitor2 = new Competitor("GALBADRAKH", "Yujeong", "Kazakhstan", "60", "Seniors");
        Competitor competitor3 = new Competitor("HAMADA", "Shori", "Japan", "-78", "Seniors");
        List<Competitor> competitors = List.of(competitor1, competitor2, competitor3);
        for (Competitor competitor : competitors) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COMPETITORS(family_name, given_name, country, weight_category, age_category) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, competitor.getFamilyName());
            preparedStatement.setString(2, competitor.getGivenName());
            preparedStatement.setString(3, competitor.getCountry());
            preparedStatement.setString(4, competitor.getWeightCategory());
            preparedStatement.setString(5, competitor.getAgeCategory());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt("id");
            competitor.setId(id);
        }

        // when
        Competitor result = competitorJDBCDAO.search(competitor1);

        // then
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM COMPETITORS WHERE id = " + competitor1.getId()).executeQuery();
        resultSet.next();
        Assertions.assertEquals(result.getFamilyName(), resultSet.getString("family_name"));
        Assertions.assertEquals(result.getGivenName(), resultSet.getString("given_name"));
        Assertions.assertEquals(result.getCountry(), resultSet.getString("country"));
        Assertions.assertEquals(result.getWeightCategory(), resultSet.getString("weight_category"));
        Assertions.assertEquals(result.getAgeCategory(), resultSet.getString("age_category"));
    }
}
