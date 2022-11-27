package fr.epita.datamodel;

import java.util.Objects;

public class Competitor {
    private int id;
    private String familyName;
    private String givenName;
    private String country;
    private String weightCategory;
    private String ageCategory;

    public Competitor(String familyName, String givenName, String country, String weightCategory, String ageCategory) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.country = country;
        this.weightCategory = weightCategory;
        this.ageCategory = ageCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competitor that = (Competitor) o;
        return familyName.equals(that.familyName) && givenName.equals(that.givenName) && country.equals(that.country) && weightCategory.equals(that.weightCategory) && ageCategory.equals(that.ageCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(familyName, givenName, country, weightCategory, ageCategory);
    }

    @Override
    public String toString() {
        return "Competitor{" +
                "familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", country='" + country + '\'' +
                ", weightCategory='" + weightCategory + '\'' +
                ", ageCategory='" + ageCategory + '\'' +
                '}';
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeightCategory() {
        return weightCategory;
    }

    public void setWeightCategory(String weightCategory) {
        this.weightCategory = weightCategory;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }
}
