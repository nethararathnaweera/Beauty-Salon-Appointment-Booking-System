package controller;

import model.AdminUser;
import service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Show login page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Show admin dashboard
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        try {
            List<AdminUser> admins = adminService.getAllAdmins();
            model.addAttribute("admins", admins);
            return "admin-dashboard";
        } catch (IOException e) {
            model.addAttribute("error", "Error reading data");
            return "admin-dashboard";
        }
    }

    // CREATE - Handle form submission for new admin
    @PostMapping("/admin/create")
    public String createAdmin(@RequestParam int id,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email) {
        try {
            AdminUser newAdmin = new AdminUser(id, username, password, email, true, true);
            adminService.addAdmin(newAdmin);
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            return "error";
        }
    }

    // DELETE - Remove admin
    @GetMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable int id) {
        try {
            adminService.deleteAdmin(id);
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            return "error";
        }
    }
}
