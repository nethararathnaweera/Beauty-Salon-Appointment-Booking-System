package model;

// This is an abstract class - you cannot create "new User()"
// You can only create AdminUser or RegularUser
public abstract class User {

    // ENCAPSULATION: private fields, accessed via getters/setters
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // "ADMIN" or "CUSTOMER"

    // Constructor
    public User(int id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // ... add getters/setters for all fields

    // POLYMORPHISM: abstract method - each subclass MUST implement this
    public abstract String getDashboardURL();

    // Method to convert user to text format for file storage
    public String toFileString() {
        return id + "," + username + "," + password + "," + email + "," + role;
    }
}
