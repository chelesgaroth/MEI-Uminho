package com.rasbet.rasbet.BookmakerBL;

public class Choice {
    private String idChoice;
    private float odd;
    private int status;
    private String result;


    public Choice(String idChoice, float odd, int status, String result) {
        this.idChoice = idChoice;
        this.odd = odd;
        this.status = status;
        this.result = result;
    }


    public String getIdChoice() {
        return this.idChoice;
    }

    public void setIdChoice(String idChoice) {
        this.idChoice = idChoice;
    }

    public float getOdd() {
        return this.odd;
    }

    public void setOdd(float odd) {
        this.odd = odd;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}