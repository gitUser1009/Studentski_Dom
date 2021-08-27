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

public class RoomsController implements Initializable {

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonClear;

    @FXML
    private TextField tfBulding;

    @FXML
    private TextField tfRoomNumber;

    @FXML
    private TextField tfBedNumber;

    @FXML
    private RadioButton radioAcYes;

    @FXML
    private ToggleGroup radioAc;

    @FXML
    private RadioButton radioAcNo;

    @FXML
    private RadioButton radioBalconyYes;

    @FXML
    private ToggleGroup radioBalcony;

    @FXML
    private RadioButton radioBalconyNo;

    @FXML
    private RadioButton radioFridgeYes;

    @FXML
    private ToggleGroup radioFridge;

    @FXML
    private RadioButton radioFridgeNo;

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
    private TextField tfFilterBuilding;

    @FXML
    private TextField tfFilterRoomNumber;

    @FXML
    private TextField tfFilterBedNumber;

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

    PreparedStatement pst;
    Connection conn;
    String filterBuilding = "";
    String filterRoomNumber = "";
    String filterBedNumber = "";
    String filterAc = "('DA','NE')";
    String filterBalcony = "('DA','NE')";
    String filterFridge = "('DA','NE')";
    String pickedID = "";

    public void Connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/stdom", "root", "1234");
        }
        catch (SQLException ex){
            System.out.println("SQL Error: " + ex);
        }
    }

    @FXML
    void actionAdd(ActionEvent event) {
        Connect();
        String building = tfBulding.getText();
        String roomNumber = tfRoomNumber.getText();
        String bedNumber = tfBedNumber.getText();
        String ac;
        String balcony;
        String fridge;
        if(radioAcYes.isSelected()){
            ac="DA";
        }
        else{
            ac="NE";
        }
        if(radioBalconyYes.isSelected()){
            balcony="DA";
        }
        else{
            balcony="NE";
        }
        if(radioFridgeYes.isSelected()){
            fridge="DA";
        }
        else{
            fridge="NE";
        }
        if(building.isEmpty() || roomNumber.isEmpty() || bedNumber.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Sva polja moraju biti popunjena");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("INSERT INTO sobe(Zgrada,BrojSobe,BrojKreveta,Klima,Balkon,Frizider)VALUES(?,?,?,?,?,?)");
                pst.setString(1, building);
                pst.setString(2, roomNumber);
                pst.setString(3, bedNumber);
                pst.setString(4, ac);
                pst.setString(5, balcony);
                pst.setString(6, fridge);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Soba uspjesno dodana u bazu podataka");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfBulding.setText("");
                    tfRoomNumber.setText("");
                    tfBedNumber.setText("");
                    pickedID = "";
                    String queryString = checkFilters();
                    table(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska pri dodavanju");
                    alert.setHeaderText("");
                    alert.setContentText("Soba nije uspjesno dodana u bazu podataka");
                    alert.showAndWait();
                }
            }catch(SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    @FXML
    void actionClear(ActionEvent event) {
        tfBulding.setText("");
        tfRoomNumber.setText("");
        tfBedNumber.setText("");
        radioAcYes.setSelected(true);
        radioBalconyYes.setSelected(true);
        radioFridgeYes.setSelected(true);
        pickedID="";
    }

    @FXML
    void actionDelete(ActionEvent event) throws SQLException {
        Connect();
        pst = conn.prepareStatement("SELECT COUNT(*) AS numOfStudents FROM smjestaj WHERE fkSoba IN (?)");
        pst.setString(1, pickedID);
        ResultSet rs = pst.executeQuery();
        String studentNumber = "";
        while(rs.next()){
            studentNumber = rs.getString("numOfStudents");
        }
        if(!studentNumber.equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Svi studenti moraju biti odjavljeni iz sobe prije brisanja iz baze podataka");
            alert.setContentText("");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("DELETE FROM sobe WHERE idsobe=?");
                pst.setString(1, pickedID);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Soba uspjesno izbrisana iz baze podataka");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfBulding.setText("");
                    tfRoomNumber.setText("");
                    tfBedNumber.setText("");
                    pickedID="";
                    String queryString = checkFilters();
                    table(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fail");
                    alert.setHeaderText("Nije odabrana soba koja ce se izbrisati");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void actionEdit(ActionEvent event) {
        Connect();
        String building = tfBulding.getText();
        String roomNumber = tfRoomNumber.getText();
        String bedNumber = tfBedNumber.getText();
        String ac;
        String balcony;
        String fridge;
        if(radioAcYes.isSelected()){
            ac="DA";
        }
        else{
            ac="NE";
        }
        if(radioBalconyYes.isSelected()){
            balcony="DA";
        }
        else{
            balcony="NE";
        }
        if(radioFridgeYes.isSelected()){
            fridge="DA";
        }
        else{
            fridge="NE";
        }
        try{
            pst=conn.prepareStatement("UPDATE sobe SET Zgrada=?, BrojSobe=?, BrojKreveta=?, Klima=?, Balkon=?, Frizider=? WHERE idsobe=?");
            pst.setString(1, building);
            pst.setString(2, roomNumber);
            pst.setString(3, bedNumber);
            pst.setString(4, ac);
            pst.setString(5, balcony);
            pst.setString(6, fridge);
            pst.setString(7, pickedID);
            int status = pst.executeUpdate();
            if(status == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspjeh");
                alert.setHeaderText("Podaci uspjesno azurirani");
                alert.setContentText("");
                alert.showAndWait();
                tfBulding.setText("");
                tfRoomNumber.setText("");
                tfBedNumber.setText("");
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
    void getRowInfo(MouseEvent event) {
        RecordRoom record = new RecordRoom();
        if(tableViewRoom.getSelectionModel().getSelectedIndex() != -1){
            record = tableViewRoom.getItems().get(tableViewRoom.getSelectionModel().getSelectedIndex());
            tfBulding.setText(record.getBuilding());
            tfRoomNumber.setText(record.getRoomNumber());
            tfBedNumber.setText(record.getNumberOfBeds());
            if("DA".equals(record.getAc())){
                radioAcYes.setSelected(true);
            }
            else{
                radioAcNo.setSelected(true);
            }
            if("DA".equals(record.getBalcony())){
                radioBalconyYes.setSelected(true);
            }
            else{
                radioBalconyNo.setSelected(true);
            }
            if("DA".equals(record.getFridge())){
                radioFridgeYes.setSelected(true);
            }
            else{
                radioFridgeNo.setSelected(true);
            }
            pickedID=record.getIdRoom();
        }
    }

    public void table(String queryString){
        ObservableList<RecordRoom> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryString);
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
    void actionFilter(ActionEvent event) {
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
        String queryString = checkFilters();
        table(queryString);
    }

    String checkFilters(){
        String queryString = "SELECT * FROM sobe WHERE ";
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

        //System.out.println(queryString);
        return queryString;
    }

    @FXML
    void actionFilterClear(ActionEvent event) {
        String filterBuilding = "";
        String filterRoomNumber = "";
        String filterBedNumber = "";
        String filterAc = "('DA','NE')";
        String filterBalcony = "('DA','NE')";
        String filterFridge = "('DA','NE')";
        String pickedID = "";
        tfFilterBuilding.setText("");
        tfFilterRoomNumber.setText("");
        tfFilterBedNumber.setText("");
        radioFilterAcAll.setSelected(true);
        radioFilterBalconyAll.setSelected(true);
        radioFilterFridgeAll.setSelected(true);
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
    void actionToRooms(ActionEvent event) {

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
