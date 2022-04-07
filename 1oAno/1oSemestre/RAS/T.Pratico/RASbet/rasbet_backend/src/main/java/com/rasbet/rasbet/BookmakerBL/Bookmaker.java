package com.rasbet.rasbet.BookmakerBL;

public class Bookmaker {
    private String idBookmaker;
    private String email;
    private String password;



    public Bookmaker(String idBookmaker, String email, String password) {
        this.idBookmaker = idBookmaker;
        this.email = email;
        this.password = password;
    }


    public String getIdBookmaker() {
        return this.idBookmaker;
    }

    public void setIdBookmaker(String idBookmaker) {
        this.idBookmaker = idBookmaker;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
