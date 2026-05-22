package com.example.appointment.entity;

public class OnlineAppointment extends Appointment {


    private String platform;


    public OnlineAppointment(int appointmentID, int customerID,
                             String customerName, String serviceType, double price, String date,
                             String time, String platform) {
        super(appointmentID, customerID, customerName, serviceType, price, date, time);
        this.platform = platform;
    }

    public OnlineAppointment() {}


    public String displayInfo() {
        return super.displayInfo()
                + " | Booked via: " + platform;
    }

    public String getPlatform()          {
        return platform; }
    public void setPlatform(String p)    {
        this.platform = p; }
}
