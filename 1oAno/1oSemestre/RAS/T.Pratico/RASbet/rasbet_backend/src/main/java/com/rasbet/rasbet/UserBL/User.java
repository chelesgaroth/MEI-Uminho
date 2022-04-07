package com.rasbet.rasbet.UserBL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.rasbet.rasbet.Exceptions.NoMoneyException;

public class User {

    private String idUser;
    private String name;
    private String email;
    private String password;
    private LocalDate dob;
    private Map<String, BigDecimal> wallets;
    private Map<String, Notification> notifications;
    private Records record;

    public User(String idUser, String name, String email, String password, LocalDate dob) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        wallets = new HashMap<>();
        notifications = new HashMap<>();
        record = new Records();

    }

    public User(String idUser, String name, String email, String password, LocalDate date,
                Map<String, BigDecimal> wallets,
                Map<String, Notification> notifications, Records record) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = date;
        this.wallets = wallets;
        this.notifications = notifications;
        this.record = record;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Map<String, BigDecimal> getWallets() {
        return this.wallets;
    }

    public void setWallets(Map<String, BigDecimal> wallets) {
        this.wallets = wallets;
    }

    public Map<String, Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Map<String, Notification> notifications) {
        this.notifications = notifications;
    }

    public Records getRecord() {
        return this.record;
    }

    public void setRecord(Records record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "{" +
                " idUser='" + getIdUser() + "'" +
                ", name='" + getName() + "'" +
                ", email='" + getEmail() + "'" +
                ", password='" + getPassword() + "'" +
                ", dob='" + getDob() + "'" +
                ", wallets='" + getWallets() + "'" +
                ", notifications='" + getNotifications() + "'" +
                ", record='" + getRecord() + "'" +
                "}";
    }

    /* Method to add entry to wallets map */
    public void addCurrency(String currencyId, BigDecimal balance) {
        this.wallets.put(currencyId, balance);
    }

    /* Method to widthdraw certain amount according to currency */
    public void widthdraw(String currency, BigDecimal amount) throws NoMoneyException {
        BigDecimal userBalance = wallets.get(currency);
        if (userBalance.compareTo(amount) == -1) { // 1 greater ; 0 equal; -1 less
            throw new NoMoneyException();
        } else {
            userBalance = userBalance.subtract(amount);
            wallets.put(currency, userBalance);
        }

    }

    /* Method to deposit certain amount according to currency */
    public void deposit(String currency, BigDecimal amount) {
        BigDecimal newAmount = amount;

        if (this.wallets.containsKey(currency))
            newAmount = this.wallets.get(currency).add(newAmount);

        this.wallets.put(currency, newAmount);
    }

    /* Method to check if user has a certain amount of money */
    public int hasEnoughMoney(String currency, BigDecimal amount) {
        BigDecimal actualAmount = this.wallets.get(currency);
        return (actualAmount.compareTo(amount)); // 0 -> equal; 1 -> greater; -1 -> lesser
    }

    /* Method to delete a notification with a certain id */
    public void deleteNotification(String idNotification) {
        this.notifications.remove(idNotification);
    }

    public void addNewNotification(Notification notification) {
        this.notifications.put(notification.getIdNotification(), notification);
    }

    /* Method to submmit a list of bets */
    public void addBets(String idBet, Map<String, List<String>> eventCurrency, Map<String, List<Float>> oddsAmount)
            throws NoMoneyException {
        for (Map.Entry<String, List<String>> entry : eventCurrency.entrySet()) {
            BigDecimal amountBetted = new BigDecimal(oddsAmount.get(entry.getKey()).get(1));
            widthdraw(entry.getValue().get(1), amountBetted);
        }

        // this.record.addOpenBets(idBet, idUser, eventCurrency, oddsAmount);
    }

    public void updateWinner(Bet b) {
        float newAmount = b.getAmount() * b.getOdd();
        deposit(b.getCurrencyUsed(), new BigDecimal(newAmount));
        String text = "";
        Notification n = new Notification(b.getIdBet(),"Bet winner!",text,LocalDateTime.now(),0);
        addNewNotification(n);
    }

}