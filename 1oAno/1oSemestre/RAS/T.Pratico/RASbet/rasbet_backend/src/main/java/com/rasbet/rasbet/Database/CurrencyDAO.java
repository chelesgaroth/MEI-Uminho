package com.rasbet.rasbet.Database;

import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rasbet.rasbet.UserBL.Currency;

public class CurrencyDAO implements Map<String, Currency> {
    private static CurrencyDAO singleton = null;
    private Connection connection;

    public CurrencyDAO() {
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            if (connection != null) {
                System.out.println("Connection Successful!");
            }
            Statement stm = connection.createStatement();
            String sql = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='currencies')" +
                    "CREATE TABLE currencies (" +
                    "IdCurrency varchar(50) NOT NULL PRIMARY KEY," +
                    "Name varchar(50) NOT NULL);";
            stm.executeUpdate(sql);

            String sql1 = "IF NOT EXISTS ( SELECT * FROM sys.tables where name='currencyTax')" +
                    "CREATE TABLE currencyTax (" +
                    "IdCurrencyOri varchar(50) NOT NULL , foreign key(IdCurrencyOri) references currencies(IdCurrency),"
                    +
                    "IdCurrencyDest varchar(50) NOT NULL , foreign key(IdCurrencyOri) references currencies(IdCurrency),"
                    +
                    "Tax float(10) NOT NULL," +
                    "PRIMARY KEY (IdCurrencyOri,IdCurrencyDest));";
            stm.executeUpdate(sql1);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static CurrencyDAO getInstance() {
        if (CurrencyDAO.singleton == null) {
            CurrencyDAO.singleton = new CurrencyDAO();
        }
        return CurrencyDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM currencies");
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Currency get(Object key) {
        Currency a = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM currencies WHERE IdCurrency='" + key + "'");
            if (rs.next()) { // A chave existe na tabela
                String idCurrency = rs.getString("IdCurrency");
                a = new Currency(idCurrency, rs.getString("Name"), getCurrencyTaxes(stm, idCurrency));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    public Map<String, Float> getCurrencyTaxes(Statement stm, String idCurrency) {
        Map<String, Float> currencyTaxes = new HashMap<>();
        try {
            ResultSet rs = stm.executeQuery("SELECT * FROM currencyTax WHERE IdCurrencyOri='" + idCurrency + "'");
            if (rs.next()) {
                currencyTaxes.put(rs.getString("IdCurrencyDest"), rs.getFloat("Tax"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return currencyTaxes;
    }

    @Override
    public Currency put(String key, Currency value) {
        Currency res = null;
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "MERGE INTO currencies AS t USING " +
                            "(SELECT IdCurrency='" + value.getIdCurrency() + "', Name='" + value.getName() + "') AS s "
                            +
                            "ON t.IdCurrency = s.IdCurrency " +
                            "WHEN MATCHED THEN " +
                            "UPDATE SET IdCurrency=s.IdCurrency, Name=s.Name " +
                            "WHEN NOT MATCHED THEN " +
                            "INSERT (IdCurrency, Name) " +
                            "VALUES (s.IdCurrency, s.Name);");
            putCurrencyTaxes(stm, value.getCurrencyTaxes(), key);

        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public void putCurrencyTaxes(Statement stm, Map<String, Float> currTaxes, String idCurr) {

        for (Map.Entry<String, Float> entry : currTaxes.entrySet()) {
            try {
                stm.executeUpdate("MERGE INTO currencyTax AS t USING " +
                        "(SELECT IdCurrencyOri='" + idCurr + "', IdCurrencyDest='" + entry.getKey() +
                        "', Tax='"
                        + entry.getValue() +
                        "') AS s " +
                        "ON t.IdCurrencyOri= s.IdCurrencyOri AND t.IdCurrencyDest= s.IdCurrencyDest" +
                        "WHEN MATCHED THEN " +
                        "UPDATE SET IdCurrencyOri= s.IdCurrencyOri, IdCurrencyDest= s.IdCurrencyDest, Tax=s.Tax" +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (IdCurrencyOri, IdCurrencyDest, Tax) " +
                        "VALUES (s.IdCurrencyOri, s.IdCurrencyDest, s.Tax);");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public Currency remove(Object key) {
        Currency b = this.get(key);
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM currencies WHERE IdCurrency='" + key + "'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return b;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Currency> m) {
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
    public Collection<Currency> values() {
        Collection<Currency> col = new HashSet<>();
        try {
            Class.forName(DAOconfig.className);
            connection = DriverManager.getConnection(DAOconfig.DATABASE_URL, DAOconfig.user, DAOconfig.password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT IdCurrency FROM currencies");
            while (rs.next()) {
                col.add(this.get(rs.getString("IdCurrency")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Set<Entry<String, Currency>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }

}
