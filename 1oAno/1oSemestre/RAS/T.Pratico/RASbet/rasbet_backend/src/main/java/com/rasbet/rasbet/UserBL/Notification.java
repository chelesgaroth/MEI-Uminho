package com.rasbet.rasbet.UserBL;

import java.time.LocalDateTime;

public class Notification {

    private String idNotification;
    private String title;
    private String message;
    private LocalDateTime dateReceived;
    private int deleted;

    public Notification(String idNotification, String title, String message, LocalDateTime dateReceived, int deleted) {
        // this.idNotification = Integer.toString(this.counter);
        this.title = title;
        this.message = message;
        this.dateReceived = dateReceived;
        this.deleted = 0;

    }

    public String getIdNotification() {
        return this.idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateReceived() {
        return this.dateReceived;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

}