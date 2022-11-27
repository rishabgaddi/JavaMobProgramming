package fr.epita.services;

import fr.epita.datamodel.Competitor;

public interface ICompetitorDAO {
    void create(Competitor competitor);

    void update(Competitor competitor);

    void delete(Competitor competitor);

    Competitor search(Competitor competitor);
}
