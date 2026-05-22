package com.example.appointment.service;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final String FILE_NAME = "appointments.txt";

    // GET SERVICE PRICE
    public double getServicePrice(String serviceType) {
        switch (serviceType) {
            case "Haircut":       return 1500.00;
            case "Hair Coloring": return 2500.00;
            case "Facial":        return 1800.00;
            case "Manicure":      return 1200.00;
            case "Pedicure":      return 1000.00;
            default:              return 0.00;
        }
    }

    // VALIDATE DATE — no past dates
    public boolean isValidDate(String date) {
        try {
            LocalDate selected = LocalDate.parse(date);
            return !selected.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    // VALIDATE TIME — 8AM to 4PM only
    public boolean isValidTime(String time) {
        try {
            LocalTime selected = LocalTime.parse(time);
            LocalTime start    = LocalTime.of(8, 0);
            LocalTime end      = LocalTime.of(16, 0);
            return !selected.isBefore(start) && !selected.isAfter(end);
        } catch (Exception e) {
            return false;
        }
    }

    // CHECK ONE APPOINTMENT PER CUSTOMER
    public boolean hasActiveAppointment(int customerID) {
        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID() == customerID &&
                    (a.getStatus() == AppointmentStatus.PENDING ||
                            a.getStatus() == AppointmentStatus.CONFIRMED)) {
                return true;
            }
        }
        return false;
    }

    // AUTO GENERATE CUSTOMER ID
    public int generateCustomerID() {
        return getAllAppointments().size() + 1;
    }

    // CREATE
    public void saveAppointment(Appointment appointment) {
        try {
            int count = getAllAppointments().size() + 1;
            appointment.setAppointmentID(count);
            appointment.setPrice(getServicePrice(
                    appointment.getServiceType()));


            FileWriter writer = new FileWriter(FILE_NAME, true);
            writer.write(
                    appointment.getAppointmentID() + "," +
                            appointment.getCustomerID()    + "," +
                            appointment.getCustomerName()  + "," +
                            appointment.getServiceType()   + "," +
                            appointment.getPrice()         + "," +
                            appointment.getDate()          + "," +
                            appointment.getTime()          + "," +
                            appointment.getStatus()        + "\n"
            );
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Error saving: " + e.getMessage());
        }
    }

    // READ ALL
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return list;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                Appointment a = new Appointment();
                a.setAppointmentID(Integer.parseInt(parts[0]));
                a.setCustomerID(Integer.parseInt(parts[1]));
                a.setCustomerName(parts[2]);
                a.setServiceType(parts[3]);
                a.setPrice(Double.parseDouble(parts[4]));
                a.setDate(parts[5]);
                a.setTime(parts[6]);
                a.setStatus(AppointmentStatus.valueOf(parts[7]));
                list.add(a);
            }
            reader.close();

        } catch (Exception e) {
            throw new RuntimeException("Error reading: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Appointment getAppointmentById(int id) {
        for (Appointment a : getAllAppointments()) {
            if (a.getAppointmentID() == id) return a;
        }
        return null;
    }

    // GET BY CUSTOMER ID
    public List<Appointment> getAppointmentsByCustomerID(int customerID) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID() == customerID) result.add(a);
        }
        return result;
    }


    // UPDATE status
    public void updateStatus(int id, AppointmentStatus newStatus) {
        try {
            List<Appointment> list = getAllAppointments();
            FileWriter writer = new FileWriter(FILE_NAME, false);
            for (Appointment a : list) {
                AppointmentStatus s =
                        a.getAppointmentID() == id ? newStatus : a.getStatus();
                writer.write(
                        a.getAppointmentID() + "," +
                                a.getCustomerID()    + "," +
                                a.getCustomerName()  + "," +
                                a.getServiceType()   + "," +
                                a.getPrice()         + "," +
                                a.getDate()          + "," +
                                a.getTime()          + "," +
                                s                    + "\n"
                );
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error updating status: "
                    + e.getMessage());
        }
    }
    // UPDATE — admin only

    public void updateAppointment(int id, Appointment updated) {
        try {
            List<Appointment> list = getAllAppointments();
            FileWriter writer = new FileWriter(FILE_NAME, false);
            for (Appointment a : list) {
                if (a.getAppointmentID() == id) {
                    double price = getServicePrice(
                            updated.getServiceType());
                    writer.write(
                            id                          + "," +
                                    updated.getCustomerID()     + "," +
                                    updated.getCustomerName()   + "," +
                                    updated.getServiceType()    + "," +
                                    price                       + "," +
                                    updated.getDate()           + "," +
                                    updated.getTime()           + "," +
                                    updated.getStatus()         + "\n"
                    );
                }else {
                    writer.write(
                            a.getAppointmentID() + "," +
                                    a.getCustomerID()    + "," +
                                    a.getCustomerName()  + "," +
                                    a.getServiceType()   + "," +
                                    a.getPrice()         + "," +
                                    a.getDate()          + "," +
                                    a.getTime()          + "," +
                                    a.getStatus()        + "\n"
                    );
                }
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Error updating: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteAppointment(int id) {
        try {
            List<Appointment> list = getAllAppointments();
            FileWriter writer = new FileWriter(FILE_NAME, false);
            for (Appointment a : list) {
                if (a.getAppointmentID() != id) {
                    writer.write(
                            a.getAppointmentID() + "," +
                                    a.getCustomerID()    + "," +
                                    a.getCustomerName()  + "," +
                                    a.getServiceType()   + "," +
                                    a.getPrice()         + "," +
                                    a.getDate()          + "," +
                                    a.getTime()          + "," +
                                    a.getStatus()        + "\n"
                    );
                }
            }
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Error deleting: " + e.getMessage());
        }
    }
}