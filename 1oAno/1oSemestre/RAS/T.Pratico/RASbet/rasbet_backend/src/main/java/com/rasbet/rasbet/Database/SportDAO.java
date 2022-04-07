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

import com.rasbet.rasbet.BookmakerBL.Competition;
import com.rasbet.rasbet.BookmakerBL.Sport;

public class SportDAO implements Map<String, Sport> {

	private static SportDAO singleton = null;
	private Connection connection;

	public SportDAO() {
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			if (connection != null) {
				System.out.println("Connection Successful!");
			}
			Statement stm = connection.createStatement();
			String sql = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='sports')" +
					"CREATE TABLE sports (" +
					"IdSport varchar(50) NOT NULL PRIMARY KEY," +
					"Name varchar(50) NOT NULL," +
					"Type int NOT NULL," +
					"Typology int NOT NULL);";
			stm.executeUpdate(sql);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	// Implementação do padrão Singleton
	// devolve a instância única desta classe
	public static SportDAO getInstance() {
		if (SportDAO.singleton == null) {
			SportDAO.singleton = new SportDAO();
		}
		return SportDAO.singleton;
	}

	@Override
	public int size() {
		int size = 0;
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT count(*) FROM sports");
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
			ResultSet rs = stm.executeQuery("SELECT IdSport FROM sports WHERE IdSport ='" + key.toString() + "'");
			r = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return r;
	}

	@Override
	public boolean containsValue(Object value) {
		Sport a = (Sport) value;
		return this.containsKey(a.getIdSport());
	}

	@Override
	public Sport get(Object key) {
		Sport a = null;
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM sports WHERE IdSport='" + key + "'");
			if (rs.next()) { // A chave existe na tabela
				String idSport = rs.getString("IdSport");
				a = new Sport(idSport, rs.getString("Name"), rs.getInt("Type"), rs.getInt("Typology"),
						getCompetitions(stm, idSport));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return a;
	}

	public Set<Competition> getCompetitions(Statement stm, String idSport) {
		Set<Competition> competitions = new HashSet<>();
		try (ResultSet rs = stm.executeQuery("SELECT * FROM competitions WHERE IdSport='" + idSport + "'")) {
			while (rs.next()) { // Percorrer todas as competitions
				Competition c = new Competition(rs.getString("IdComp"), rs.getString("Name"),rs.getString("IdSport"));
				competitions.add(c);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return competitions;
	}

	@Override
	public Sport put(String key, Sport value) {
		Sport res = null;
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			stm.executeQuery(
					"MERGE INTO sports AS t USING " +
							"(SELECT IdSport='" + value.getIdSport() + "', Name='" + value.getName() +
							"', Type='" + value.getType() + "', Typology='" + value.getTypology() + "') AS s " +
							"ON t.IdSport = s.IdSport " +
							"WHEN MATCHED THEN " +
							"UPDATE SET IdSport=s.IdSport, Name=s.Name, Type=s.Type, Typology=s.Typology " +
							"WHEN NOT MATCHED THEN " +
							"INSERT (IdSport, Name, Type, Typology) " +
							"VALUES (s.IdSport, s.Name, s.Type, s.Typology);");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return res;
	}

	public void putCompetitions(Statement stm, Set<Competition> comps, String idSport) {
		for (Competition value : comps) {
			try {
				stm.executeQuery(
						"MERGE INTO sports AS t USING " +
								"(SELECT IdCompetition='" + value.getIdCompetition() + "', Name='" + value.getName() +
								"', IdSport='" + idSport + "') AS s " +
								"ON t.IdCompetition = s.IdCompetition " +
								"WHEN MATCHED THEN " +
								"UPDATE SET IdCompetition=s.IdCompetition, Name=s.Name, IdSport=s.IdSport " +
								"WHEN NOT MATCHED THEN " +
								"INSERT (IdCompetition, Name, IdSport) " +
								"VALUES (s.IdCompetition, s.Name, s.IdSport);");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Sport remove(Object key) {
		Sport b = this.get(key);
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			stm.executeUpdate("DELETE FROM sports WHERE IdSport='" + key + "'");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return b;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Sport> sports) {
		for (Sport b : sports.values()) {
			this.put(b.getIdSport(), b);
		}

	}

	@Override
	public void clear() {
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			stm.executeUpdate("UPDATE sports SET IdSport=NULL");
			stm.executeUpdate("TRUNCATE sports");
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
			ResultSet rs = stm.executeQuery("SELECT IdSport FROM sports");
			while (rs.next()) {
				col.add(rs.getString("IdSport"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return col;
	}

	@Override
	public Collection<Sport> values() {
		Collection<Sport> col = new HashSet<>();
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT IdSport FROM sports");
			while (rs.next()) {
				col.add(this.get(rs.getString("IdSport")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return col;
	}

	@Override
	public Set<Entry<String, Sport>> entrySet() {
		Map.Entry<String, Sport> entry;
		HashSet<Entry<String, Sport>> col = new HashSet<>();
		try {
			Class.forName(DAOconfig.className);
			connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT IdSport FROM sports");
			while (rs.next()) {
				entry = new AbstractMap.SimpleEntry<>(rs.getString("IdSport"), this.get(rs.getString("IdSport")));
				col.add(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return col;
	}

}