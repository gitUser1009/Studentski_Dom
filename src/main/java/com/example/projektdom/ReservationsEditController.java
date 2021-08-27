package com.example.projektdom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationsEditController implements Initializable {
    String pickedID = "";
    String filterName = "";
    String filterSurname = "";
    String filterBuilding = "";
    String filterRoomNumber = "";
    String filterPrice1 = "";
    String filterPrice2 = "";

    @FXML
    private TextField tfFilterName;

    @FXML
    private TextField tfFilterSurname;

    @FXML
    private TextField tfFilterBuilding;

    @FXML
    private TextField tfFilterRoomNumber;

    @FXML
    private TextField tfFilterPrice1;

    @FXML
    private TextField tfFilterPrice2;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonFilterClear;

    @FXML
    private TableView<RecordReservation> tableViewReservation;

    @FXML
    private TableColumn<RecordReservation, String> colID;

    @FXML
    private TableColumn<RecordReservation, String> colFirstName;

    @FXML
    private TableColumn<RecordReservation, String> colSurname;

    @FXML
    private TableColumn<RecordReservation, String> colBuilding;

    @FXML
    private TableColumn<RecordReservation, String> colRoomNumber;

    @FXML
    private TableColumn<RecordReservation, String> colPrice;

    @FXML
    private Button buttonDeleteReservation;

    @FXML
    private Label studentLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Button buttonToStudent;

    @FXML
    private Button buttonToRooms;

    @FXML
    private Button buttonToResCreate;

    @FXML
    private Button buttonToResEdit;

    @FXML
    private Button buttonEditPrice;

    @FXML
    private TextField tfEditPrice;

    @FXML
    private Button buttonToUsers;

    @FXML
    private Button buttonLogout;

    Connection conn;
    PreparedStatement pst;

    public void Connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/stdom", "root", "1234");
        }
        catch (SQLException ex){
            System.out.println("SQL Error: " + ex);
        }
    }

    public void tableReservationEdit(String queryString){
        ObservableList<RecordReservation> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryString);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    RecordReservation record = new RecordReservation();
                    record.setIdSmjestaj(rs.getString("idSmjestaj"));
                    record.setFirstName(rs.getString("ime"));
                    record.setSurname(rs.getString("Prezime"));
                    record.setBuilding(rs.getString("Zgrada"));
                    record.setRoomNumber(rs.getString("BrojSobe"));
                    record.setPrice(rs.getString("Cijena"));
                    records.add(record);
                }
            }
            tableViewReservation.setItems(records);
            colID.setCellValueFactory(f -> f.getValue().idSmjestajProperty());
            colFirstName.setCellValueFactory(f -> f.getValue().firstNameProperty());
            colSurname.setCellValueFactory(f -> f.getValue().surnameProperty());
            colBuilding.setCellValueFactory(f -> f.getValue().buildingProperty());
            colRoomNumber.setCellValueFactory(f -> f.getValue().roomNumberProperty());
            colPrice.setCellValueFactory(f -> f.getValue().priceProperty());
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void actionDeleteReservation(ActionEvent event) {
        if(pickedID.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fail");
            alert.setHeaderText("Morate odabrati rezervaciju koju zelite izbrisati");
            alert.setContentText("");
            alert.showAndWait();
        }
        else{
            try{
                Connect();
                pst = conn.prepareStatement("DELETE FROM smjestaj WHERE idSmjestaj=?");
                pst.setString(1, pickedID);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Rezervacija uspjesno izbrisana");
                    alert.setContentText("");
                    alert.showAndWait();
                    pickedID = "";
                    studentLabel.setText("");
                    roomLabel.setText("");
                    tfEditPrice.setText("");
                    String queryString = checkFilters();
                    tableReservationEdit(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fail");
                    alert.setHeaderText("Rezervacija se nije mogla obrisati");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void
    actionFilter(ActionEvent event) {
        filterName = tfFilterName.getText();
        filterSurname = tfFilterSurname.getText();
        filterBuilding = tfFilterBuilding.getText();
        filterRoomNumber = tfFilterRoomNumber.getText();
        filterPrice1 = tfFilterPrice1.getText();
        filterPrice2 = tfFilterPrice2.getText();
        String queryString = checkFilters();
        tableReservationEdit(queryString);
    }

    @FXML
    void actionFilterClear(ActionEvent event) {
        tfFilterName.setText("");
        tfFilterSurname.setText("");
        tfFilterBuilding.setText("");
        tfFilterRoomNumber.setText("");
        tfFilterPrice1.setText("");
        tfFilterPrice2.setText("");
        filterName = "";
        filterSurname = "";
        filterBuilding = "";
        filterRoomNumber = "";
        filterPrice1 = "";
        filterPrice2 = "";
    }

    @FXML
    void actionEditPrice(ActionEvent event) {
        Connect();
        if(!tfEditPrice.getText().isEmpty()){
            try{
                pst = conn.prepareStatement("UPDATE smjestaj SET cijena=? WHERE idsmjestaj=?");
                pst.setString(1, tfEditPrice.getText());
                pst.setString(2, pickedID);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Cijena uspjesno azurirana");
                    alert.setContentText("");
                    alert.showAndWait();
                    pickedID = "";
                    roomLabel.setText("");
                    studentLabel.setText("");
                    tfEditPrice.setText("");
                    String queryString = checkFilters();
                    tableReservationEdit(queryString);
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void actionToResCreate(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("ReservationView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Kreiranje rezervacija");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToResEdit(ActionEvent event) {

    }

    @FXML
    void actionToRooms(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("RoomView.fxml"));
        Stage stage = (Stage)buttonToRooms.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Sobe");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToStudent(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("StudentView.fxml"));
        Stage stage = (Stage)buttonToStudent.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Studenti");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToUsers(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("UsersView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Korisnici");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("LoginView.fxml"));
        Stage stage = (Stage)buttonToUsers.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        stage.setTitle("Studentski Dom - Prijava");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void getRowInfo(MouseEvent event) {
        RecordReservation record = new RecordReservation();
        if(tableViewReservation.getSelectionModel().getSelectedIndex() != -1){
            record = tableViewReservation.getItems().get(tableViewReservation.getSelectionModel().getSelectedIndex());
            tfEditPrice.setText(record.getPrice());
            studentLabel.setText(record.getFirstName() + " " + record.getSurname());
            roomLabel.setText(record.getBuilding() + " - " + record.getRoomNumber());
            pickedID = record.getIdSmjestaj();
        }
    }

    String checkFilters(){
        String queryString = "SELECT smjestaj.idSmjestaj, studenti.ime, studenti.prezime, sobe.Zgrada, sobe.BrojSobe, smjestaj.Cijena FROM smjestaj, studenti, sobe WHERE smjestaj.fkStudent=studenti.ID AND smjestaj.fkSoba=sobe.idsobe ";
        String temp;
        if (filterName.isEmpty() && filterSurname.isEmpty() && filterBuilding.isEmpty() && filterRoomNumber.isEmpty() && filterPrice1.isEmpty() && filterPrice2.isEmpty()){
            return queryString;
        }
        else{
            if(!filterName.isEmpty()){
                temp = " AND Ime IN('"+filterName+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
            }
            if(!filterSurname.isEmpty()){
                temp = " AND Prezime IN('"+filterSurname+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
            }
            if(!filterBuilding.isEmpty()){
                temp = " AND Zgrada IN('"+filterBuilding+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
            }
            if(!filterRoomNumber.isEmpty()){
                temp = " AND BrojSobe IN('"+filterRoomNumber+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
            }
            if(!filterPrice1.isEmpty() || !filterPrice2.isEmpty()){
                if(filterPrice1.isEmpty()){
                    queryString = queryString.concat(" AND Cijena<="+filterPrice2);
                }
                else if(filterPrice2.isEmpty()){
                    queryString = queryString.concat(" AND Cijena>="+filterPrice1);
                }
                else{
                    queryString = queryString.concat(" AND Cijena BETWEEN "+filterPrice1+" AND "+filterPrice2);
                }
            }
        }
        //System.out.println(queryString);
        return queryString;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
        String queryString = checkFilters();
        tableReservationEdit(queryString);
        LoggedInRights newLogin = new LoggedInRights();
        if(!newLogin.getCanEditUsers()){
            buttonToUsers.setDisable(true);
        }
        if(!newLogin.getCanEditStudents()){
            buttonToStudent.setDisable(true);
        }
        if(!newLogin.getCanEditRooms()){
            buttonToRooms.setDisable(true);
        }
        if(!newLogin.getCanEditReservations()){
            buttonToResCreate.setDisable(true);
            buttonToResEdit.setDisable(true);
        }
    }
}
