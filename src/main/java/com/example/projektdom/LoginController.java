package com.example.projektdom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    Connection conn;
    PreparedStatement pst;

    @FXML
    private TextField tfUsername;

    @FXML
    private Button buttonLogin;

    @FXML
    private PasswordField tfPassword;

    @FXML
    void actionLogin(ActionEvent event) throws IOException{
        int nubmerOfRights = 4;
        String goToView = "StudentView.fxml";
        String title = "Studentski Dom - Studenti";
        String username = "";
        String password = "";
        String extractedID = "";
        String extractedUsername = "";
        String extractedPassword = "";
        String extractedRightsUsers = "";
        String extractedRightsStudents = "";
        String extractedRightsRooms = "";
        String extractedRightsReservations = "";
        username = tfUsername.getText();
        password = tfPassword.getText();
        if(username.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("");
            alert.setContentText("Sva polja moraju biti popunjena");
            alert.showAndWait();
        }
        else{
            try{
                pst = conn.prepareStatement(" SELECT * FROM korisnici WHERE korisnickoIme IN(?) ");
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                if(!rs.isBeforeFirst()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greska");
                    alert.setHeaderText("");
                    alert.setContentText("Korisnik nije pronadjen u bazi podataka");
                    alert.showAndWait();
                }
                else{
                    while(rs.next()){
                        RecordUser record = new RecordUser();
                        record.setUserID(rs.getString("idKorisnik"));
                        record.setUsername(rs.getString("korisnickoIme"));
                        record.setPassword(rs.getString("sifra"));
                        record.setRightsUsers(rs.getString("dopustenjeKorisnici"));
                        record.setRightsStudents(rs.getString("dopustenjeStudenti"));
                        record.setRightsRooms(rs.getString("dopustenjeSobe"));
                        record.setRightsReservations(rs.getString("dopustenjeSmjestaj"));
                        extractedID = record.getUserID();
                        extractedUsername = record.getUsername();
                        extractedPassword = record.getPassword();
                        extractedRightsUsers = record.getRightsUsers();
                        extractedRightsStudents = record.getRightsStudents();
                        extractedRightsRooms = record.getRightsRooms();
                        extractedRightsReservations = record.getRightsReservations();
                    }
                    if(!extractedPassword.equals(password)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Greska");
                        alert.setHeaderText("");
                        alert.setContentText("Pogresna zaporka");
                        alert.showAndWait();
                    }
                    else{
                        LoggedInRights newLogin = new LoggedInRights();
                        LoggedInRights.setLoggedInID(extractedID);
                        if(extractedRightsStudents.equals("DA")){
                            newLogin.setCanEditStudents(true);
                        }
                        else{
                            newLogin.setCanEditStudents(false);
                            nubmerOfRights -= 1;
                            goToView = "RoomView.fxml";
                            title = "Studentski Dom - Sobe";
                        }
                        if(extractedRightsRooms.equals("DA")){
                            newLogin.setCanEditRooms(true);
                        }
                        else{
                            newLogin.setCanEditRooms(false);
                            nubmerOfRights -= 1;
                            goToView = "ReservationView.fxml";
                            title = "Studentski Dom - Kreiranje rezervacija";
                        }
                        if(extractedRightsReservations.equals("DA")){
                            newLogin.setCanEditReservations(true);
                        }
                        else{
                            newLogin.setCanEditReservations(false);
                            nubmerOfRights -= 1;
                            goToView = "UsersView.fxml";
                            title = "Studentski Dom - Korisnici";
                        }
                        if(extractedRightsUsers.equals("DA")){
                            newLogin.setCanEditUsers(true);
                        }
                        else{
                            newLogin.setCanEditUsers(false);
                            nubmerOfRights -= 1;
                        }
                        if(nubmerOfRights > 0){
                            FXMLLoader fxmlLoader = new FXMLLoader(RoomsController.class.getResource(goToView));
                            Stage stage = (Stage)buttonLogin.getScene().getWindow();
                            Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
                            stage.setTitle(title);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Greska");
                            alert.setHeaderText("Nemate nikakva prava nad bazom podataka");
                            alert.setContentText("Obradite se administratoru");
                            alert.showAndWait();
                        }

                    }
                }
            }catch(SQLException ex){
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/stdom", "root", "1234");
        }
        catch (SQLException ex){
            System.out.println("SQL Error: " + ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connect();
    }
}
