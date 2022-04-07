package com.rasbet.rasbet.UserBL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.sql.Date;

import com.rasbet.rasbet.IdCounterInterface;

import com.rasbet.rasbet.Database.*;
import com.rasbet.rasbet.Exceptions.NoMoneyException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Users Subsystem Facade
 */
public class UserFacade implements IdCounterInterface {

    Map<String, Currency> currencies;
    Map<String, User> users;
    Map<String, Bet> bets;
    int betCounter;

    public UserFacade() {
        currencies = CurrencyDAO.getInstance();
        users = UserDAO.getInstance();
        bets = BetDAO.getInstance();
        updateCounter();
    }

    // Done
    public String addNewUser(String idUser, String name, String email, String password, LocalDate dob) {
        User u = new User(idUser, name, email, password, dob);
        // u.addCurrency("currency", new BigDecimal(0.0));
        users.put(idUser, u);
        return idUser;
    }

    // Done
    public boolean verifyUser(String email, String password) {

        for (User u : this.users.values()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public String getUserID(String email, String password) {
        for (User u : this.users.values()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
                return u.getIdUser();
        }
        return null;
    }

    // Done
    public Map<String, String> getUser(String idUser) {
        User u = this.users.get(idUser);
        HashMap<String, String> retValues = new HashMap<>();
        retValues.put("idUser", idUser);
        retValues.put("name", u.getName());
        retValues.put("email", u.getEmail());
        retValues.put("password", u.getPassword());
        retValues.put("dob", u.getDob().toString());

        return retValues;
    }

    // Done
    public List<String> updateUser(String idUser, String email, String name, String password) {
        User u = users.get(idUser);

        if (email != null)
            u.setEmail(email);
        if (name != null)
            u.setName(name);
        if (password != null)
            u.setPassword(password);

        users.put(idUser, u);
        List<String> res = new ArrayList<>();
        res.add(idUser);
        res.add(email);
        res.add(name);
        res.add(password);
        return res;
    }

    public void addUserBets(String idUser, Map<String, List<String>> choiceEvent, Map<String, List<Float>> oddsAmount)
            throws NoMoneyException {
        User u = users.get(idUser);
        String idBet = getNewBetId();
        u.addBets(idBet, choiceEvent, oddsAmount);
        users.put(idUser, u);
        for (Map.Entry<String, List<Float>> entry : oddsAmount.entrySet()) {
            Bet b = new Bet(idBet, entry.getKey(), choiceEvent.get(entry.getKey()).get(0),
                    oddsAmount.get(entry.getKey()).get(0), oddsAmount.get(entry.getKey()).get(1),
                    choiceEvent.get(entry.getKey()).get(1), idUser);
            this.bets.put(entry.getKey(), b);
        }
    }

    public Map<String, Object> checkRecords(String idUser) {

        User u = users.get(idUser);
        Records userRecord = u.getRecord();

        //System.out.println(userRecord.getOpenBets().toString());
        //System.out.println(userRecord.getClosedBets().toString());


        HashMap<String, Object> returnObj = new HashMap<>();
        returnObj.put("total_wins", userRecord.getTotalWins());
        returnObj.put("total_profit", userRecord.getTotalProfit());
        returnObj.put("total_invested", userRecord.getTotalInvested());

        returnObj.put("open_bets", userRecord.getOpenBets());
        returnObj.put("closed_bets", userRecord.getClosedBets());

        return returnObj;
    }

    public Map<String, BigDecimal> checkWallet(String idUser) {
        User u = users.get(idUser);

        return u.getWallets();
    }

    public boolean widthdraw(String idUser, BigDecimal amount, String currency) {
        User u = users.get(idUser);
        if (u.hasEnoughMoney(currency, amount) >= 0) {
            try {
                u.widthdraw(currency, amount);
            } catch (NoMoneyException e) {
                e.printStackTrace();
                return false;
            }
            users.put(idUser, u);
            return true;
        } else
            return false;
    }

    public void deposit(String idUser, BigDecimal amount, String currency) {
        User u = users.get(idUser);
        u.deposit(currency, amount);
        users.put(idUser, u);
    }

    public Map<String, Notification> checkNotifications(String idUser) {
        User u = users.get(idUser);
        return u.getNotifications();
    }

    public void clearNotifications(String idUser, String idNotification) {
        User u = users.get(idUser);
        u.deleteNotification(idNotification);
    }

    public void createNotification(String idUser, String content, String titulo, LocalDateTime dataReceived) {
        // Notification n = new Notification(titulo, content, dataReceived);
        // User u = this.users.get(idUser);

        // u.addNewNotification(n);

        // this.users.put(idUser, u);
    }

    public void updateWinners(String winnerChoice) {
        for(Bet b : bets.values()) {
            if (b.getIdChoice().equals(winnerChoice)) {
                User u = users.get(b.getIdUser());
                u.updateWinner(b);
            }
        }
    }

    private String getNewBetId() {
        String id = new String("B" + betCounter);
        betCounter++;
        return id;
    }

    private void updateCounter() {
        betCounter = getCounter(bets.keySet());
    }


}
