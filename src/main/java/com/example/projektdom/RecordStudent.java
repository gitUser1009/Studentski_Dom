package com.example.projektdom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecordStudent {
    private final StringProperty id;
    private final StringProperty firstName;
    private final StringProperty surname;
    private final StringProperty college;
    private final StringProperty year;
    private final StringProperty county;
    private final StringProperty city;
    private final StringProperty points;

    public RecordStudent() {
        id = new SimpleStringProperty(this, "id");
        firstName = new SimpleStringProperty(this, "firstName");
        surname = new SimpleStringProperty(this, "surname");
        college = new SimpleStringProperty(this, "college");
        year = new SimpleStringProperty(this, "year");
        county = new SimpleStringProperty(this, "county");
        city = new SimpleStringProperty(this, "city");
        points = new SimpleStringProperty(this, "points");
    }

    //Getteri
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() { return firstName; }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getCollege() {
        return college.get();
    }

    public StringProperty collegeProperty() {
        return college;
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public String getCounty() {
        return county.get();
    }

    public StringProperty countyProperty() {
        return county;
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getPoints() {
        return points.get();
    }

    public StringProperty pointsProperty() {
        return points;
    }

    //Setteri
    public void setId(String id) {
        this.id.set(id);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setCollege(String college) {
        this.college.set(college);
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public void setCounty(String county) {
        this.county.set(county);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setPoints(String points) {
        this.points.set(points);
    }

    @Override
    public String toString() {
        return "recordStudent{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", surname=" + surname +
                ", college=" + college +
                ", year=" + year +
                ", county=" + county +
                ", city=" + city +
                ", points=" + points +
                '}';
    }
}
