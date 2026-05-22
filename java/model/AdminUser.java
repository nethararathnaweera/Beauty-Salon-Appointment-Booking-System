package model;

// INHERITANCE: AdminUser IS-A User
public class AdminUser extends User {

    private boolean canManageAdmins;
    private boolean canViewReports;

    public AdminUser(int id, String username, String password, String email,
                     boolean canManageAdmins, boolean canViewReports) {
        // Call parent constructor
        super(id, username, password, email, "ADMIN");
        this.canManageAdmins = canManageAdmins;
        this.canViewReports = canViewReports;
    }

    // POLYMORPHISM: Override abstract method
    @Override
    public String getDashboardURL() {
        return "/admin/dashboard";
    }

    // Getters and setters for admin-specific fields
    public boolean isCanManageAdmins() { return canManageAdmins; }
    public void setCanManageAdmins(boolean canManageAdmins) {
        this.canManageAdmins = canManageAdmins;
    }

    @Override
    public String toFileString() {
        // Include admin-specific fields when saving to file
        return super.toFileString() + "," + canManageAdmins + "," + canViewReports;
    }
}
