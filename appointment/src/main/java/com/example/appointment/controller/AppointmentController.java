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
    public String appointmentPage(Model model) {
        model.addAttribute("newCustomerID",
                appointmentService.generateCustomerID());
        return "appointment";
    }

    // Save new appointment
    @PostMapping("/saveAppointment")
    public String saveAppointment(
            @RequestParam int customerID,
            @RequestParam String customerName,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time,
            Model model) {

        // validate date
        if (!appointmentService.isValidDate(date)) {
            model.addAttribute("error",
                    "Invalid date! Please select today or a future date.");
            model.addAttribute("newCustomerID", customerID);
            return "appointment";
        }

        // validate time
        if (!appointmentService.isValidTime(time)) {
            model.addAttribute("error",
                    "Invalid time! Appointments are only available "
                            + "between 8:00 AM and 4:00 PM.");
            model.addAttribute("newCustomerID", customerID);
            return "appointment";
        }

        // one appointment per customer
        if (appointmentService.hasActiveAppointment(customerID)) {
            model.addAttribute("error",
                    "You already have an active appointment! "
                            + "Please complete or cancel it before booking a new one.");
            model.addAttribute("newCustomerID", customerID);
            return "appointment";
        }

        Appointment appointment = new Appointment();
        appointment.setCustomerID(customerID);
        appointment.setCustomerName(customerName);
        appointment.setServiceType(serviceType);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentService.saveAppointment(appointment);

        // send to success page
        int newId = appointmentService.getAllAppointments().size();
        Appointment saved = appointmentService.getAppointmentById(newId);
        model.addAttribute("appointment", saved);
        return "appointment-success";
    }

    // CUSTOMER VIEW — filtered by customer ID
    @GetMapping("/appointments")
    public String listAppointments(
            @RequestParam(required = false) Integer customerID,
            Model model) {

        if (customerID != null) {
            model.addAttribute("appointments",
                    appointmentService.getAppointmentsByCustomerID(customerID));
            model.addAttribute("customerID", customerID);
            model.addAttribute("isFiltered", true);
        } else {
            model.addAttribute("appointments", new java.util.ArrayList<>());
            model.addAttribute("isFiltered", false);
        }
        return "appointment-list";
    }

    // ADMIN VIEW — all appointments
    @GetMapping("/admin/appointments")
    public String adminAppointments(Model model) {
        model.addAttribute("appointments",
                appointmentService.getAllAppointments());
        model.addAttribute("isFiltered", false);
        model.addAttribute("isAdmin", true);
        return "appointment-list";
    }


    // CONFIRM
    @GetMapping("/confirmAppointment/{id}")
    public String confirmAppointment(@PathVariable int id,
                                     @RequestParam(required = false) Integer customerID) {
        appointmentService.updateStatus(id, AppointmentStatus.CONFIRMED);
        if (customerID != null)
            return "redirect:/appointments?customerID=" + customerID;
        return "redirect:/admin/appointments";
    }

    // COMPLETE
    @GetMapping("/completeAppointment/{id}")
    public String completeAppointment(@PathVariable int id,
                                      @RequestParam(required = false) Integer customerID) {
        appointmentService.updateStatus(id, AppointmentStatus.COMPLETED);
        if (customerID != null)
            return "redirect:/appointments?customerID=" + customerID;
        return "redirect:/admin/appointments";
    }

    // CANCEL
    @GetMapping("/cancelAppointment/{id}")
    public String cancelAppointment(@PathVariable int id,
                                    @RequestParam(required = false) Integer customerID) {
        appointmentService.updateStatus(id, AppointmentStatus.CANCELLED);
        if (customerID != null)
            return "redirect:/appointments?customerID=" + customerID;
        return "redirect:/admin/appointments";
    }

    // EDIT FORM — admin only
    @GetMapping("/editAppointment/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("appointment",
                appointmentService.getAppointmentById(id));
        return "appointment-edit";
    }

    // UPDATE — admin only
    @PostMapping("/updateAppointment")
    public String updateAppointment(
            @RequestParam int id,
            @RequestParam int customerID,
            @RequestParam String customerName,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String status) {

        Appointment a = new Appointment();
        a.setCustomerID(customerID);
        a.setCustomerName(customerName);
        a.setServiceType(serviceType);
        a.setDate(date);
        a.setTime(time);
        a.setStatus(AppointmentStatus.valueOf(status));

        appointmentService.updateAppointment(id, a);
        return "redirect:/admin/appointments";
    }

    // DELETE
    @GetMapping("/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable int id,
                                    @RequestParam(required = false) Integer customerID) {
        appointmentService.deleteAppointment(id);
        if (customerID != null)
            return "redirect:/appointments?customerID=" + customerID;
        return "redirect:/admin/appointments";
    }
}
