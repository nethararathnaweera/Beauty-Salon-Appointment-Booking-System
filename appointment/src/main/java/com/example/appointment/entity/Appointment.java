package com.example.appointment.entity;

public class Appointment {

    private int appointmentID;
    private int customerID;
    private String customerName;
    private String date;
    private String time;
    private AppointmentStatus status;

    public Appointment(int appointmentID, int customerID, String customerName,
                       String date, String time) {
        this.appointmentID = appointmentID;
        this.customerID    = customerID;
        this.customerName  = customerName;
        this.date          = date;
        this.time          = time;
        this.status        = AppointmentStatus.PENDING;
    }

    public Appointment() {}

    public String displayInfo() {
        return "Appointment [" + appointmentID + "]"
                + " | Customer: " + customerName
                + " | Date: " + date + " at " + time
                + " | Status: " + status;
    }

    public int getAppointmentID()  {
        return appointmentID; }
    public int getCustomerID()   {
        return customerID; }
    public String getCustomerName()   {
        return customerName; }
    public String getDate()    {
        return date; }
    public String getTime()   {
        return time; }
    public AppointmentStatus getStatus()    {
        return status; }

    public void setAppointmentID(int a)  {
        this.appointmentID = a; }
    public void setCustomerID(int c)   {
        this.customerID = c; }
    public void setCustomerName(String n)   {
        this.customerName = n; }
    public void setDate(String date)   {
        this.date = date; }
    public void setTime(String time)  {
        this.time = time; }
    public void setStatus(AppointmentStatus s)   {
        this.status = s; }
}
