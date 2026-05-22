package service;

import model.AdminUser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private static final String FILE_PATH = "admins.txt";

    // CREATE - Add new admin to file
    public void addAdmin(AdminUser admin) throws IOException {
        // Open file in append mode (true = append, not overwrite)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(admin.toFileString());
            writer.newLine();
        }
    }

    // READ - Get all admins from file
    public List<AdminUser> getAllAdmins() throws IOException {
        List<AdminUser> admins = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return admins;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse line: id,username,password,email,role,canManageAdmins,canViewReports
                String[] parts = line.split(",");
                AdminUser admin = new AdminUser(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        Boolean.parseBoolean(parts[5]),
                        Boolean.parseBoolean(parts[6])
                );
                admins.add(admin);
            }
        }
        return admins;
    }

    // UPDATE - Find admin by ID and update details
    public void updateAdmin(AdminUser updatedAdmin) throws IOException {
        List<AdminUser> admins = getAllAdmins();

        // Find and replace
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId() == updatedAdmin.getId()) {
                admins.set(i, updatedAdmin);
                break;
            }
        }

        // Write all back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (AdminUser admin : admins) {
                writer.write(admin.toFileString());
                writer.newLine();
            }
        }
    }

    // DELETE - Remove admin by ID
    public void deleteAdmin(int id) throws IOException {
        List<AdminUser> admins = getAllAdmins();
        admins.removeIf(admin -> admin.getId() == id);

        // Rewrite file without deleted admin
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (AdminUser admin : admins) {
                writer.write(admin.toFileString());
                writer.newLine();
            }
        }
    }
}
