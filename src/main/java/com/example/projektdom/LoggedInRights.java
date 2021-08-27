package com.example.projektdom;

public class LoggedInRights {
    private static String loggedInID;
    private static boolean canEditUsers;
    private static boolean canEditStudents;
    private static boolean canEditRooms;
    private static boolean canEditReservations;

    //GETTERS AND SETTERS
    public static String getLoggedInID() {
        return loggedInID;
    }

    public static void setLoggedInID(String loggedInID) {
        LoggedInRights.loggedInID = loggedInID;
    }

    public boolean getCanEditUsers() {
        return canEditUsers;
    }

    public void setCanEditUsers(boolean canEditUsers) {
        this.canEditUsers = canEditUsers;
    }

    public boolean getCanEditStudents() {
        return canEditStudents;
    }

    public void setCanEditStudents(boolean canEditStudents) {
        this.canEditStudents = canEditStudents;
    }

    public boolean getCanEditRooms() {
        return canEditRooms;
    }

    public void setCanEditRooms(boolean canEditRooms) {
        this.canEditRooms = canEditRooms;
    }

    public boolean getCanEditReservations() {
        return canEditReservations;
    }

    public void setCanEditReservations(boolean canEditReservations) {
        this.canEditReservations = canEditReservations;
    }

    //CONSTRUCTOR
    public LoggedInRights() {
        loggedInID = this.getLoggedInID();
        canEditUsers = this.getCanEditUsers();
        canEditStudents = this.getCanEditStudents();
        canEditRooms = this.getCanEditRooms();
        canEditReservations = this.getCanEditReservations();
    }
}
