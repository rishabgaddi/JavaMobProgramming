package fr.epita.services;

import fr.epita.datamodel.Competitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitionDataService extends CompetitorJDBCDAO {
    private static final Logger LOGGER = LogManager.getLogger(CompetitionDataService.class);

    public CompetitionDataService(DataSource ds) {
        super(ds);
    }

    public String getMostRepresentedWeightCategory() {
        String sql = "SELECT weight_category, COUNT(*) FROM COMPETITORS GROUP BY weight_category ORDER BY COUNT(*) DESC LIMIT 1";
        String weightCategory = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            weightCategory = resultSet.getString(1);
            int count = resultSet.getInt(2);
            LOGGER.info("The most represented weight category is {} with {} competitors", weightCategory, count);
        } catch (SQLException e) {
            LOGGER.error("Error while retrieving the most represented weight category", e);
        }
        return weightCategory;
    }

    public Map<String, Integer> distributionOfCompetitorsPerCountry() {
        String sql = "SELECT country, COUNT(*) FROM COMPETITORS GROUP BY country ORDER BY COUNT(*) DESC";
        Map<String, Integer> distribution = new HashMap<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String country = resultSet.getString(1);
                int count = resultSet.getInt(2);
                distribution.put(country, count);
            }
        } catch (SQLException e) {
            LOGGER.error("Error while retrieving the distribution of competitors per country", e);
        }
        return distribution;
    }

    public Integer totalNumberOfCompetitors() {
        String sql = "SELECT COUNT(*) FROM COMPETITORS";
        Integer total = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            total = resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("Error while retrieving the total number of competitors", e);
        }
        return total;
    }

    public List<Competitor> findByFamilyName(String familyName) {
        String sql = "SELECT * FROM COMPETITORS WHERE family_name = ?";
        List<Competitor> competitors = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, familyName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String family_name = resultSet.getString("family_name");
                String givenName = resultSet.getString("given_name");
                String country = resultSet.getString("country");
                String weightCategory = resultSet.getString("weight_category");
                String ageCategory = resultSet.getString("age_category");
                int id = resultSet.getInt("id");
                Competitor competitor = new Competitor(family_name, givenName, country, weightCategory, ageCategory);
                competitor.setId(id);
                competitors.add(competitor);
            }
        } catch (SQLException e) {
            LOGGER.error("Error while retrieving the competitors with family name {}", familyName, e);
        }
        return competitors;
    }
}
