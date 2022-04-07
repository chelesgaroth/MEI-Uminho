package com.rasbet.rasbet.UserBL;

import java.util.Map;

public class Currency {

    private String idCurrency;
    private String name;
    private Map<String, Float> currencyTaxes;

    public Currency(String idCurrency, String name, Map<String, Float> currencyTaxes) {
        this.idCurrency = idCurrency;
        this.name = name;
        this.currencyTaxes = currencyTaxes;
    }

    public String getIdCurrency() {
        return this.idCurrency;
    }

    public void setIdCurrency(String idCurrency) {
        this.idCurrency = idCurrency;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Float> getCurrencyTaxes() {
        return this.currencyTaxes;
    }

    public void setCurrencyTaxes(Map<String, Float> currencyTaxes) {
        this.currencyTaxes = currencyTaxes;
    }

    @Override
    public String toString() {
        return "{" +
                " idCurrency='" + getIdCurrency() + "'" +
                ", name='" + getName() + "'" +
                "}";
    }

}