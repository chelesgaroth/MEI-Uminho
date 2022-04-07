package com.rasbet.rasbet.UserBL;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Records {

    private int totalWins;
    private float totalProfit;
    private int totalInvested;
    private Map<String, Bet> openBets;
    private Map<String, Bet> closedBets;

    public Records() {
        this.totalWins = 0;
        this.totalProfit = 0;
        openBets = null;
        closedBets = null;
    }

    public Records(Map<String, Bet> openBets, Map<String, Bet> closedBets) {
        this.openBets = openBets;
        this.closedBets = closedBets;
        this.totalWins = 0;
        this.totalProfit = 0;
        this.totalInvested = 0;
    }

    public int getTotalWins() {
        return this.totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public float getTotalProfit() {
        return this.totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getTotalInvested() {
        return this.totalInvested;
    }

    public void setTotalInvested(int totalInvested) {
        this.totalInvested = totalInvested;
    }

    public Map<String, Bet> getOpenBets() {
        return this.openBets;
    }

    public void setOpenBets(Map<String, Bet> openBets) {
        this.openBets = openBets;
    }

    public Map<String, Bet> getClosedBets() {
        return this.closedBets;
    }

    public void setClosedBets(Map<String, Bet> closedBets) {
        this.closedBets = closedBets;
    }

    public void addOpenBets(String idBet, String idUser, Map<String, List<String>> eventCurrency,
            Map<String, List<Float>> oddsAmount) {
        for (Map.Entry<String, List<Float>> entry : oddsAmount.entrySet()) {
            Bet b = new Bet(idBet, entry.getKey(), eventCurrency.get(entry.getKey()).get(0),
                    oddsAmount.get(entry.getKey()).get(0), oddsAmount.get(entry.getKey()).get(1),
                    eventCurrency.get(entry.getKey()).get(1), idUser);
            this.openBets.put(entry.getKey(), b);

        }
    }

}