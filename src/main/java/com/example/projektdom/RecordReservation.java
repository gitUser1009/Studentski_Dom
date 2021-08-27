package com.example.projektdom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecordReservation {
    private final StringProperty idSmjestaj;
    private final StringProperty firstName;
    private final StringProperty surname;
    private final StringProperty building;
    private final StringProperty roomNumber;
    private final StringProperty price;

    public RecordReservation() {
        idSmjestaj = new SimpleStringProperty(this, "idSmjestaj");
        firstName = new SimpleStringProperty(this, "firstName");
        surname = new SimpleStringProperty(this, "surname");
        building = new SimpleStringProperty(this, "building");
        roomNumber = new SimpleStringProperty(this, "roomNumber");
        price = new SimpleStringProperty(this, "price");
    }

    //GETERI, SETERI
    public String getIdSmjestaj() {
        return idSmjestaj.get();
    }

    public StringProperty idSmjestajProperty() {
        return idSmjestaj;
    }

    public void setIdSmjestaj(String idSmjestaj) {
        this.idSmjestaj.set(idSmjestaj);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getBuilding() {
        return building.get();
    }

    public StringProperty buildingProperty() {
        return building;
    }

    public void setBuilding(String building) {
        this.building.set(building);
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    @Override
    public String toString() {
        return "RecordReservation{" +
                "idSmjestaj=" + idSmjestaj +
                ", firstName=" + firstName +
                ", surname=" + surname +
                ", building=" + building +
                ", roomNumber=" + roomNumber +
                ", price=" + price +
                '}';
    }
}
