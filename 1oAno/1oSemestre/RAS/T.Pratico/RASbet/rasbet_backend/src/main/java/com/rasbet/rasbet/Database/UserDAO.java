package com.rasbet.rasbet.Database;

import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.rasbet.rasbet.UserBL.Bet;
import com.rasbet.rasbet.UserBL.Notification;
import com.rasbet.rasbet.UserBL.Records;
import com.rasbet.rasbet.UserBL.User;

public class UserDAO implements Map<String, User> {

    private static UserDAO singleton = null;
    private Connection connection;

    // Construtor. É aqui que a tabela das Bets na base de dados é criada.
    /* private */ public UserDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='users')" +
                    "CREATE TABLE users (" +
                    "IdUser varchar(50) NOT NULL PRIMARY KEY," +
                    "Name varchar(50) NOT NULL," +
                    "Email varchar(50) NOT NULL," +
                    "Passwd varchar(50) NOT NULL," +
                    "Dob date NOT NULL);";
            stm.executeUpdate(sql);

            String sql1 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='notifications')" +
                    "CREATE TABLE notifications (" +
                    "IdNotif varchar(50) NOT NULL PRIMARY KEY," +
                    "IdUser varchar(50) NOT NULL, foreign key(IdUser) references users(IdUser)," +
                    "Title varchar(50) DEFAULT NULL," +
                    "Message ntext DEFAULT NULL," +
                    "DateReceived datetime DEFAULT NULL," +
                    "Deleted int  NOT NULL );";
            stm.executeUpdate(sql1);

            String sql2 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='wallets')" +
                    "CREATE TABLE wallets (" +
                    "IdCurrency varchar(50) NOT NULL, foreign key(IdCurrency) references currencies(IdCurrency)," +
                    "IdUser varchar(50) NOT NULL, foreign key(IdUser) references users(IdUser)," +
                    "Balance money NOT NULL," +
                    "PRIMARY KEY (IdCurrency,IdUser));";
            stm.executeUpdate(sql2);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static UserDAO getInstance() {
        if (UserDAO.singleton == null) {
            UserDAO.singleton = new UserDAO();
        }
        return UserDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM users");
            if (rs.next())
                size = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdUser FROM bookmakers WHERE IdUser ='" + key.toString() + "'");
            r = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        User a = (User) value;
        return this.containsKey(a.getIdUser());
    }

    @Override
    public User get(Object key) {
        User u = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE IdUser ='" + key + "'");
            if (rs.next()) {

                // Reconstrutir as notificações
                Map<String, Notification> notifications = new HashMap<>();
                String sql = "SELECT * FROM notifications WHERE IdUser ='" + rs.getString("IdUser") + "'";
                Class.forName(DAOconfig.className);
                connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
                Statement stm2 = connection.createStatement();
                ResultSet rs2 = stm2.executeQuery(sql);
                while (rs2.next()) {
                    Notification n = new Notification(rs2.getString("IdNotif"), rs2.getString("Title"),
                            rs2.getString("Message"), (rs2.getTimestamp("DateReceived")).toLocalDateTime(),
                            rs2.getInt("Deleted"));
                    notifications.put(rs2.getString("IdNotif"), n); // CUIDADO NULL
                }

                // Reconstruir as wallets do utilizador
                Map<String, BigDecimal> wallets = new HashMap<>();
                String sql2 = "SELECT * FROM wallets WHERE IdUser ='" + rs.getString("IdUser") + "'";
                Class.forName(DAOconfig.className);
                connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
                Statement stm3 = connection.createStatement();
                ResultSet rs3 = stm3.executeQuery(sql2);
                while (rs3.next()) {
                    BigDecimal balance = rs3.getBigDecimal("Balance");
                    String currency = rs3.getString("IdCurrency");
                    wallets.put(currency, balance); // CUIDADO NULL
                }

                // Reconstruir o histórico

                Map<String, Bet> openBets = getOpenBets(key);
                Map<String, Bet> closedBets = getClosedBets(key);
                Records r = new Records(openBets, closedBets);

                // Reconstruir o User com os dados obtidos
                // ZoneId defaultZoneId = ZoneId.systemDefault();
                // Instant instant = (rs.getDate("Dob")).toInstant();


                // Reconstruir o User com os dados obtidos
                // ZoneId defaultZoneId = ZoneId.systemDefault();
                // Instant instant = (rs.getDate("Dob")).toInstant();
                u = new User(rs.getString("IdUser"), rs.getString("Name"), rs.getString("Email"),
                        rs.getString("Passwd"), rs.getDate("Dob").toLocalDate(), wallets, notifications, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return u;
    }

    public Map<String, Bet> getOpenBets(Object key) {
        Map<String, Bet> openBets = new HashMap<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm2 = connection.createStatement();
            String sql3 = "SELECT bets.IdBet " +
                    "FROM [dbo].[choices] " +
                    "INNER JOIN bets ON bets.IdChoice=choices.IdChoice " +
                    "WHERE Status= '1' AND IdUser= '" + key + "'";
            ResultSet rs2 = stm2.executeQuery(sql3);
            while (rs2.next()) {
                Statement stm3 = connection.createStatement();
                ResultSet rs = stm3.executeQuery("SELECT * FROM bets WHERE IdBet='" + rs2.getString("IdBet") + "'");

                if (rs.next()){
                    openBets.put(rs2.getString("IdBet"),
                            new Bet(rs2.getString("IdBet"), rs.getString("IdChoice"), rs2.getString("IdEvent"),
                                    rs.getFloat("Amount"), rs.getFloat("Odd"), rs.getString("IdCurrency"),
                                    rs.getString("IdUser")));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return openBets;
    }

    public Map<String, Bet> getClosedBets(Object key) {
        Map<String, Bet> openBets = new HashMap<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm2 = connection.createStatement();
            String sql3 = "SELECT bets.IdBet " +
                    "FROM [dbo].[choices] " +
                    "INNER JOIN bets ON bets.IdChoice=choices.IdChoice " +
                    "WHERE Status= '0' AND IdUser= '" + key + "'";
            ResultSet rs2 = stm2.executeQuery(sql3);
            while (rs2.next()) {
                Statement stm3 = connection.createStatement();
                ResultSet rs = stm3.executeQuery("SELECT * FROM bets WHERE IdBet='" + rs2.getString("IdBet") + "'");

                if (rs.next()){
                    openBets.put(rs2.getString("IdBet"),
                            new Bet(rs2.getString("IdBet"), rs.getString("IdChoice"), rs.getString("IdEvent"),
                                    rs.getFloat("Amount"), rs.getFloat("Odd"), rs.getString("IdCurrency"),
                                    rs.getString("IdUser")));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return openBets;
    }

    @Override
    public User put(String key, User value) {
        User res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("MERGE INTO users AS t USING " +
                    "(SELECT IdUser='" + value.getIdUser() + "', Name='" + value.getName() + "', Email='"
                    + value.getEmail() +
                    "', Passwd='" + value.getPassword() + "', Dob='" + value.getDob() + "') AS s " +
                    "ON t.IdUser = s.IdUser " +
                    "WHEN MATCHED THEN " +
                    "UPDATE SET IdUser=s.IdUser, Email=s.Email, Passwd=s.Passwd " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (IdUser, Name, Email, Passwd, Dob) " +
                    "VALUES (s.IdUser, s.Name, s.Email, s.Passwd, s.Dob);");

            for (Notification n : value.getNotifications().values()) {
                stm.executeUpdate("MERGE INTO notifications AS t USING " +
                        "(SELECT IdNotif='" + n.getIdNotification() + "', Title='" + n.getTitle() + "', Message='"
                        + n.getMessage() +
                        "', DateReceived='" + n.getDateReceived() + "', Deleted='" + n.getDeleted() + "') AS s " +
                        "ON t.IdNotif = s.IdNotif " +
                        "WHEN MATCHED THEN " +
                        "UPDATE SET IdNotif=s.IdNotif, Title=s.Title, Passwd=s.Passwd " +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (IdNotif, Title, Message, DateReceived, Deleted) " +
                        "VALUES (s.IdNotif, s.Title, s.Message, s.DateReceived, s.Deleted);");
            }

            Map<String, BigDecimal> wallets = value.getWallets();
            for (Map.Entry<String, BigDecimal> entry : wallets.entrySet()) {
                stm.executeUpdate("MERGE INTO wallets AS t USING " +
                        "(SELECT IdCurrency= '" + entry.getKey() + "', IdUser='" + value.getIdUser() +
                        "', Balance='" + entry.getValue() + "') AS s " +
                        "ON (t.IdCurrency= s.IdCurrency AND t.IdUser= s.IdUser) " +
                        "WHEN MATCHED THEN " +
                        "UPDATE SET IdCurrency= s.IdCurrency, IdUser= s.IdUser, Balance=s.Balance " +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (IdCurrency, IdUser, Balance) " +
                        "VALUES (s.IdCurrency, s.IdUser, s.Balance);");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return res;
    }

    @Override
    public User remove(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends User> m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<User> values() {
        Collection<User> res = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdUser FROM users");
            while (rs.next()) {
                String idt = rs.getString("IdUser"); // Obtemos um id do ResultSet
                User t = this.get(idt); // Utilizamos o get para construir
                res.add(t); // Adiciona ao resultado.
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Set<Entry<String, User>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }

}
