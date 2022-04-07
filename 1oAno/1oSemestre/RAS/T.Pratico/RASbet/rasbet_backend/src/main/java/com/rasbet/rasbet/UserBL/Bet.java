package com.rasbet.rasbet.UserBL;

public class Bet {

    private String idBet;
    private String idChoice;
    private String idEvent;
    private float amount;
    private float odd;
    private String currencyUsed;
    private String idUser;

    public Bet(String idBet, String idChoice, String idEvent, float amount, float odd, String currencyUsed,
            String idUser) {
        this.idBet = idBet;
        this.idChoice = idChoice;
        this.idEvent = idEvent;
        this.amount = amount;
        this.odd = odd;
        this.currencyUsed = currencyUsed;
        this.idUser = idUser;
    }

    public String getIdBet() {
        return this.idBet;
    }

    public void setIdBet(String idBet) {
        this.idBet = idBet;
    }

    public String getIdChoice() {
        return this.idChoice;
    }

    public void setIdChoice(String idChoice) {
        this.idChoice = idChoice;
    }

    public String getIdEvent() {
        return this.idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getOdd() {
        return this.odd;
    }

    public void setOdd(float odd) {
        this.odd = odd;
    }

    public String getCurrencyUsed() {
        return this.currencyUsed;
    }

    public void setCurrencyUsed(String currencyUsed) {
        this.currencyUsed = currencyUsed;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "{" +
                " idBet='" + getIdBet() + "'" +
                ", idChoice='" + getIdChoice() + "'" +
                ", idEvent='" + getIdEvent() + "'" +
                ", amount='" + getAmount() + "'" +
                ", odd='" + getOdd() + "'" +
                ", currencyUsed='" + getCurrencyUsed() + "'" +
                ", idUser='" + getIdUser() + "'" +
                "}";
    }

}
