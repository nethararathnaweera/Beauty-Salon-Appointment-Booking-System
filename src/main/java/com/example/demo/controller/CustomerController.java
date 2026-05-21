package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer")
    public String customerPage() {

        return "customer";
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String membershipType) {

        Customer c = new Customer();

        c.setName(name);

        c.setEmail(email);

        c.setPhone(phone);

        c.setMembershipType(
                membershipType
        );

        customerService
                .saveCustomer(c);

        return "redirect:/customers";
    }

    @GetMapping("/customers")
    public String listCustomers(
            Model model) {

        model.addAttribute(
                "customers",
                customerService
                        .getAllCustomers()
        );

        return "customer-list";
    }

    @GetMapping("/editCustomer/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "customer",
                customerService
                        .getCustomerById(id)
        );

        return "customer-edit";
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String membershipType) {

        Customer c =
                new Customer();

        c.setId(id);

        c.setName(name);

        c.setEmail(email);

        c.setPhone(phone);

        c.setMembershipType(
                membershipType
        );

        customerService
                .updateCustomer(c);

        return "redirect:/customers";
    }

    @GetMapping("/deleteCustomer/{id}")
    public String deleteCustomer(
            @PathVariable Long id) {

        customerService
                .deleteCustomer(id);

        return "redirect:/customers";
    }
}