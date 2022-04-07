package  com.rasbet.rasbet.BookmakerBL;
import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.time.LocalDateTime;
import java.util.*;


import com.rasbet.rasbet.Database.*;
import com.rasbet.rasbet.IdCounterInterface;

/**
 * Bookmakers Subsystem Facade
 */

public class BookmakerFacade implements IdCounterInterface {

    Map<String, Sport> sports;
    Map<String, Bookmaker> bookmakers;
    Map<String, Event> events;
    Map<String, Participant> participants;
    int eventCounter;
    int choiceCounter;

    public BookmakerFacade() {
        this.sports = SportDAO.getInstance();
        this.bookmakers = BookmakerDAO.getInstance();
        this.participants = ParticipantDAO.getInstance();
        this.events = EventDAO.getInstance();
        updateChoiceCounter();
        updateEventCounter();
    }

    // Feed de eventos para os users
    public List<Object> getFeed() {
        List<Object> listEvent = new ArrayList<>();
        for (Event e : this.events.values()) {
            e.setSport(getSportEvent(e));
            listEvent.add(e);
        }
        return listEvent;
    }

    // Vai buscar o desporto de uma competição de um evento
    public String getSportEvent(Event e) {
        String comp = e.getIdCompetition();
        Competition c = getCompetitions(comp);
        Sport s = this.sports.get(c.getIdSport());
        return s.getName();
    }

    // eventos que um bookmaker fez
    public List<String> checkBook(String idBookmaker) {

        List<String> listEvent = new ArrayList<>();
        for (Event e : this.events.values()) {
            if (e.getIdBookmaker().equals(idBookmaker))
                listEvent.add(e.toString());
        }
        return listEvent;
    }

    // done
    public List<Object> getAllSports() {
        Map<String, Sport> sports = this.sports;
        List<Object> res = new ArrayList<>();
        for (Sport s : sports.values()) {
            res.add(s);
        }
        return res;
    }

    // done
    public List<Object> getAllComps() {
        List<Object> res = new ArrayList<>();
        Map<String, Sport> sports = this.sports;
        for (Sport s : sports.values()) {
            Set<Competition> comps = new HashSet<>();
            comps = s.getCompetitions();
            for (Competition c : comps) {
                res.add(c);
            }
        }
        return res;
    }

    public boolean verifyBookmaker(String email, String password) {

        for (Bookmaker b : this.bookmakers.values()) {
            if (b.getEmail().equals(email) && b.getPassword().equals(password))
                return true;
        }
        return false;

    }

    public String getBookmakerID(String email, String password) {
        for (Bookmaker u : this.bookmakers.values()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
                return u.getIdBookmaker();
        }
        return null;
    }

    public void addNewEvent(String competition, List<String> participants, LocalDateTime date,
                            Map<String, Float> choices, String idBookmaker) {

        Set<Participant> setP = new HashSet<>();
        Set<Choice> choiceList = new HashSet<>();
        for (Map.Entry<String, Float> entry : choices.entrySet()) {
            Choice c = new Choice(getNewChoiceId(), entry.getValue(), 1, "");
            choiceList.add(c);
        }
        for (String sp : participants) {
            Participant p = this.participants.get(sp);
            setP.add(p);
        }
        String aux  = getNewEventId();
        Event e = new Event(aux, date, "", 1, "", idBookmaker, competition, setP, choiceList);
        events.put(e.getIdEvent(), e);
    }

    public Set<String> getAllChoices() {
        Set<String> choices = new HashSet<>();
        for (Event e : events.values()) {
            for (Choice c : e.getChoices()) {
                choices.add(c.getIdChoice());
            }
        }
        return choices;
    }

    public String getNewChoiceId() {
        String id = new String("C" + choiceCounter);
        choiceCounter++;
        return id;
    }

    public void updateChoiceCounter() {
        choiceCounter = getCounter(getAllChoices());
    }

    public void updateEventDescription(String idEvent, String newDescript) {
        Event e = this.events.get(idEvent);
        e.setDescription(newDescript);
        this.events.put(idEvent, e);

    }





    public void suspendEventBets(String idEvent, List<String> choices) {

    }

    public void reopenEventBets(String idEvent, List<String> choices) {

    }




    public void changeChoicesStatus(String idEvent, Map<String, Integer> choices) {
        Event e = this.events.get(idEvent);
        e.changeChoicesStatus(choices);

    }

    public void updateEventOdds(String idEvent, Map<String, Float> choices) {
        Event e = this.events.get(idEvent);
        e.updateChoicesOdds(choices);
    }

    /**
     * Mudar o estado de todas as apostas para fechado
     */
    public void closeEvent(String idEvent, String endResult) {
        Event e = events.get(idEvent);
        e.closeEvent(endResult);
        events.put(idEvent, e);
    }

    public void cancelEvent(String idEvent) {

    }

    public List<String> getAllParticipants() {
        List<String> res = new ArrayList<>();
        for (Participant p : this.participants.values()) {
            res.add(p.toString());
        }
        return res;
    }

    private String getNewEventId() {
        String id = new String("E" + eventCounter);
        eventCounter++;
        return id;
    }

    private void updateEventCounter() {
        eventCounter = getCounter(events.keySet());
    }

    /*
    private Competition getCompetitionByID (String competitionID) {

    }*/


    //-------------------------------------------------------------------------------------------------------------
























    public Competition getCompetitions(String idComp)  {
        Competition c = null;
        String DATABASE_URL = "jdbc:sqlserver://rasbet-server.database.windows.net:1433;DatabaseName=RASbet_db;encrypt=true;trustServerCertificate=true;";
        String user = "rasadmin";
        String password = "rasbet_1234";
        String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(DATABASE_URL, user, password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM competitions WHERE IdComp='" + idComp + "'");
            if(rs.next()){
                c = new Competition(rs.getString("IdComp"),rs.getString("Name"),rs.getString("IdSport"));

            }

        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c;
    }
}