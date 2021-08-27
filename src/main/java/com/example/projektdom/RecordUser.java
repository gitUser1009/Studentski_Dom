package com.example.projektdom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecordUser {
    private final StringProperty userID;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty rightsUsers;
    private final StringProperty rightsStudents;
    private final StringProperty rightsRooms;
    private final StringProperty rightsReservations;

    public RecordUser() {
        userID = new SimpleStringProperty(this, "userID");
        username = new SimpleStringProperty(this, "username");
        password = new SimpleStringProperty(this, "password");
        rightsUsers = new SimpleStringProperty(this, "rightsUsers");
        rightsStudents = new SimpleStringProperty(this, "rightsStudents");
        rightsRooms = new SimpleStringProperty(this, "rightsRooms");
        rightsReservations = new SimpleStringProperty(this, "rightsReservations");
    }

    //GETERI, SETERI
    public String getUserID() {
        return userID.get();
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRightsUsers() {
        return rightsUsers.get();
    }

    public StringProperty rightsUsersProperty() {
        return rightsUsers;
    }

    public void setRightsUsers(String rightsUsers) {
        this.rightsUsers.set(rightsUsers);
    }

    public String getRightsStudents() {
        return rightsStudents.get();
    }

    public StringProperty rightsStudentsProperty() {
        return rightsStudents;
    }

    public void setRightsStudents(String rightsStudents) {
        this.rightsStudents.set(rightsStudents);
    }

    public String getRightsRooms() {
        return rightsRooms.get();
    }

    public StringProperty rightsRoomsProperty() {
        return rightsRooms;
    }

    public void setRightsRooms(String rightsRooms) {
        this.rightsRooms.set(rightsRooms);
    }

    public String getRightsReservations() {
        return rightsReservations.get();
    }

    public StringProperty rightsReservationsProperty() {
        return rightsReservations;
    }

    public void setRightsReservations(String rightsReservations) {
        this.rightsReservations.set(rightsReservations);
    }

    @Override
    public String toString() {
        return "UsersRecord{" +
                "userID=" + userID +
                ", username=" + username +
                ", password=" + password +
                ", rightsUsers=" + rightsUsers +
                ", rightsStudents=" + rightsStudents +
                ", rightsRooms=" + rightsRooms +
                ", rightsReservations=" + rightsReservations +
                '}';
    }
}
