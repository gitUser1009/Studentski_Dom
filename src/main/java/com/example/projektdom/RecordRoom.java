package com.example.projektdom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordRoom {
    private final StringProperty idRoom;
    private final StringProperty building;
    private final StringProperty roomNumber;
    private final StringProperty numberOfBeds;
    private final StringProperty numberOfFreeBeds;
    private final StringProperty ac;
    private final StringProperty balcony;
    private final StringProperty fridge;

    public RecordRoom() {
        idRoom = new SimpleStringProperty(this, "idRoom");
        building = new SimpleStringProperty(this, "building");
        roomNumber = new SimpleStringProperty(this, "roomNumber");
        numberOfBeds = new SimpleStringProperty(this, "numberOfBeds");
        numberOfFreeBeds = new SimpleStringProperty(this, "numberOfFreeBeds");
        ac = new SimpleStringProperty(this, "ac");
        balcony = new SimpleStringProperty(this, "balcony");
        fridge = new SimpleStringProperty(this, "fridge");
    }

    PreparedStatement pst;
    Connection conn;
    public void Connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/stdom", "root", "1234");
        }
        catch (SQLException ex){
            System.out.println("SQL Error: " + ex);
        }
    }

    //Funkcija vraca broj slobodnih kreveta (Broj kreveta u sobi - Broj smjestenih studenata u sobi)
    Integer getNumberOfStudentsInRoom(){
        Connect();
        Integer studentNumber = 0;
        try{
            pst = conn.prepareStatement("SELECT COUNT(smjestaj.fksoba) AS brojSmjestaja FROM sobe LEFT JOIN smjestaj ON (sobe.idsobe = smjestaj.fksoba) WHERE idsobe=? GROUP BY sobe.idsobe");
            pst.setString(1, getIdRoom());
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                studentNumber = rs.getInt("brojSmjestaja");
            }
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Integer bedNumber = Integer.parseInt(getNumberOfBeds());
        return bedNumber - studentNumber;
    }

    //Getteri
    public String getIdRoom() {
        return idRoom.get();
    }

    public StringProperty idRoomProperty() {
        return idRoom;
    }

    public String getBuilding() {
        return building.get();
    }

    public StringProperty buildingProperty() {
        return building;
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public String getNumberOfBeds() {
        return numberOfBeds.get();
    }

    public StringProperty numberOfBedsProperty() {
        return numberOfBeds;
    }

    public String getAc() {
        return ac.get();
    }

    public StringProperty acProperty() {
        return ac;
    }

    public String getBalcony() { return balcony.get(); }

    public StringProperty balconyProperty() {
        return balcony;
    }

    public String getFridge() {
        return fridge.get();
    }

    public StringProperty fridgeProperty() {
        return fridge;
    }

    //Seteri
    public void setIdRoom(String idRoom) {
        this.idRoom.set(idRoom);
    }

    public void setBuilding(String building) {
        this.building.set(building);
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public void setNumberOfBeds(String numberOfBeds) {
        this.numberOfBeds.set(numberOfBeds);
    }

    public void setAc(String ac) {
        this.ac.set(ac);
    }

    public void setBalcony(String balcony) {
        this.balcony.set(balcony);
    }

    public void setFridge(String fridge) {
        this.fridge.set(fridge);
    }

    //GETERI SETERI ZA NUMBER OF FREE BEDS
    public String getNumberOfFreeBeds() {
        return numberOfFreeBeds.get();
    }

    public StringProperty numberOfFreeBedsProperty() {
        numberOfFreeBeds.set(getNumberOfStudentsInRoom().toString());
        return numberOfFreeBeds;
    }

    public void setNumberOfFreeBeds(String numberOfFreeBeds) {
        this.numberOfFreeBeds.set(numberOfFreeBeds);
    }

    @Override
    public String toString() {
        return "RecordRoom{" +
                "idRoom=" + idRoom +
                ", building=" + building +
                ", roomNumber=" + roomNumber +
                ", numberOfBeds=" + numberOfBeds +
                ", numberOfFreeBeds=" + numberOfFreeBeds +
                ", ac=" + ac +
                ", balcony=" + balcony +
                ", fridge=" + fridge +
                '}';
    }
}
