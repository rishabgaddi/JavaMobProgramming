package fr.epita.tests;

import fr.epita.datamodel.Competitor;
import fr.epita.services.CompetitorDAO;

import java.io.File;
import java.util.Set;

public class TestMVN3 {
    public static void main(String[] args) {
        CompetitorDAO dao = new CompetitorDAO();
        File file = new File("src/test/resources/contest-tokyo-grand-slam-2017.json");

        Set<Competitor> competitors = dao.getCompetitors(file);
        System.out.println("Competitors:");
        for (Competitor competitor : competitors) {
            System.out.println(competitor);
        }

        System.out.println("\nNumber of competitors: " + competitors.size() + "\n");

        System.out.println("Competitors sorted by Country and Family Name:");
        Set<Competitor> sortedCompetitors = dao.sort(competitors);
        for (Competitor competitor : sortedCompetitors) {
            System.out.println(competitor);
        }
    }
}
