package com.rasbet.rasbet.BookmakerBL;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class Event {
    private String idEvent;
    private LocalDateTime initialDate;
    private String finalResult;
    private int eventStatus;
    private String description;
    private String idBookmaker;
    private String idCompetition;
    private Set<Participant> participants;
    private Set<Choice> choices;
    private String sport;

    public Event() {
    }

    public Event(String idEvent, LocalDateTime initialDate, String finalResult, int eventStatus, String description,
                 String idBookmaker, String idCompetition, Set<Participant> participants, Set<Choice> choices) {
        this.idEvent = idEvent;
        this.initialDate = initialDate;
        this.finalResult = finalResult;
        this.eventStatus = eventStatus;
        this.description = description;
        this.idBookmaker = idBookmaker;
        this.idCompetition = idCompetition;
        this.participants = participants;
        this.choices = choices;
    }

    public String getIdEvent() {
        return this.idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public LocalDateTime getInitialDate() {
        return this.initialDate;
    }

    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalResult() {
        return this.finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public int getEventStatus() {
        return this.eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdBookmaker() {
        return this.idBookmaker;
    }

    public void setIdBookmaker(String idBookmaker) {
        this.idBookmaker = idBookmaker;
    }

    public String getIdCompetition() {
        return this.idCompetition;
    }

    public void setIdCompetition(String idCompetition) {
        this.idCompetition = idCompetition;
    }

    public Set<Participant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public Set<Choice> getChoices() {
        return this.choices;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    public void closeEvent(String result) {
        eventStatus = 0;
        finalResult = result;
        for (Choice c : choices) {
            c.setStatus(0);
        }
    }

    public void updateChoicesOdds(Map<String, Float> choicesNew) {
        for (Choice c : this.choices) {
            if (choicesNew.containsKey(c.getIdChoice()))
                c.setOdd(choicesNew.get(c.getIdChoice()));
        }
    }

    public void changeChoicesStatus(Map<String, Integer> choices) {
        for (Choice c : this.choices) {
            Integer stat = choices.get(c.getIdChoice());
            c.setStatus(stat);
        }
    }

    public String getSport() {
        return this.sport;
    }
    public void setSport(String s){
        this.sport = s;
    }
    @Override
    public String toString() {
        return "{" +
                " idEvent='" + getIdEvent() + "'" +
                ", initialDate='" + getInitialDate() + "'" +
                ", finalResult='" + getFinalResult() + "'" +
                ", eventStatus='" + getEventStatus() + "'" +
                ", description='" + getDescription() + "'" +
                ", idBookmaker='" + getIdBookmaker() + "'" +
                ", idCompetition='" + getIdCompetition() + "'" +
                ", participants='" + getParticipants() + "'" +
                ", choices='" + getChoices() + "'" +
                "}";
    }

}