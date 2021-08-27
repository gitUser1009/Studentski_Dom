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

public class UsersController implements Initializable {
    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonClear;

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfPassword;

    @FXML
    private RadioButton radioUsernameYes;

    @FXML
    private ToggleGroup radioUsername;

    @FXML
    private RadioButton radioUsernameNo;

    @FXML
    private RadioButton radioStudentsYes;

    @FXML
    private ToggleGroup radioStudents;

    @FXML
    private RadioButton radioStudentsNo;

    @FXML
    private RadioButton radioRoomsYes;

    @FXML
    private ToggleGroup radioRooms;

    @FXML
    private RadioButton radioRoomsNo;

    @FXML
    private RadioButton radioReservationYes;

    @FXML
    private ToggleGroup radioReservation;

    @FXML
    private RadioButton radioReservationNo;

    @FXML
    private TextField tfFilterUsername;

    @FXML
    private TextField tfFilterPassword;

    @FXML
    private RadioButton radioFilterUsernameAll;

    @FXML
    private ToggleGroup radioFilterUsername;

    @FXML
    private RadioButton radioFilterUsernameYes;

    @FXML
    private RadioButton radioFilterUsernameNo;

    @FXML
    private RadioButton radioFilterStudentsAll;

    @FXML
    private ToggleGroup radioFilterStudents;

    @FXML
    private RadioButton radioFilterStudentsYes;

    @FXML
    private RadioButton radioFilterStudentsNo;

    @FXML
    private RadioButton radioFilterRoomsAll;

    @FXML
    private ToggleGroup radioFilterRooms;

    @FXML
    private RadioButton radioFilterRoomsYes;

    @FXML
    private RadioButton radioFilterRoomsNo;

    @FXML
    private RadioButton radioFilterReservationAll;

    @FXML
    private ToggleGroup radioFilterReservation;

    @FXML
    private RadioButton radioFilterReservationYes;

    @FXML
    private RadioButton radioFilterReservationNo;

    @FXML
    private Button buttonFilter;

    @FXML
    private Button buttonFilterClear;

    @FXML
    private TableView<RecordUser> tableViewUsers;

    @FXML
    private TableColumn<RecordUser, String> colID;

    @FXML
    private TableColumn<RecordUser, String> colUsername;

    @FXML
    private TableColumn<RecordUser, String> colPassword;

    @FXML
    private TableColumn<RecordUser, String> colRightsUsers;

    @FXML
    private TableColumn<RecordUser, String> colRightsStudents;

    @FXML
    private TableColumn<RecordUser, String> colRightsRooms;

    @FXML
    private TableColumn<RecordUser, String> colRightsReservations;

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
    String pickedID = "";
    String filterUsername = "";
    String filterPassword = "";
    String filterRightsUsername = "('DA','NE')";
    String filterRightsStudents = "('DA','NE')";
    String filterRightsRooms = "('DA','NE')";
    String filterRightsReservations = "('DA','NE')";

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
        String userName = tfUsername.getText();
        String password = tfPassword.getText();
        String rightsUsername = "";
        String rightsStudents = "";
        String rightsRooms = "";
        String rightsReservations = "";
        String resNumber = "";
        if(radioUsernameYes.isSelected()){
            rightsUsername = "DA";
        }
        else{
            rightsUsername = "NE";
        }
        if(radioStudentsYes.isSelected()){
            rightsStudents = "DA";
        }
        else{
            rightsStudents = "NE";
        }
        if(radioRoomsYes.isSelected()){
            rightsRooms = "DA";
        }
        else{
            rightsRooms = "NE";
        }
        if(radioReservationYes.isSelected()){
            rightsReservations = "DA";
        }
        else{
            rightsReservations = "NE";
        }

        if(!userName.isEmpty()){
            try{
                pst = conn.prepareStatement("SELECT COUNT(*) AS resNumber FROM korisnici WHERE korisnickoIme IN (?)");
                pst.setString(1, userName);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    resNumber = rs.getString("resNumber");
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!resNumber.equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Korisnicko ime je zauzeto");
            alert.showAndWait();
        }
        else if(userName.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Sva polja moraju biti popunjena");
            alert.showAndWait();
        }
        else if(password.length()<6){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Sifra mora sadrzavati barem 6 znakova");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement("INSERT INTO korisnici(korisnickoIme,sifra,dopustenjeKorisnici,dopustenjeStudenti,dopustenjeSobe,dopustenjeSmjestaj)VALUES(?,?,?,?,?,?)");
                pst.setString(1, userName);
                pst.setString(2, password);
                pst.setString(3, rightsUsername);
                pst.setString(4, rightsStudents);
                pst.setString(5, rightsRooms);
                pst.setString(6, rightsReservations);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Korisnik uspjesno dodan u bazu podataka");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfUsername.setText("");
                    tfPassword.setText("");
                    radioUsernameYes.setSelected(true);
                    radioStudentsYes.setSelected(true);
                    radioRoomsYes.setSelected(true);
                    radioReservationYes.setSelected(true);
                    pickedID = "";
                    String queryString = checkFilters();
                    tableUsers(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("");
                    alert.setContentText("Korisnik se nije mogao dodati u bazu podataka");
                    alert.showAndWait();
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    String checkFilters(){
        String queryString = "SELECT * FROM korisnici WHERE ";
        boolean isFirstFilter = true;
        String temp;
        if(! filterUsername.isEmpty()){
            temp = "korisnickoIme IN('"+filterUsername+"')";
            temp = temp.replace(",", "','");
            queryString = queryString.concat(temp);
            isFirstFilter = false;
        }
        if(! filterPassword.isEmpty()){
            if(! isFirstFilter){
                queryString = queryString.concat(" AND ");
            }
            temp = "sifra IN ('"+filterPassword+"')";
            temp = temp.replace(",", "','");
            queryString = queryString.concat(temp);
            isFirstFilter = false;
        }
        if(! isFirstFilter){
            queryString = queryString.concat(" AND ");
        }
        queryString = queryString.concat("dopustenjeKorisnici IN"+filterRightsUsername);
        queryString = queryString.concat(" AND dopustenjeStudenti IN"+filterRightsStudents);
        queryString = queryString.concat(" AND dopustenjeSobe IN"+filterRightsRooms);
        queryString = queryString.concat(" AND dopustenjeSmjestaj IN"+filterRightsReservations);
        //System.out.println(queryString);
        return queryString;
    }

    @FXML
    void actionClear(ActionEvent event) {
        tfUsername.setText("");
        tfPassword.setText("");
        radioUsernameYes.setSelected(true);
        radioStudentsYes.setSelected(true);
        radioRoomsYes.setSelected(true);
        radioReservationYes.setSelected(true);
        pickedID = "";
        buttonEdit.setDisable(false);
        buttonDelete.setDisable(false);
    }

    @FXML
    void actionDelete(ActionEvent event) {
        try{
            Connect();
            pst = conn.prepareStatement("DELETE FROM korisnici WHERE idKorisnik=?");
            pst.setString(1, pickedID);
            int status = pst.executeUpdate();
            if(status == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspjeh");
                alert.setHeaderText("Korisnik uspjesno izbrisan iz baze podataka");
                alert.setContentText("");
                alert.showAndWait();
                tfUsername.setText("");
                tfPassword.setText("");
                radioUsernameYes.setSelected(true);
                radioStudentsYes.setSelected(true);
                radioRoomsYes.setSelected(true);
                radioReservationYes.setSelected(true);
                pickedID = "";
                String queryString = checkFilters();
                tableUsers(queryString);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Korisnik nije obrisan iz baze podataka");
                alert.setContentText("");
                alert.showAndWait();
            }
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void actionEdit(ActionEvent event) {
        if(pickedID.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Potrebno je izabrati korisnika iz tablice");
            alert.setContentText("");
            alert.showAndWait();
        }
        else{
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            String rightsUsers = "";
            String rightsStudents = "";
            String rightsRooms = "";
            String rightsReservations = "";
            if(radioUsernameYes.isSelected()){
                rightsUsers = "DA";
            }
            else{
                rightsUsers = "NE";
            }
            if(radioStudentsYes.isSelected()){
                rightsStudents = "DA";
            }
            else{
                rightsStudents = "NE";
            }
            if(radioRoomsYes.isSelected()){
                rightsRooms = "DA";
            }
            else{
                rightsRooms = "NE";
            }
            if(radioReservationYes.isSelected()){
                rightsReservations = "DA";
            }
            else{
                rightsReservations = "NE";
            }
            try{
                pst=conn.prepareStatement("UPDATE korisnici SET korisnickoIme=?, sifra=?, dopustenjeKorisnici=?, dopustenjeStudenti=?, dopustenjeSobe=?, dopustenjeSmjestaj=? WHERE idKorisnik=?");
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, rightsUsers);
                pst.setString(4, rightsStudents);
                pst.setString(5, rightsRooms);
                pst.setString(6, rightsReservations);
                pst.setString(7, pickedID);
                int status = pst.executeUpdate();
                if(status == 1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspjeh");
                    alert.setHeaderText("Podaci uspjesno azurirani");
                    alert.setContentText("");
                    alert.showAndWait();
                    tfUsername.setText("");
                    tfPassword.setText("");
                    radioUsernameYes.setSelected(true);
                    radioStudentsYes.setSelected(true);
                    radioRoomsYes.setSelected(true);
                    radioReservationYes.setSelected(true);
                    pickedID = "";
                    String queryString = checkFilters();
                    tableUsers(queryString);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("Podaci se nisu azurirali");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }catch (SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void actionFilter(ActionEvent event) {
        filterUsername = tfFilterUsername.getText();
        filterPassword = tfFilterPassword.getText();
        if(radioFilterUsernameAll.isSelected()){
            filterRightsUsername = "('DA','NE')";
        }
        else if(radioFilterUsernameYes.isSelected()){
            filterRightsUsername = "('DA')";
        }
        else{
            filterRightsUsername = "('NE')";
        }
        if(radioFilterStudentsAll.isSelected()){
            filterRightsStudents = "('DA','NE')";
        }
        else if(radioFilterStudentsYes.isSelected()){
            filterRightsStudents = "('DA')";
        }
        else{
            filterRightsStudents = "('NE')";
        }
        if(radioFilterRoomsAll.isSelected()){
            filterRightsRooms = "('DA','NE')";
        }
        else if(radioFilterRoomsYes.isSelected()){
            filterRightsRooms = "('DA')";
        }
        else{
            filterRightsRooms = "('NE')";
        }
        if(radioFilterReservationAll.isSelected()){
            filterRightsReservations = "('DA','NE')";
        }
        else if(radioFilterReservationYes.isSelected()){
            filterRightsReservations = "('DA')";
        }
        else{
            filterRightsReservations = "('NE')";
        }
        String queryString = checkFilters();
        tableUsers(queryString);
    }

    @FXML
    void actionFilterClear(ActionEvent event) {
        tfFilterUsername.setText("");
        tfFilterPassword.setText("");
        filterUsername = "";
        filterPassword = "";
        filterRightsUsername = "('DA','NE')";
        filterRightsStudents = "('DA','NE')";
        filterRightsRooms = "('DA','NE')";
        filterRightsReservations = "('DA','NE')";
        radioFilterUsernameAll.setSelected(true);
        radioFilterStudentsAll.setSelected(true);
        radioFilterRoomsAll.setSelected(true);
        radioFilterReservationAll.setSelected(true);
    }

    @FXML
    void actionLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("LoginView.fxml"));
        Stage stage = (Stage)buttonLogout.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        stage.setTitle("Studentski Dom - Prijava");
        stage.setScene(scene);
        stage.show();
        stage.show();
    }

    @FXML
    void actionToResCreate(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("ReservationView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Kreiranje rezervacija");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToResEdit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("ReservationEditView.fxml"));
        Stage stage = (Stage)buttonToResCreate.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Uređivanje rezervacija");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToRooms(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("RoomView.fxml"));
        Stage stage = (Stage)buttonToRooms.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Sobe");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actionToStudent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource("StudentView.fxml"));
        Stage stage = (Stage)buttonToStudent.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        stage.setTitle("Studentski Dom - Studenti");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void getRowInfo(MouseEvent event) {
        RecordUser record = new RecordUser();
        if(tableViewUsers.getSelectionModel().getSelectedIndex() != -1){
            record = tableViewUsers.getItems().get(tableViewUsers.getSelectionModel().getSelectedIndex());
            tfUsername.setText(record.getUsername());
            tfPassword.setText(record.getPassword());
            if("DA".equals(record.getRightsUsers())){
                radioUsernameYes.setSelected(true);
            }
            else{
                radioUsernameNo.setSelected(true);
            }
            if("DA".equals(record.getRightsStudents())){
                radioStudentsYes.setSelected(true);
            }
            else{
                radioStudentsNo.setSelected(true);
            }
            if("DA".equals(record.getRightsRooms())){
                radioRoomsYes.setSelected(true);
            }
            else{
                radioRoomsNo.setSelected(true);
            }
            if("DA".equals(record.getRightsReservations())){
                radioReservationYes.setSelected(true);
            }
            else{
                radioReservationNo.setSelected(true);
            }
            pickedID = record.getUserID();
            if(pickedID.equals("1") || pickedID.equals(LoggedInRights.getLoggedInID())){
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
            }
            else{
                buttonEdit.setDisable(false);
                buttonDelete.setDisable(false);
            }
        }
    }

    public void tableUsers(String queryString){
        ObservableList<RecordUser> records = FXCollections.observableArrayList();
        try{
            pst = conn.prepareStatement(queryString);
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    RecordUser record = new RecordUser();
                    record.setUserID(rs.getString("idKorisnik"));
                    record.setUsername(rs.getString("KorisnickoIme"));
                    record.setPassword(rs.getString("Sifra"));
                    record.setRightsUsers(rs.getString("dopustenjeKorisnici"));
                    record.setRightsStudents(rs.getString("dopustenjeStudenti"));
                    record.setRightsRooms(rs.getString("dopustenjeSobe"));
                    record.setRightsReservations(rs.getString("dopustenjeSmjestaj"));
                    records.add(record);
                }
            }
            tableViewUsers.setItems(records);
            colID.setCellValueFactory(f -> f.getValue().userIDProperty());
            colUsername.setCellValueFactory(f -> f.getValue().usernameProperty());
            colPassword.setCellValueFactory(f -> f.getValue().passwordProperty());
            colRightsUsers.setCellValueFactory(f -> f.getValue().rightsUsersProperty());
            colRightsStudents.setCellValueFactory(f -> f.getValue().rightsStudentsProperty());
            colRightsRooms.setCellValueFactory(f -> f.getValue().rightsRoomsProperty());
            colRightsReservations.setCellValueFactory(f -> f.getValue().rightsReservationsProperty());
        }catch (SQLException ex){
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
        String queryString = checkFilters();
        tableUsers(queryString);
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
