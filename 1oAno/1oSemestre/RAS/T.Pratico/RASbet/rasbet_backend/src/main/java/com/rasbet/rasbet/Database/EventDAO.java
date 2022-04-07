package com.rasbet.rasbet.Database;

import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rasbet.rasbet.BookmakerBL.Choice;
import com.rasbet.rasbet.BookmakerBL.Event;
import com.rasbet.rasbet.BookmakerBL.Participant;

public class EventDAO implements Map<String, Event> {

    private static EventDAO singleton = null;
    private Connection connection;

    public EventDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql1 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='competitions')" +
                    "CREATE TABLE competitions (" +
                    "IdComp varchar(50) NOT NULL PRIMARY KEY," +
                    "Name varchar(50) NOT NULL," +
                    "IdSport varchar(50) NOT NULL, foreign key(IdSport) references sports(IdSport));";
            stm.executeUpdate(sql1);

            String sql2 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='events')" +
                    "CREATE TABLE events (" +
                    "IdEvent varchar(50) NOT NULL PRIMARY KEY," +
                    "InitialDate datetime NOT NULL," +
                    "IdBmaker varchar(50) NOT NULL, foreign key(IdBmaker) references bookmakers(IdBmaker)," +
                    "IdComp varchar(50) NOT NULL, foreign key(IdComp) references competitions(IdComp)," +
                    "FinalResult varchar(50) DEFAULT NULL, " +
                    "EventStatus int NOT NULL, " +
                    "Description varchar(50) NOT NULL);";
            stm.executeUpdate(sql2);

            String sql3 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='participantEvent')" +
                    "CREATE TABLE participantEvent (" +
                    "IdParticipant varchar(50) NOT NULL, foreign key(IdParticipant ) references participants(IdParticipant),"
                    +
                    "IdEvent varchar(50) NOT NULL, foreign key(IdEvent) references events(IdEvent)," +
                    "PRIMARY KEY (IdParticipant,IdEvent));";
            stm.executeUpdate(sql3);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static EventDAO getInstance() {
        if (EventDAO.singleton == null) {
            EventDAO.singleton = new EventDAO();
        }
        return EventDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM events");
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
            ResultSet rs = stm.executeQuery("SELECT IdEvent FROM events WHERE IdEvent ='" + key.toString() + "'");
            r = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Event a = (Event) value;
        return this.containsKey(a.getIdEvent());
    }

    @Override
    public Event get(Object key) {
        Event a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM events WHERE IdEvent='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                String IdEvent = rs.getString("IdEvent");
                a = new Event(IdEvent, (rs.getTimestamp("InitialDate")).toLocalDateTime(),
                        rs.getString("FinalResult"),
                        rs.getInt("EventStatus"), rs.getString("Description"), rs.getString("IdBmaker"),
                        rs.getString("IdComp"),
                        getParticipants(IdEvent), getChoices(stm, IdEvent));

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    public Set<Participant> getParticipants(String IdEvent) {
        Set<Participant> participants = new HashSet<>();
        ResultSet rs;
        try {
            Statement stm = connection.createStatement();
            rs = stm.executeQuery("SELECT IdParticipant FROM participantEvent WHERE IdEvent='" + IdEvent + "'");
            String idParticipant;
            while (rs.next()) {
                idParticipant = rs.getString("IdParticipant");
                Statement stm2 = connection.createStatement();
                ResultSet rs2 = stm2.executeQuery("SELECT * FROM participants WHERE IdParticipant='" + idParticipant + "'");
                while(rs2.next()) {
                    Participant p = new Participant(rs2.getString("IdParticipant"), rs2.getString("Name"),
                            rs2.getString("IdSport"));
                    participants.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return participants;
    }

    public Set<Choice> getChoices(Statement stm, String IdEvent) {
        Set<Choice> choices = new HashSet<>();
        ResultSet rs;
        try {
            rs = stm.executeQuery("SELECT * FROM choices WHERE IdEvent='" + IdEvent + "'");
            while (rs.next()) {
                Choice p = new Choice(rs.getString("IdChoice"), rs.getFloat("Odd"), rs.getInt("Status"),
                        rs.getString("Result"));
                choices.add(p);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return choices;
    }

    @Override
    public Event put(String key, Event value) {
        Event res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "MERGE INTO events AS t USING " +
                            "(SELECT IdEvent='" + value.getIdEvent() + "', InitialDate='" + Timestamp.valueOf(value.getInitialDate()) +
                            "', IdBmaker='" + value.getIdBookmaker() + "', IdComp='" + value.getIdCompetition()
                            + "', FinalResult='" + value.getFinalResult() + "', EventStatus='" + value.getEventStatus()
                            + "', Description='" + value.getDescription() + "') AS s " +
                            "ON t.IdEvent = s.IdEvent " +
                            "WHEN MATCHED THEN " +
                            "UPDATE SET IdEvent=s.IdEvent, InitialDate=s.InitialDate, IdBmaker=s.IdBmaker, IdComp=s.IdComp, FinalResult=s.FinalResult, EventStatus=s.EventStatus, Description=s.Description "
                            +
                            "WHEN NOT MATCHED THEN " +
                            "INSERT (IdEvent, InitialDate, IdBmaker, IdComp, FinalResult, EventStatus, Description) " +
                            "VALUES (s.IdEvent, s.InitialDate, s.IdBmaker, s.IdComp, s.FinalResult, s.EventStatus, s.Description);");
            putParticipants(value.getParticipants(), value.getIdEvent());
            putChoices(value.getChoices(), value.getIdEvent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public void putParticipants(Set<Participant> participants, String idEvent) {

        try {
            for (Participant p : participants) {
                Statement stm = connection.createStatement();
                stm.executeUpdate(
                        "MERGE INTO participantEvent AS t USING " +
                                "(SELECT IdParticipant='" + p.getIdParticipant() + "', IdEvent='" + idEvent + "') AS s "
                                +
                                "ON (t.IdParticipant = s.IdParticipant AND t.IdEvent = s.IdEvent) " +
                                "WHEN MATCHED THEN " +
                                "UPDATE SET IdParticipant = s.IdParticipant, IdEvent=s.IdEvent " +
                                "WHEN NOT MATCHED THEN " +
                                "INSERT (IdParticipant, IdEvent) " +
                                "VALUES (s.IdParticipant, s.IdEvent);");
                Map<String, Participant> participantsMap = ParticipantDAO.getInstance();
                participantsMap.put(p.getIdParticipant(), p);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void putChoices(Set<Choice> choices, String idEvent) {

        try {
            for (Choice value : choices) {
                System.out.println(value.toString());
                Statement stm = connection.createStatement();
                stm.executeUpdate(
                        "MERGE INTO choices AS t USING " +
                                "(SELECT IdChoice='" + value.getIdChoice() + "', Odd='" + value.getOdd() +
                                "', IdEvent='" + idEvent + "', Status='" + value.getStatus()
                                + "', Result='" + value.getResult() + "') AS s " +
                                "ON t.IdChoice = s.IdChoice " +
                                "WHEN MATCHED THEN " +
                                "UPDATE SET IdChoice=s.IdChoice, Odd=s.Odd, IdEvent=s.IdEvent, Status=s.Status, Result=s.Result "
                                +
                                "WHEN NOT MATCHED THEN " +
                                "INSERT (IdChoice, Odd, IdEvent, Status, Result) "
                                +
                                "VALUES (s.IdChoice, s.Odd, s.IdEvent, s.Status, s.Result);");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Event remove(Object key) {
        Event b = this.get(key);
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM events WHERE IdEvent='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return b;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Event> m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("UPDATE events SET IdEvent=NULL");
            stm.executeUpdate("TRUNCATE events");
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
            ResultSet rs = stm.executeQuery("SELECT IdEvent FROM events");
            while (rs.next()) {
                col.add(rs.getString("IdEvent"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Collection<Event> values() {
        Collection<Event> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdEvent FROM events");
            while (rs.next()) {
                col.add(this.get(rs.getString("IdEvent")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Set<Entry<String, Event>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }
}