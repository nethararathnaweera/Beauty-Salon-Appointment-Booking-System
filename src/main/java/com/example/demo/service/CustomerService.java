package com.example.demo.service;

import com.example.demo.entity.Customer;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final String FILE_NAME =
            "customers.txt";

    // CREATE
    public Customer saveCustomer(
            Customer customer) {

        try {

            customer.setId(
                    System.currentTimeMillis()
            );

            FileWriter writer =
                    new FileWriter(
                            FILE_NAME,
                            true
                    );

            writer.write(
                    customer.getId() + "," +
                    customer.getName() + "," +
                    customer.getEmail() + "," +
                    customer.getPhone() + "," +
                    customer.getMembershipType() +
                    "\n"
            );

            writer.close();

            return customer;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error saving customer"
            );
        }
    }

    // READ ALL
    public List<Customer> getAllCustomers() {

        List<Customer> list =
                new ArrayList<>();

        try {

            File file =
                    new File(FILE_NAME);

            if (!file.exists()) {
                return list;
            }

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts =
                        line.split(",");

                Customer c =
                        new Customer();

                c.setId(
                        Long.parseLong(parts[0])
                );

                c.setName(parts[1]);

                c.setEmail(parts[2]);

                c.setPhone(
                        parts.length > 3
                                ? parts[3]
                                : ""
                );

                c.setMembershipType(
                        parts.length > 4
                                ? parts[4]
                                : "Regular"
                );

                list.add(c);
            }

            reader.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error reading customers"
            );
        }

        return list;
    }

    // READ ONE
    public Customer getCustomerById(
            Long id) {

        return getAllCustomers()
                .stream()
                .filter(c ->
                        c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // UPDATE
    public void updateCustomer(
            Customer updated) {

        try {

            List<Customer> all =
                    getAllCustomers();

            FileWriter writer =
                    new FileWriter(
                            FILE_NAME,
                            false
                    );

            for (Customer c : all) {

                Customer w =
                        c.getId().equals(
                                updated.getId())
                                ? updated
                                : c;

                writer.write(
                        w.getId() + "," +
                        w.getName() + "," +
                        w.getEmail() + "," +
                        w.getPhone() + "," +
                        w.getMembershipType() +
                        "\n"
                );
            }

            writer.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error updating customer"
            );
        }
    }

    // DELETE
    public void deleteCustomer(
            Long id) {

        try {

            List<Customer> all =
                    getAllCustomers();

            all.removeIf(c ->
                    c.getId().equals(id));

            FileWriter writer =
                    new FileWriter(
                            FILE_NAME,
                            false
                    );

            for (Customer c : all) {

                writer.write(
                        c.getId() + "," +
                        c.getName() + "," +
                        c.getEmail() + "," +
                        c.getPhone() + "," +
                        c.getMembershipType() +
                        "\n"
                );
            }

            writer.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error deleting customer"
            );
        }
    }
}