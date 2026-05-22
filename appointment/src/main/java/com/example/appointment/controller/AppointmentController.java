package com.example.appointment.controller;

import jakarta.servlet.http.HttpSession;
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
    public String appointmentPage(HttpSession session, Model model) {

        Integer customerID = (Integer) session.getAttribute("customerID");
        String customerName = (String) session.getAttribute("customerName");

        if (customerID == null || customerName == null) {
            return "redirect:/login";
        }

        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        return "appointment";
    }

    // Save new appointment
    @PostMapping("/saveAppointment")
    public String saveAppointment(
            HttpSession session,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time,
            Model model) {

        Integer customerID = (Integer) session.getAttribute("customerID");
        String customerName = (String) session.getAttribute("customerName");

        if (customerID == null || customerName == null) {
            return "redirect:/login";
        }

        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        // validate date
        if (!appointmentService.isValidDate(date)) {
            model.addAttribute("error",
                    "Invalid date! Please select today or a future date.");
            return "appointment";
        }

        // validate time
        if (!appointmentService.isValidTime(time)) {
            model.addAttribute("error",
                    "Invalid time! Appointments are only available between 8:00 AM and 4:00 PM.");
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

        return "redirect:/appointments?success=true";
    }

    // CUSTOMER VIEW — filtered by customer ID
    @GetMapping("/appointments")
    public String listAppointments(
            HttpSession session,
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String updated,
            Model model) {

        Integer customerID = (Integer) session.getAttribute("customerID");
        String customerName = (String) session.getAttribute("customerName");
        model.addAttribute("updated", updated);
        if (customerID == null || customerName == null) {
            return "redirect:/login";
        }

        model.addAttribute("appointments",
                appointmentService.getAppointmentsByCustomerID(customerID));

        model.addAttribute("customerID", customerID);
        model.addAttribute("customerName", customerName);

        // These two lines are needed because appointment-list.html uses them
        model.addAttribute("isFiltered", true);
        model.addAttribute("isAdmin", false);

        model.addAttribute("success", success);

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
    public String editForm(
            @PathVariable int id,
            HttpSession session,
            Model model) {

        Integer customerID = (Integer) session.getAttribute("customerID");

        if (customerID == null) {
            return "redirect:/login";
        }

        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null || appointment.getCustomerID() != customerID) {
            return "redirect:/appointments";
        }

        model.addAttribute("appointment", appointment);
        return "appointment-edit";
    }

    // UPDATE — admin only
    @PostMapping("/updateAppointment")
    public String updateAppointment(
            HttpSession session,
            @RequestParam int id,
            @RequestParam String serviceType,
            @RequestParam String date,
            @RequestParam String time,
            Model model) {

        Integer customerID = (Integer) session.getAttribute("customerID");
        String customerName = (String) session.getAttribute("customerName");

        if (customerID == null || customerName == null) {
            return "redirect:/login";
        }

        Appointment existing = appointmentService.getAppointmentById(id);

        if (existing == null) {
            return "redirect:/appointments";
        }

        if (existing.getCustomerID() != customerID) {
            return "redirect:/appointments";
        }

        if (!appointmentService.isValidDate(date)) {
            model.addAttribute("error",
                    "Invalid date! Please select today or a future date.");
            model.addAttribute("appointment", existing);
            return "appointment-edit";
        }

        if (!appointmentService.isValidTime(time)) {
            model.addAttribute("error",
                    "Invalid time! Appointments are only available between 8:00 AM and 4:00 PM.");
            model.addAttribute("appointment", existing);
            return "appointment-edit";
        }

        Appointment updated = new Appointment();
        updated.setCustomerID(customerID);
        updated.setCustomerName(customerName);
        updated.setServiceType(serviceType);
        updated.setDate(date);
        updated.setTime(time);
        updated.setStatus(existing.getStatus());

        appointmentService.updateAppointment(id, updated);

        return "redirect:/appointments?updated=true";
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
