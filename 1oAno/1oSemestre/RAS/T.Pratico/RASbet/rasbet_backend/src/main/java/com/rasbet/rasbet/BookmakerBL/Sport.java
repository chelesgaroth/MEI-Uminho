package com.rasbet.rasbet.BookmakerBL;

import java.util.Set;

public class Sport {
    private String idSport;
    private String name;
    private int type;
    private int typology;
    private Set<Competition> competitions;


    public Sport(String idSport, String name, int type, int typology, Set<Competition> competitions) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.typology = typology;
        this.competitions = competitions;
    }



    public String getIdSport() {
        return this.idSport;
    }

    public void setIdSport(String idSport) {
        this.idSport = idSport;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypology() {
        return this.typology;
    }

    public void setTypology(int typology) {
        this.typology = typology;
    }

    public Set<Competition> getCompetitions() {
        return this.competitions;
    }

    public void setCompetitions(Set<Competition> competitions) {
        this.competitions = competitions;
    }

}