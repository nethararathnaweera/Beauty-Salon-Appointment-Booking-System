package com.example.appointment.controller;

import com.example.appointment.entity.Customer;
import com.example.appointment.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        if (customerService.emailExists(email)) {
            model.addAttribute("error", "This email is already registered. Please login.");
            return "register";
        }

        Customer customer = new Customer();
        customer.setCustomerID(customerService.generateCustomerID());
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setPassword(password);

        customerService.saveCustomer(customer);

        session.setAttribute("customerID", customer.getCustomerID());
        session.setAttribute("customerName", customer.getFullName());
        session.setAttribute("customerEmail", customer.getEmail());
        session.setAttribute("customerPhone", customer.getPhone());

        return "redirect:/appointments";
    }

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String logout,
            Model model) {

        if (logout != null) {
            model.addAttribute("success", "You have logged out successfully.");
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginCustomer(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Customer customer = customerService.validateLogin(email, password);

        if (customer == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        session.setAttribute("customerID", customer.getCustomerID());
        session.setAttribute("customerName", customer.getFullName());
        session.setAttribute("customerEmail", customer.getEmail());
        session.setAttribute("customerPhone", customer.getPhone());

        return "redirect:/appointments";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}