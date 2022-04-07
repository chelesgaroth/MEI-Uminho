package com.rasbet.rasbet.Facade;

public class BetDTO {

    private String eventID;
    private String date;
    private String time;
    private String type;
    private String bettedTeam;
    private String bettedOdd;
    private String coin;
    private String amount;
    private String choiceID;

    public BetDTO(String eventID, String date, String time, String type, String bettedTeam, String bettedOdd, String coin, String amount, String choiceID) {
        this.eventID = eventID;
        this.date = date;
        this.time = time;
        this.type = type;
        this.bettedTeam = bettedTeam;
        this.bettedOdd = bettedOdd;
        this.coin = coin;
        this.amount = amount;
        this.choiceID = choiceID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBettedTeam() {
        return bettedTeam;
    }

    public void setBettedTeam(String bettedTeam) {
        this.bettedTeam = bettedTeam;
    }

    public String getBettedOdd() {
        return bettedOdd;
    }

    public void setBettedOdd(String bettedOdd) {
        this.bettedOdd = bettedOdd;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChoiceID() {
        return choiceID;
    }

    public void setChoiceID(String choiceID) {
        this.choiceID = choiceID;
    }

    @Override
    public String toString() {
        return "BetDTO{" +
                "eventID='" + eventID + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", bettedTeam='" + bettedTeam + '\'' +
                ", bettedOdd='" + bettedOdd + '\'' +
                ", coin='" + coin + '\'' +
                ", amount='" + amount + '\'' +
                ", choiceID='" + choiceID + '\'' +
                '}';
    }
}
