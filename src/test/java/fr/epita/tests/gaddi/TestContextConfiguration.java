package fr.epita.tests.gaddi;

import fr.epita.datamodel.Competitor;
import fr.epita.services.CompetitionDataService;
import fr.epita.services.CompetitorJDBCDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestContextConfiguration {
    @Bean(name = "my-first-bean")
    public String myFirstBean() {
        return "Hello from Spring, Gaddi";
    }

    @Bean(name = "create-competitor")
    public Competitor createCompetitor() {
        return new Competitor("Gaddi", "Rishab", "India", "-73", "-100");
    }

    @Bean(name = "main-ds")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setPassword("test");
        dataSource.setUsername("sa");
        return dataSource;
    }

    @Bean(name = "competitor-jdbc-dao")
    public CompetitorJDBCDAO getCompetitorJDBCDao(@Qualifier("main-ds") DataSource ds) {
        return new CompetitorJDBCDAO(ds);
    }

    @Bean(name = "competition-data-service")
    public CompetitionDataService competitionDataService(@Qualifier("main-ds") DataSource ds) {
        return new CompetitionDataService(ds);
    }
}
