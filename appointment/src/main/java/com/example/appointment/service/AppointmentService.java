package com.example.appointment.service;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final String FILE_NAME = "appointments.txt";

    // CREATE
    public void saveAppointment(Appointment appointment) {
        try {
            int count = getAllAppointments().size() + 1;
            appointment.setAppointmentID(count);

            FileWriter writer = new FileWriter(FILE_NAME, true);
            writer.write(
                    appointment.getAppointmentID() + "," +
                            appointment.getCustomerID()    + "," +
                            appointment.getCustomerName()  + "," +
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
                a.setDate(parts[3]);
                a.setTime(parts[4]);
                a.setStatus(AppointmentStatus.valueOf(parts[5]));
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
        List<Appointment> list = getAllAppointments();
        for (Appointment a : list) {
            if (a.getAppointmentID() == id) {
                return a;
            }
        }
        return null;
    }

    // UPDATE
    public void updateAppointment(int id, Appointment updated) {
        try {
            List<Appointment> list = getAllAppointments();
            FileWriter writer = new FileWriter(FILE_NAME, false);
            for (Appointment a : list) {
                if (a.getAppointmentID() == id) {
                    writer.write(
                            id                          + "," +
                                    updated.getCustomerID()     + "," +
                                    updated.getCustomerName()   + "," +
                                    updated.getDate()           + "," +
                                    updated.getTime()           + "," +
                                    updated.getStatus()         + "\n"
                    );
                } else {
                    writer.write(
                            a.getAppointmentID() + "," +
                                    a.getCustomerID()    + "," +
                                    a.getCustomerName()  + "," +
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