package es.otherperspectiv.myapplication.models;

public class Shift {
    private String dateStart;
    private int workingHours;
    private String username;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public Shift(String dateStart, int workingHours, String username) {
        this.dateStart = dateStart;
        this.workingHours = workingHours;
        this.username = username;
    }

    public Shift(String dateStart, int workingHours) {
        this.dateStart = dateStart;
        this.workingHours = workingHours;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
