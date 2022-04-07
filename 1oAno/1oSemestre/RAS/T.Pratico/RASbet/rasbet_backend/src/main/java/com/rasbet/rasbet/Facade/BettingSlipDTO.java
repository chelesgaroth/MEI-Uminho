package com.rasbet.rasbet.Facade;

import java.util.List;

public class BettingSlipDTO {
    private List<BetDTO> bets;

    public BettingSlipDTO() {
    }

    public BettingSlipDTO(List<BetDTO> bets) {
        this.bets = bets;
        System.out.println(this.bets);
    }

    public List<BetDTO> getBets() {
        return bets;
    }

    public void setBets(List<BetDTO> bets) {
        this.bets = bets;
    }

    @Override
    public String toString() {
        return "BettingSlipDTO{" +
                "bets=" + bets +
                '}';
    }
}
