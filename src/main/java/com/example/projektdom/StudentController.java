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

public class StudentController implements Initializable {

    @FXML
    private TextField tempTestID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfSurname;

    @FXML
    private TextField tfCollege;

    @FXML
    private TextField tfYear;

    @FXML
    private TextField tfCounty;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfPoints;

    @FXML
    private TableView<RecordStudent> tableViewStudent;

    @FXML
    private TableColumn<RecordStudent, String> colID;

    @FXML
    private TableColumn<RecordStudent, String> colName;

    @FXML
    private TableColumn<RecordStudent, String> colSurname;

    @FXML
    private TableColumn<RecordStudent, String> colCollege;

    @FXML
    private TableColumn<RecordStudent, String> colYear;

    @FXML
    private TableColumn<RecordStudent, String> colCounty;

    @FXML
    private TableColumn<RecordStudent, String> colCity;

    @FXML
    private TableColumn<RecordStudent, String> colPoints;

    @FXML
    private TextField tfFilterName;

    @FXML
    private TextField tfFilterSurname;

    @FXML
    private TextField tfFilterCollege;

    @FXML
    private TextField tfFilterYear;

    @FXML
    private TextField tfFilterCounty;

    @FXML
    private TextField tfFilterCity;

    @FXML
    private TextField tfFilterPoints1;

    @FXML
    private TextField tfFilterPoints2;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonFilterClear;

    @FXML
    private Button buttonToStudent;

    @FXML
    private Button buttonToRooms;

    @FXML
    private Button buttonToResCreate;

    @FXML
    private Button buttonToResEdit;

    @FXML
    private Button buttonToUsers;

    @FXML
    private Button buttonLogout;

    Connection conn;
    PreparedStatement pst;
    String pickedID;
    String filterName="";
    String filterSurname="";
    String filterCollege="";
    String filterYear="";
    String filterCounty="";
    String filterCity="";
    String filterPoints1="";
    String filterPoints2="";

    public void Connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/stdom", "root", "1234");
        }
        catch (SQLException ex){
            System.out.println("SQL Error: " + ex);
        }
    }

    @FXML
    void actionAdd (ActionEvent event){
        Connect();
        String firstName = tfName.getText();
        String surname = tfSurname.getText();
        String college = tfCollege.getText();
        String year = tfYear.getText();
        String county = tfCounty.getText();
        String city = tfCity.getText();
        String points = tfPoints.getText();
        if(firstName.isEmpty() || surname.isEmpty() || college.isEmpty() || year.isEmpty() || county.isEmpty() || city.isEmpty() || points.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Sva polja moraju biti popunjena");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("insert into studenti(Ime,Prezime,Fakultet,Godina,Zupanija,Opcina,Bodovi)values(?,?,?,?,?,?,?)");
                pst.setString(1, firstName);
                pst.setString(2, surname);
                pst.setString(3, college);
                pst.setString(4, year);
                pst.setString(5, county);
                pst.setString(6, city);
                pst.setString(7, points);
                int status = pst.executeUpdate();
                if(status==1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Student uspjesno dodan u bazu podataka");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfName.setText("");
                    tfSurname.setText("");
                    tfCollege.setText("");
                    tfYear.setText("");
                    tfCounty.setText("");
                    tfCity.setText("");
                    tfPoints.setText("");
                    pickedID="";
                    String queryString = checkFilters();
                    table(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska pri dodavanju");
                    alert.setHeaderText("");
                    alert.setContentText("Student nije uspjesno dodan u bazu podataka");
                    alert.showAndWait();
                }
            }
            catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void table(String queryString){
        ObservableList<RecordStudent> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryString);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    RecordStudent record = new RecordStudent();
                    record.setId(rs.getString("id"));
                    record.setFirstName(rs.getString("Ime"));
                    record.setSurname(rs.getString("Prezime"));
                    record.setCollege(rs.getString("Fakultet"));
                    record.setYear(rs.getString("Godina"));
                    record.setCounty(rs.getString("Zupanija"));
                    record.setCity(rs.getString("Opcina"));
                    record.setPoints(rs.getString("Bodovi"));
                    records.add(record);
                }
            }
            tableViewStudent.setItems(records);
            colID.setCellValueFactory(f -> f.getValue().idProperty());
            colName.setCellValueFactory(f -> f.getValue().firstNameProperty());
            colSurname.setCellValueFactory(f -> f.getValue().surnameProperty());
            colCollege.setCellValueFactory(f -> f.getValue().collegeProperty());
            colYear.setCellValueFactory(f -> f.getValue().yearProperty());
            colCounty.setCellValueFactory(f -> f.getValue().countyProperty());
            colCity.setCellValueFactory(f -> f.getValue().cityProperty());
            colPoints.setCellValueFactory(f -> f.getValue().pointsProperty());
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void actionDelete(ActionEvent event) throws SQLException {
        Connect();
        pst = conn.prepareStatement("SELECT COUNT(*) AS inRoom FROM smjestaj WHERE fkStudent IN (?)");
        pst.setString(1, pickedID);
        ResultSet rs = pst.executeQuery();
        String isInRoom = "";
        while(rs.next()){
            isInRoom = rs.getString("inRoom");
        }
        if(!isInRoom.equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Student mora biti odjavljen iz sobe prije brisanja iz baze podataka");
            alert.setContentText("");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("delete from studenti where id=?");
                pst.setString(1, pickedID);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Student uspjesno izbrisan iz baze podataka");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfName.setText("");
                    tfSurname.setText("");
                    tfCollege.setText("");
                    tfYear.setText("");
                    tfCounty.setText("");
                    tfCity.setText("");
                    tfPoints.setText("");
                    pickedID="";
                    String queryString = checkFilters();
                    table(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fail");
                    alert.setHeaderText("Nije odabran student koji ce se izbrisati");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }catch(SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void actionEdit(ActionEvent event) {
        Connect();
        String firstName = tfName.getText();
        String surname = tfSurname.getText();
        String college = tfCollege.getText();
        String year = tfYear.getText();
        String county = tfCounty.getText();
        String city = tfCity.getText();
        String points = tfPoints.getText();
        try{
            pst = conn.prepareStatement("update studenti set ime=?, prezime=?, Fakultet=?, Godina=?, Zupanija=?, Opcina=?, Bodovi=? where id=?");
            pst.setString(1, firstName);
            pst.setString(2, surname);
            pst.setString(3, college);
            pst.setString(4, year);
            pst.setString(5, county);
            pst.setString(6, city);
            pst.setString(7, points);
            pst.setString(8, pickedID);
            int status = pst.executeUpdate();
            if(status == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspjeh");
                alert.setHeaderText("Podaci uspjesno azurirani");
                alert.setContentText("");
                alert.showAndWait();
                tfName.setText("");
                tfSurname.setText("");
                tfCollege.setText("");
                tfYear.setText("");
                tfCounty.setText("");
                tfCity.setText("");
                tfPoints.setText("");
                pickedID="";
                String queryString = checkFilters();
                table(queryString);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Podaci se nisu azurirali");
                alert.setContentText("");
                alert.showAndWait();
            }
        }catch(SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void actionClear(ActionEvent event) {
        tfName.setText("");
        tfSurname.setText("");
        tfCollege.setText("");
        tfYear.setText("");
        tfCounty.setText("");
        tfCity.setText("");
        tfPoints.setText("");
        pickedID="";
    }

    @FXML
    void getRowInfo(MouseEvent event) {
        RecordStudent record = new RecordStudent();
        if(tableViewStudent.getSelectionModel().getSelectedIndex() != -1){
            System.out.println("Izabire se");
            record = tableViewStudent.getItems().get(tableViewStudent.getSelectionModel().getSelectedIndex());
            tfName.setText(record.getFirstName());
            tfSurname.setText(record.getSurname());
            tfCollege.setText(record.getCollege());
            tfYear.setText(record.getYear());
            tfCounty.setText(record.getCounty());
            tfCity.setText(record.getCity());
            tfPoints.setText(record.getPoints());
            pickedID=record.getId();
        }
    }

    @FXML
    void actionFilter(ActionEvent event) {
        filterName = tfFilterName.getText();
        filterSurname = tfFilterSurname.getText();
        filterCollege = tfFilterCollege.getText();
        filterYear = tfFilterYear.getText();
        filterCounty = tfFilterCounty.getText();
        filterCity = tfFilterCity.getText();
        filterPoints1 = tfFilterPoints1.getText();
        filterPoints2 = tfFilterPoints2.getText();
        String queryString = checkFilters();
        table(queryString);
    }

    String checkFilters(){
        if (filterName.isEmpty() && filterSurname.isEmpty() && filterCollege.isEmpty() && filterYear.isEmpty() && filterCounty.isEmpty() && filterCity.isEmpty() && filterPoints1.isEmpty() && filterPoints2.isEmpty()){
            //System.out.println("SELECT * FROM studenti");
            return "SELECT * FROM studenti";
        }
        else{
            boolean isFirstFilter = true;
            String queryString = "SELECT * FROM studenti WHERE ";
            String temp;
            if(! filterName.isEmpty()){
                temp = "Ime IN('"+filterName+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterSurname.isEmpty()){
                if(! isFirstFilter){
                    queryString = queryString.concat(" AND ");
                }
                temp = "Prezime IN('"+filterSurname+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterCollege.isEmpty()){
                if(! isFirstFilter){
                    queryString = queryString.concat(" AND ");
                }
                temp = "Fakultet IN('"+filterCollege+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterYear.isEmpty()){
                if(! isFirstFilter){
                    queryString = queryString.concat(" AND ");
                }
                temp = "Godina IN('"+filterYear+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterCounty.isEmpty()){
                if(! isFirstFilter){
                    queryString = queryString.concat(" AND ");
                }
                temp = "Zupanija IN('"+filterCounty+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterCity.isEmpty()){
                if(! isFirstFilter){
                    queryString = queryString.concat(" AND ");
                }
                temp = "Opcina IN('"+filterCity+"')";
                temp = temp.replace(",", "','");
                queryString = queryString.concat(temp);
                isFirstFilter = false;
            }
            if(! filterPoints1.isEmpty() || ! filterPoints2.isEmpty()) {
                if (!isFirstFilter) {
                    queryString = queryString.concat(" AND ");
                }
                if (filterPoints1.isEmpty()){
                    queryString = queryString.concat("Bodovi <= "+filterPoints2);
                }
                else if(filterPoints2.isEmpty()){
                    queryString = queryString.concat("Bodovi >= "+filterPoints1);
                }
                else{
                    queryString = queryString.concat("Bodovi BETWEEN "+filterPoints1+" AND "+filterPoints2);
                }
            }
            //System.out.println(queryString);
            return queryString;
        }
    }

    @FXML
    void actionFilterClear(ActionEvent event) {
        tfFilterName.setText("");
        tfFilterSurname.setText("");
        tfFilterCollege.setText("");
        tfFilterYear.setText("");
        tfFilterCounty.setText("");
        tfFilterCity.setText("");
        tfFilterPoints1.setText("");
        tfFilterPoints2.setText("");
        filterName = "";
        filterSurname = "";
        filterCollege = "";
        filterYear = "";
        filterCounty = "";
        filterCity = "";
        filterPoints1 = "";
        filterPoints2 = "";
    }

    @FXML
    void actionToStudent(ActionEvent event) throws IOException{

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
    void actionToResCreate(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("ReservationView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Kreiranje rezervacija");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToResEdit(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("ReservationEditView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Uređivanje rezervacija");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void actionToUsers(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("UsersView.fxml"));
        Stage stage = (Stage)buttonToUsers.getScene().getWindow();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
        String queryString = checkFilters();
        table(queryString);
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