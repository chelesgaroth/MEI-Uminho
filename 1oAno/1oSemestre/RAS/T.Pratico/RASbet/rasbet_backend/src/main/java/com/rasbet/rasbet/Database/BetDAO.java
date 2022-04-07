package com.rasbet.rasbet.Database;

import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rasbet.rasbet.UserBL.Bet;

public class BetDAO implements Map<String, Bet> {

    private static BetDAO singleton = null;
    private Connection connection;

    // Construtor. É aqui que a tabela das Bets na base de dados é criada.
    /* private */ public BetDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql1 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='choices')" +
                    "CREATE TABLE choices (" +
                    "IdChoice varchar(50) NOT NULL PRIMARY KEY," +
                    "Odd float(5) NOT NULL," +
                    "IdEvent varchar(50) NOT NULL, foreign key(IdEvent) references events(IdEvent)," +
                    "Status int NOT NULL," +
                    "Result varchar(50) NOT NULL);";

            stm.executeUpdate(sql1);
            String sql2 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='bets')" +
                    "CREATE TABLE bets (" +
                    "IdBet varchar(50) NOT NULL PRIMARY KEY," +
                    "IdUser varchar(50) NOT NULL, foreign key(IdUser) references users(IdUser)," +
                    "IdChoice varchar(50) NOT NULL, foreign key(IdChoice) references choices(IdChoice)," +
                    "Amount float(10) NOT NULL," +
                    "Odd float(10) NOT NULL," +
                    "IdCurrency varchar(50) NOT NULL, foreign key(IdCurrency) references currencies(IdCurrency));";

            stm.executeUpdate(sql2);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static BetDAO getInstance() {
        if (BetDAO.singleton == null) {
            BetDAO.singleton = new BetDAO();
        }
        return BetDAO.singleton;
    }

    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM bets");
            if (rs.next())
                size = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsKey(Object key) {
        boolean r;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBet FROM bookmakers WHERE IdBet ='" + key.toString() + "'");
            r = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Bet a = (Bet) value;
        return this.containsKey(a.getIdBet());
    }

    public Bet get(Object key) {
        Bet a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bets WHERE IdBet='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                a = new Bet(rs.getString("IdBet"), rs.getString("IdChoice"), getEventId(stm, rs.getString("IdChoice")),
                        rs.getFloat("Amount"), rs.getFloat("Odd"), rs.getString("IdCurrency"), rs.getString("IdUser"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    public Bet getClosed(Object key) {
        Bet a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bets WHERE IdBet='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                a = new Bet(rs.getString("IdBet"), rs.getString("IdChoice"), getEventId(stm, rs.getString("IdChoice")),
                        rs.getFloat("Amount"), rs.getFloat("Odd"), rs.getString("IdCurrency"), rs.getString("IdUser"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    public String getEventId(Statement stm, String idChoice) {
        String event = null;
        try {
            ResultSet rs = stm.executeQuery("SELECT IdEvent FROM choices WHERE IdChoice='" + idChoice + "'");
            if (rs.next()) {
                event = rs.getString("IdEvent");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return event;
    }

    public Bet put(String key, Bet value) {
        Bet res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "MERGE INTO bets AS t USING " +
                            "(SELECT IdBet='" + value.getIdBet() + "', IdChoice='" + value.getIdChoice() +
                            "', Amount='" + value.getAmount() + "', Odd='" + value.getOdd() + "', IdCurrency='"
                            + value.getCurrencyUsed() + "', IdUser='" + value.getIdUser() + "') AS s " +
                            "ON t.IdBet = s.IdBet " +
                            "WHEN MATCHED THEN " +
                            "UPDATE SET IdBet=s.IdBet, IdChoice=s.IdChoice, Amount=s.Amount, Odd=s.Odd,  IdCurrency=s.IdCurrency, IdUser=s.IdUser "
                            + "WHEN NOT MATCHED THEN " +
                            "INSERT (IdBet, IdChoice, Amount, Odd, IdCurrency, IdUser) " +
                            "VALUES (s.IdBet, s.IdChoice, s.Amount, s.Odd, s.IdCurrency, s.IdUser);");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public Bet remove(Object key) {
        Bet b = this.get(key);
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM bets WHERE IdBet='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return b;

    }

    public void putAll(Map<? extends String, ? extends Bet> bets) {
        for (Bet b : bets.values()) {
            this.put(b.getIdBet(), b);
        }
    }

    public void clear() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("UPDATE events SET IdBmaker=NULL");
            stm.executeUpdate("TRUNCATE bookmakers");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Set<String> keySet() {
        Set<String> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBet FROM bets");
            while (rs.next()) {
                col.add(rs.getString("IdBet"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    public Collection<Bet> values() {
        Collection<Bet> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBet FROM bets");
            while (rs.next()) {
                col.add(this.get(rs.getString("IdBet")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    public Set<Entry<String, Bet>> entrySet() {
        Map.Entry<String, Bet> entry;
        HashSet<Entry<String, Bet>> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBet FROM bets");
            while (rs.next()) {
                entry = new AbstractMap.SimpleEntry<>(rs.getString("IdBet"), this.get(rs.getString("IdBet")));
                col.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

}
