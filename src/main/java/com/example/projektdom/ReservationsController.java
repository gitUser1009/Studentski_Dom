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

public class ReservationsController implements Initializable {

    String pickedIDstudent="";
    String filterName="";
    String filterSurname="";
    String filterCollege="";
    String filterYear="";
    String filterCounty="";
    String filterCity="";
    String filterPoints1="";
    String filterPoints2="";

    String filterBuilding = "";
    String filterRoomNumber = "";
    String filterBedNumber = "";
    String filterAc = "('DA','NE')";
    String filterBalcony = "('DA','NE')";
    String filterFridge = "('DA','NE')";
    String pickedIDroom = "";
    Integer price = 0;

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
    private Button buttonStudentFilter;

    @FXML
    private Button buttonFilterClear;

    @FXML
    private TextField tfFilterBuilding;

    @FXML
    private TextField tfFilterRoomNumber;

    @FXML
    private TextField tfFilterBedNumber;

    @FXML
    private TextField tfFilterFreeBedNumber;

    @FXML
    private RadioButton radioFilterAcAll;

    @FXML
    private ToggleGroup radioFilterAc;

    @FXML
    private RadioButton radioFilterAcYes;

    @FXML
    private RadioButton radioFilterAcNo;

    @FXML
    private RadioButton radioFilterBalconyAll;

    @FXML
    private ToggleGroup radioFilterBalcony;

    @FXML
    private RadioButton radioFilterBalconyYes;

    @FXML
    private RadioButton radioFilterBalconyNo;

    @FXML
    private RadioButton radioFilterFridgeAll;

    @FXML
    private ToggleGroup radioFilterFridge;

    @FXML
    private RadioButton radioFilterFridgeYes;

    @FXML
    private RadioButton radioFilterFridgeNo;

    @FXML
    private Button buttonRoomFilter;

    @FXML
    private Button buttonRoomFilterClear;

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
    private TableView<RecordRoom> tableViewRoom;

    @FXML
    private TableColumn<RecordRoom, String> colIDroom;

    @FXML
    private TableColumn<RecordRoom, String> colBuilding;

    @FXML
    private TableColumn<RecordRoom, String> colRoomNumber;

    @FXML
    private TableColumn<RecordRoom, String> colBedNumber;

    @FXML
    private TableColumn<RecordRoom, String> colFreeBeed;

    @FXML
    private TableColumn<RecordRoom, String> colAc;

    @FXML
    private TableColumn<RecordRoom, String> colBalcony;

    @FXML
    private TableColumn<RecordRoom, String> colFridge;

    @FXML
    private Button buttonCreateReservation;

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
    private CheckBox checkbocAutoPrice;

    @FXML
    private TextField tfPrice;

    @FXML
    private Label labelInRoom;

    @FXML
    private Button buttonToUsers;

    @FXML
    private Button buttonLogout;

    @FXML
    void actionCreateReservation(ActionEvent event) {
        if(pickedIDstudent.isEmpty() || pickedIDroom.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Student i soba moraju biti izabrani klikom na tablicu");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("INSERT INTO smjestaj(fkStudent,fkSoba,Cijena)VALUES(?,?,?)");
                pst.setString(1, pickedIDstudent);
                pst.setString(2, pickedIDroom);
                pst.setString(3, tfPrice.getText());
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Smjestaj uspjesan");
                    alert.setContentText("Student :"+studentLabel.getText()+"     Soba:"+roomLabel.getText());
                    alert.showAndWait();
                    String queryStringStudents = checkFiltersStudents();
                    tableStudents(queryStringStudents);
                    String queryStringRooms = checkFiltersRooms();
                    tableRooms(queryStringRooms);
                    pickedIDroom = "";
                    pickedIDstudent = "";
                    price = 0;
                    tfPrice.setText("");
                    studentLabel.setText("");
                    roomLabel.setText("");
                    labelInRoom.setText("");
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("");
                    alert.setContentText("Smjestaj nije uspjesno realizovan");
                    alert.showAndWait();
                }
            }catch(SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void actionRoomFilter(ActionEvent event) {
        filterBuilding = tfFilterBuilding.getText();
        filterRoomNumber = tfFilterRoomNumber.getText();
        filterBedNumber = tfFilterBedNumber.getText();
        if(radioFilterAcAll.isSelected()){
            filterAc = "('DA','NE')";
        }
        else if(radioFilterAcYes.isSelected()){
            filterAc = "('DA')";
        }
        else{
            filterAc="('NE')";
        }
        if(radioFilterBalconyAll.isSelected()){
            filterBalcony="('DA','NE')";
        }
        else if(radioFilterBalconyYes.isSelected()){
            filterBalcony="('DA')";
        }
        else{
            filterBalcony="('NE')";
        }
        if(radioFilterFridgeAll.isSelected()){
            filterFridge="('DA','NE')";
        }
        else if(radioFilterFridgeYes.isSelected()){
            filterFridge="('DA')";
        }
        else{
            filterFridge="('NE')";
        }
        String queryString = checkFiltersRooms();
        tableRooms(queryString);
    }

    @FXML
    void actionRoomFilterClear(ActionEvent event) {
        String filterBuilding = "";
        String filterRoomNumber = "";
        String filterBedNumber = "";
        String filterAc = "('DA','NE')";
        String filterBalcony = "('DA','NE')";
        String filterFridge = "('DA','NE')";
        String pickedIDroom = "";
        Integer price = 0;
        tfFilterBuilding.setText("");
        tfFilterRoomNumber.setText("");
        tfFilterBedNumber.setText("");
        tfFilterFreeBedNumber.setText("");
        radioFilterAcAll.setSelected(true);
        radioFilterBalconyAll.setSelected(true);
        radioFilterFridgeAll.setSelected(true);
    }

    @FXML
    void actionStudentFilter(ActionEvent event) {
        filterName = tfFilterName.getText();
        filterSurname = tfFilterSurname.getText();
        filterCollege = tfFilterCollege.getText();
        filterYear = tfFilterYear.getText();
        filterCounty = tfFilterCounty.getText();
        filterCity = tfFilterCity.getText();
        filterPoints1 = tfFilterPoints1.getText();
        filterPoints2 = tfFilterPoints2.getText();
        String queryStringStudent = checkFiltersStudents();
        tableStudents(queryStringStudent);
    }

    @FXML
    void actionStudentFilterClear(ActionEvent event) {
        String pickedIDstudent="";
        String filterName="";
        String filterSurname="";
        String filterCollege="";
        String filterYear="";
        String filterCounty="";
        String filterCity="";
        String filterPoints1="";
        String filterPoints2="";
        tfFilterName.setText("");
        tfFilterSurname.setText("");
        tfFilterCollege.setText("");
        tfFilterYear.setText("");
        tfFilterCounty.setText("");
        tfFilterCity.setText("");
        tfFilterPoints1.setText("");
        tfFilterPoints2.setText("");
    }

    @FXML
    void actionToResCreate(ActionEvent event) {

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
    void getRowInfoStudent(MouseEvent event) {
        RecordStudent record = new RecordStudent();
        if(tableViewStudent.getSelectionModel().getSelectedIndex() != -1){
            record = tableViewStudent.getItems().get(tableViewStudent.getSelectionModel().getSelectedIndex());
            String studentName = record.getFirstName() + " ";
            studentName = studentName.concat(record.getSurname());
            studentLabel.setText(studentName);
            pickedIDstudent = record.getId();
        }
    }

    @FXML
    void getRowInfoRoom(MouseEvent event) {
        RecordRoom record = new RecordRoom();
        if(tableViewRoom.getSelectionModel().getSelectedIndex() != -1){
            record = tableViewRoom.getItems().get(tableViewRoom.getSelectionModel().getSelectedIndex());
            String roomInfo = record.getBuilding() + " - ";
            roomInfo = roomInfo.concat(record.getRoomNumber());
            roomLabel.setText(roomInfo);
            pickedIDroom = record.getIdRoom();
            price = 159;
            if(record.getAc().equals("DA")){
                price += 5;
            }
            if(record.getFridge().equals("DA")){
                price += 3;
            }
            tfPrice.setText(String.valueOf(price));
            try{
                pst = conn.prepareStatement("SELECT ime, prezime FROM stdom.smjestaj JOIN studenti ON smjestaj.fkStudent = studenti.ID WHERE fkSoba=?");
                pst.setString(1, pickedIDroom);
                ResultSet rs = pst.executeQuery();
                String inRoomString="";
                while(rs.next()){
                    RecordStudent labelRecord = new RecordStudent();
                    labelRecord.setFirstName(rs.getString("Ime"));
                    labelRecord.setSurname(rs.getString("Prezime"));
                    inRoomString = inRoomString.concat(labelRecord.getFirstName()+" "+labelRecord.getSurname()+"\n");
                }
                labelInRoom.setText(inRoomString);
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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

    String checkFiltersStudents(){
        if (filterName.isEmpty() && filterSurname.isEmpty() && filterCollege.isEmpty() && filterYear.isEmpty() && filterCounty.isEmpty() && filterCity.isEmpty() && filterPoints1.isEmpty() && filterPoints2.isEmpty()){
            //System.out.println("SELECT * FROM studenti WHERE id NOT IN (SELECT fkStudent FROM smjestaj)");
            return "SELECT * FROM studenti WHERE id NOT IN (SELECT fkStudent FROM smjestaj)";
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

            queryString = queryString.concat(" AND id NOT IN (SELECT fkStudent FROM smjestaj)");
            //System.out.println(queryString);
            return queryString;
        }
    }

    public void tableStudents(String queryStringStudents){
        ObservableList<RecordStudent> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryStringStudents);
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

    String checkFiltersRooms(){
        String queryString = "SELECT sobe.*, count(smjestaj.fksoba) AS brojSmjestaja\n" +
                "FROM sobe\n" +
                "LEFT JOIN smjestaj\n" +
                "ON (sobe.idsobe = smjestaj.fksoba) WHERE ";
        boolean isFirstFilter = true;
        String temp;
        if(! filterBuilding.isEmpty()){
            temp = "Zgrada IN('"+filterBuilding+"')";
            temp = temp.replace(",", "','");
            queryString = queryString.concat(temp);
            isFirstFilter = false;
        }
        if(! filterRoomNumber.isEmpty()){
            if(! isFirstFilter){
                queryString = queryString.concat(" AND ");
            }
            temp = "BrojSobe IN ('"+filterRoomNumber+"')";
            temp = temp.replace(",", "','");
            queryString = queryString.concat(temp);
            isFirstFilter = false;
        }
        if(! filterBedNumber.isEmpty()){
            if(! isFirstFilter){
                queryString = queryString.concat(" AND ");
            }
            temp = "BrojKreveta IN ('"+filterBedNumber+"')";
            temp = temp.replace(",", "','");
            queryString = queryString.concat(temp);
            isFirstFilter = false;
        }
        if(! isFirstFilter){
            queryString = queryString.concat(" AND ");
        }
        queryString = queryString.concat("Klima IN"+filterAc);
        queryString = queryString.concat(" AND ");
        queryString = queryString.concat("Balkon IN"+filterBalcony);
        queryString = queryString.concat(" AND ");
        queryString = queryString.concat("Frizider IN"+filterFridge);
        queryString = queryString.concat("\nGROUP BY sobe.idsobe\n" +
                "HAVING COUNT(smjestaj.fksoba)<BrojKreveta");

        //System.out.println(queryString);
        return queryString;
    }

    public void tableRooms(String queryStringRooms){
        ObservableList<RecordRoom> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryStringRooms);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    RecordRoom record = new RecordRoom();
                    record.setIdRoom(rs.getString("idsobe"));
                    record.setBuilding(rs.getString("Zgrada"));
                    record.setRoomNumber(rs.getString("BrojSobe"));
                    record.setNumberOfBeds(rs.getString("BrojKreveta"));
                    record.setAc(rs.getString("Klima"));
                    record.setBalcony(rs.getString("Balkon"));
                    record.setFridge(rs.getString("Frizider"));
                    records.add(record);
                }
            }
            tableViewRoom.setItems(records);
            colIDroom.setCellValueFactory(f -> f.getValue().idRoomProperty());
            colBuilding.setCellValueFactory(f -> f.getValue().buildingProperty());
            colRoomNumber.setCellValueFactory(f -> f.getValue().roomNumberProperty());
            colBedNumber.setCellValueFactory(f -> f.getValue().numberOfBedsProperty());
            colFreeBeed.setCellValueFactory(f -> f.getValue().numberOfFreeBedsProperty());
            colAc.setCellValueFactory(f -> f.getValue().acProperty());
            colBalcony.setCellValueFactory(f -> f.getValue().balconyProperty());
            colFridge.setCellValueFactory(f -> f.getValue().fridgeProperty());
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    void actionAutoPrice(ActionEvent event) {
        if(checkbocAutoPrice.isSelected()){
            tfPrice.setDisable(true);
            tfPrice.setText(String.valueOf(price));
        }
        else if(!checkbocAutoPrice.isSelected()){
            tfPrice.setDisable(false);
        }
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
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
        String queryStringStudents = checkFiltersStudents();
        tableStudents(queryStringStudents);
        String queryStringRooms = checkFiltersRooms();
        tableRooms(queryStringRooms);
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
