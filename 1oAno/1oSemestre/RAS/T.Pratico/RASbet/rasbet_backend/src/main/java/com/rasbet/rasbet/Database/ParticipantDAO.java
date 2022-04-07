package com.rasbet.rasbet.Database;

import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rasbet.rasbet.BookmakerBL.Participant;

public class ParticipantDAO implements Map<String, Participant> {
    private static ParticipantDAO singleton = null;
    private Connection connection;

    public ParticipantDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='participants')" +
                    "CREATE TABLE participants (" +
                    "IdParticipant varchar(50) NOT NULL PRIMARY KEY," +
                    "Name varchar(50) NOT NULL," +
                    "IdSport varchar(50) NOT NULL, foreign key(IdSport) references sports(IdSport));";
            stm.executeUpdate(sql);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static ParticipantDAO getInstance() {
        if (ParticipantDAO.singleton == null) {
            ParticipantDAO.singleton = new ParticipantDAO();
        }
        return ParticipantDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM participants");
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
            ResultSet rs = stm.executeQuery(
                    "SELECT IdParticipant FROM participants WHERE IdParticipant ='" + key.toString() + "'");
            r = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Participant get(Object key) {
        Participant a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM participants WHERE IdParticipant='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                a = new Participant(rs.getString("IdParticipant"), rs.getString("Name"), rs.getString("IdSport"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    @Override
    public Participant put(String key, Participant value) {
        Participant res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "MERGE INTO participants AS t USING " +
                            "(SELECT IdParticipant='" + value.getIdParticipant() + "', Name='" + value.getName() +
                            "', IdSport='" + value.getIdSport() + "') AS s " +
                            "ON t.IdParticipant = s.IdParticipant " +
                            "WHEN MATCHED THEN " +
                            "UPDATE SET IdParticipant=s.IdParticipant, Name=s.Name, IdSport=s.IdSport " +
                            "WHEN NOT MATCHED THEN " +
                            "INSERT (IdParticipant, Name, IdSport) " +
                            "VALUES (s.IdParticipant, s.Name, s.IdSport);");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Participant remove(Object key) {
        Participant b = this.get(key);
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM participant WHERE IdParticipant='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return b;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Participant> m) {
        for (Participant b : m.values()) {
            this.put(b.getIdParticipant(), b);
        }

    }

    @Override
    public void clear() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("UPDATE participants SET IdParticipant=NULL");
            stm.executeUpdate("TRUNCATE participants");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

    }

    @Override
    public Set<String> keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Participant> values() {
        Collection<Participant> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdParticipant FROM participants");
            while (rs.next()) {
                col.add(this.get(rs.getString("IdParticipant")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Set<Entry<String, Participant>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }
}