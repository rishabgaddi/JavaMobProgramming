package fr.epita.services;

import fr.epita.datamodel.Competitor;

import java.io.File;
import java.util.*;

public class CompetitorDAO {
    public Set<Competitor> getCompetitors(File file) {
        StringConversionService stringConversionService = new JsonConversionService();
        Set<Map<String, Object>> competitors = new HashSet<>(stringConversionService.convert(file));
        Set<Competitor> competitorSet = new HashSet<>();
        for (Map<String, Object> competitor : competitors) {
            // Competitor White
            String familyName = (String) competitor.get("family_name_white");
            String givenName = (String) competitor.get("given_name_white");
            String country = (String) competitor.get("country_white");
            String weightCategory = (String) competitor.get("weight");
            String ageCategory = (String) competitor.get("age");
            Competitor competitor1 = new Competitor(familyName, givenName, country, weightCategory, ageCategory);
            competitorSet.add(competitor1);

            // Competitor Blue
            familyName = (String) competitor.get("family_name_blue");
            givenName = (String) competitor.get("given_name_blue");
            country = (String) competitor.get("country_blue");
            weightCategory = (String) competitor.get("weight");
            ageCategory = (String) competitor.get("age");
            Competitor competitor2 = new Competitor(familyName, givenName, country, weightCategory, ageCategory);
            competitorSet.add(competitor2);
        }
        return competitorSet;
    }

    public Set<Competitor> sort(Set<Competitor> competitors) {
        Set<Competitor> sortedCompetitors = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                int result = o1.getCountry().compareTo(o2.getCountry());
                if (result == 0) {
                    return o1.getFamilyName().compareTo(o2.getFamilyName());
                } else {
                    return result;
                }
            }
        });
        sortedCompetitors.addAll(competitors);
        return sortedCompetitors;
    }
}
