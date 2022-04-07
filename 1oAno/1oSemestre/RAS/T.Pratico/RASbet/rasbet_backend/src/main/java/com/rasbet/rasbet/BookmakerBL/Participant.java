package com.rasbet.rasbet.BookmakerBL;

public class Participant {
    private String idParticipant;
    private String name;
    private String idSport;

    public Participant(String idParticipant, String name, String idSport) {
        this.idParticipant = idParticipant;
        this.name = name;
        this.idSport = idSport;
    }

    public String getIdParticipant() {
        return this.idParticipant;
    }

    public void setIdParticipant(String idParticipant) {
        this.idParticipant = idParticipant;
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