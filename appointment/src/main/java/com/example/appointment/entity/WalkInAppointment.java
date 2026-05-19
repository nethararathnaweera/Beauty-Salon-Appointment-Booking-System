package com.example.appointment.entity;

public class WalkInAppointment extends Appointment {

    private String counterNumber;

    public WalkInAppointment(int appointmentID, int customerID,
                             String customerName, String date,
                             String time, String counterNumber) {
        super(appointmentID, customerID, customerName, date, time);
        this.counterNumber = counterNumber;
    }

    public WalkInAppointment() {}

    public String displayInfo() {
        return super.displayInfo()
                + " | Walk-in counter: " + counterNumber;
    }


    public String getCounterNumber()          {
        return counterNumber; }
    public void setCounterNumber(String c)    {
        this.counterNumber = c; }
}
