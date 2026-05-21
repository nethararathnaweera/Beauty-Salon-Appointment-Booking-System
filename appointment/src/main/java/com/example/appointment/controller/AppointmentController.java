package com.example.appointment.controller;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import com.example.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Show booking form
    @GetMapping("/appointment")
    public String appointmentPage() {
        return "appointment";
    }

    // Save new appointment
    @PostMapping("/saveAppointment")
    public String saveAppointment(
            @RequestParam int customerID,
            @RequestParam String customerName,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time) {

        Appointment appointment = new Appointment();
        appointment.setCustomerID(customerID);
        appointment.setCustomerName(customerName);
        appointment.setServiceType(serviceType);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments";
    }

    // View all appointments
    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments",
                appointmentService.getAllAppointments());
        return "appointment-list";
    }

    // Show edit form
    @GetMapping("/editAppointment/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("appointment",
                appointmentService.getAppointmentById(id));
        return "appointment-edit";
    }

    // Save update appointment
    @PostMapping("/updateAppointment")
    public String updateAppointment(
            @RequestParam int id,
            @RequestParam int customerID,
            @RequestParam String customerName,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String status) {

        Appointment appointment = new Appointment();
        appointment.setCustomerID(customerID);
        appointment.setCustomerName(customerName);
        appointment.setServiceType(serviceType);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setStatus(AppointmentStatus.valueOf(status));

        appointmentService.updateAppointment(id, appointment);
        return "redirect:/appointments";
    }

    // Delete appointment
    @GetMapping("/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}
