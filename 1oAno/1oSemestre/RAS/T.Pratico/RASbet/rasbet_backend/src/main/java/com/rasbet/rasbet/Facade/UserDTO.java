package com.rasbet.rasbet.Facade;


import java.time.LocalDate;

public class UserDTO {
    private String name;
    private String idUser;
    private String email;
    private String password;
    private String  dob;

public UserDTO () {

}

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", idUser='" + idUser + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dob=" + dob +
                '}';
    }

    public UserDTO(String name, String idUser, String email, String password, String dob) {
        this.name = name;
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
