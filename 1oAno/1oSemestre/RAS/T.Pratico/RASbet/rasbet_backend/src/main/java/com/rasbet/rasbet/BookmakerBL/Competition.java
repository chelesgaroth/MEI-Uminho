package com.rasbet.rasbet.BookmakerBL;

public class Competition {
    private String idCompetition;
    private String name;
    private String idSport;


    public Competition(String idCompetition, String name, String idSport) {
        this.idCompetition = idCompetition;
        this.name = name;
        this.idSport = idSport;
    }


    public String getIdCompetition() {
        return this.idCompetition;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "idCompetition='" + idCompetition + '\'' +
                ", name='" + name + '\'' +
                ", idSport='" + idSport + '\'' +
                '}';
    }

    public void setIdCompetition(String idCompetition) {
        this.idCompetition = idCompetition;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdSport() {
        return this.idSport;
    }

    public void setIdSport(String idSport) {
        this.idSport = idSport;
    }

}