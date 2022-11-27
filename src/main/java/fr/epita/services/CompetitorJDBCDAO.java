package fr.epita.services;

import fr.epita.datamodel.Competitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;

public class CompetitorJDBCDAO implements ICompetitorDAO {
    private static final Logger LOGGER = LogManager.getLogger(CompetitorJDBCDAO.class);
    private static final String SQL_INSERT = "INSERT INTO COMPETITORS (family_name, given_name, country, weight_category, age_category) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE COMPETITORS SET family_name = ?, given_name = ?, country = ?, weight_category = ?, age_category = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM COMPETITORS WHERE id = ?";

    DataSource ds;

    public CompetitorJDBCDAO(DataSource ds) {
        this.ds = ds;
    }

    protected CompetitorJDBCDAO() {
    }

    public void create(Competitor competitor) {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, competitor.getFamilyName());
            statement.setString(2, competitor.getGivenName());
            statement.setString(3, competitor.getCountry());
            statement.setString(4, competitor.getWeightCategory());
            statement.setString(5, competitor.getAgeCategory());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt("id");
            competitor.setId(id);
        } catch (SQLException sqle) {
            LOGGER.error("Error while inserting ", sqle);
        }
    }

    public void update(Competitor competitor) {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, competitor.getFamilyName());
            statement.setString(2, competitor.getGivenName());
            statement.setString(3, competitor.getCountry());
            statement.setString(4, competitor.getWeightCategory());
            statement.setString(5, competitor.getAgeCategory());
            statement.setInt(6, competitor.getId());
            statement.executeUpdate();
        } catch (SQLException sqle) {
            LOGGER.error("Error while updating ", sqle);
        }
    }

    public void delete(Competitor competitor) {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setInt(1, competitor.getId());
            statement.executeUpdate();
        } catch (SQLException sqle) {
            LOGGER.error("Error while deleting ", sqle);
        }
    }

    public Competitor search(Competitor competitor) {
        Competitor result = null;
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPETITORS WHERE id = ?");
            statement.setInt(1, competitor.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String familyName = resultSet.getString("family_name");
            String givenName = resultSet.getString("given_name");
            String country = resultSet.getString("country");
            String weightCategory = resultSet.getString("weight_category");
            String ageCategory = resultSet.getString("age_category");
            int id = resultSet.getInt("id");
            result = new Competitor(familyName, givenName, country, weightCategory, ageCategory);
            result.setId(id);
        } catch (SQLException sqle) {
            LOGGER.error("Error while searching ", sqle);
        }
        return result;
    }
}
