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

import com.rasbet.rasbet.BookmakerBL.Bookmaker;

public class BookmakerDAO implements Map<String, Bookmaker> {

    private static BookmakerDAO singleton = null;
    private Connection connection;

    public BookmakerDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='bookmakers')" +
                    "CREATE TABLE bookmakers (" +
                    "IdBmaker varchar(50) NOT NULL PRIMARY KEY," +
                    "Email varchar(50) NOT NULL," + // foreign key(IdChoice) references choices(Id))
                    "Passwd varchar(50) NOT NULL);";
            stm.executeUpdate(sql);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static BookmakerDAO getInstance() {
        if (BookmakerDAO.singleton == null) {
            BookmakerDAO.singleton = new BookmakerDAO();
        }
        return BookmakerDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM bookmakers");
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
            ResultSet rs = stm.executeQuery("SELECT IdBmaker FROM bookmakers WHERE IdBmaker ='" + key.toString() + "'");
            r = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Bookmaker a = (Bookmaker) value;
        return this.containsKey(a.getIdBookmaker());
    }

    @Override
    public Bookmaker get(Object key) {
        Bookmaker a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bookmakers WHERE IdBmaker='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                a = new Bookmaker(rs.getString("IdBmaker"), rs.getString("Email"), rs.getString("Passwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    @Override
    public Bookmaker put(String key, Bookmaker value) {
        Bookmaker res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeQuery(
                    "MERGE INTO bookmakers AS t USING " +
                            "(SELECT IdBmaker='" + value.getIdBookmaker() + "', Email='" + value.getEmail() +
                            "', Passwd='" + value.getPassword() + "') AS s " +
                            "ON t.IdBmaker = s.IdBmaker " +
                            "WHEN MATCHED THEN " +
                            "UPDATE SET IdBmaker=s.IdBmaker, Email=s.Email, Passwd=s.Passwd " +
                            "WHEN NOT MATCHED THEN " +
                            "INSERT (IdBmaker, Email, Passwd) " +
                            "VALUES (s.IdBmaker, s.Email, s.Passwd);");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Bookmaker remove(Object key) {
        Bookmaker b = this.get(key);
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM bookmakers WHERE IdBmaker='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return b;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Bookmaker> bookmakers) {
        for (Bookmaker b : bookmakers.values()) {
            this.put(b.getIdBookmaker(), b);
        }
    }

    @Override
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

    @Override
    public Set<String> keySet() {
        Set<String> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBmaker FROM bookmakers");
            while (rs.next()) {
                col.add(rs.getString("IdBmaker"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Collection<Bookmaker> values() {
        Collection<Bookmaker> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBmaker FROM bookmakers");
            while (rs.next()) {
                col.add(this.get(rs.getString("IdBmaker")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Set<Entry<String, Bookmaker>> entrySet() {
        Map.Entry<String, Bookmaker> entry;
        HashSet<Entry<String, Bookmaker>> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdBmaker FROM bookmakers");
            while (rs.next()) {
                entry = new AbstractMap.SimpleEntry<>(rs.getString("IdBmaker"), this.get(rs.getString("IdBmaker")));
                col.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

}
