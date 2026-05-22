package com.example.appointment.service;

import com.example.appointment.entity.Customer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final String FILE_NAME = "customers.txt";

    public int generateCustomerID() {
        int maxId = 0;

        for (Customer customer : getAllCustomers()) {
            if (customer.getCustomerID() > maxId) {
                maxId = customer.getCustomerID();
            }
        }

        return maxId + 1;
    }

    public void saveCustomer(Customer customer) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);

            writer.write(
                    customer.getCustomerID() + "|" +
                            customer.getFullName() + "|" +
                            customer.getEmail() + "|" +
                            customer.getPhone() + "|" +
                            customer.getPassword() + "\n"
            );

            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Error saving customer: " + e.getMessage());
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                return customers;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);

                if (parts.length < 5) {
                    continue;
                }

                Customer customer = new Customer();
                customer.setCustomerID(Integer.parseInt(parts[0]));
                customer.setFullName(parts[1]);
                customer.setEmail(parts[2]);
                customer.setPhone(parts[3]);
                customer.setPassword(parts[4]);

                customers.add(customer);
            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException("Error reading customers: " + e.getMessage());
        }

        return customers;
    }

    public Customer findByEmail(String email) {
        for (Customer customer : getAllCustomers()) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }

        return null;
    }

    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    public Customer validateLogin(String email, String password) {
        Customer customer = findByEmail(email);

        if (customer == null) {
            return null;
        }

        if (customer.getPassword().equals(password)) {
            return customer;
        }

        return null;
    }
}