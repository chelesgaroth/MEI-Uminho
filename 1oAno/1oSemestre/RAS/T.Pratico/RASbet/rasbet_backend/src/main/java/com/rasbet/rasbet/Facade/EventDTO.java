package com.rasbet.rasbet.Facade;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventDTO {
    private String idBookamker;
    private String idSport;
    private String idCompetition;
    private List<String> participants;
    private Map<String, Float> choices;
    private LocalDateTime date;

    public EventDTO () {

    }

    public EventDTO(String idBookamker, String idSport, String idCompetition, List<String> participants, Map<String, Float> choices, LocalDateTime date) {
        this.idBookamker = idBookamker;
        this.idSport = idSport;
        this.idCompetition = idCompetition;
        this.participants = participants;
        this.choices = choices;
        this.date = date;
    }

    public String getIdBookamker() {
        return idBookamker;
    }

    public void setIdBookamker(String idBookamker) {
        this.idBookamker = idBookamker;
    }

    public String getIdSport() {
        return idSport;
    }

    public void setIdSport(String idSport) {
        this.idSport = idSport;
    }

    public String getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(String idCompetition) {
        this.idCompetition = idCompetition;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Map<String, Float> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, Float> choices) {
        this.choices = choices;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "idBookamker='" + idBookamker + '\'' +
                ", idSport='" + idSport + '\'' +
                ", idCompetition='" + idCompetition + '\'' +
                ", participants=" + participants +
                ", choices=" + choices +
                ", date=" + date +
                '}';
    }
}
