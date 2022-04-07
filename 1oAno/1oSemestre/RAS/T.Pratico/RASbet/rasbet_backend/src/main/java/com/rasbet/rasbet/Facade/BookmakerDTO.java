package com.rasbet.rasbet.Facade;

public class BookmakerDTO {
    private String idBookmaker;

    public BookmakerDTO( ) {
    }

    public BookmakerDTO(String idBookmaker) {
        this.idBookmaker = idBookmaker;
    }

    public String getIdBookmaker() {
        return idBookmaker;
    }

    public void setIdBookmaker(String idBookmaker) {
        this.idBookmaker = idBookmaker;
    }

    @Override
    public String toString() {
        return "BookmakerDTO{" +
                "idBookmaker='" + idBookmaker + '\'' +
                '}';
    }
}
