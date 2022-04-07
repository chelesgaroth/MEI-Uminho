package com.rasbet.rasbet.Facade;




public class WalletUpdateDTO {
    private double quantity;
    private String coin;
    private int action;

    public WalletUpdateDTO(double quantity, String coin, int action) {
        this.quantity = quantity;
        this.coin = coin;
        this.action = action;
    }

    public WalletUpdateDTO() {
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "WalletUpdateDTO{" +
                "quantity=" + quantity +
                ", coin='" + coin + '\'' +
                ", action=" + action +
                '}';
    }
}
