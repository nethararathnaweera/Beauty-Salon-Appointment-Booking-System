package model;

public class RegularUser extends User {

    private String phoneNumber;

    public RegularUser(int id, String username, String password,
                       String email, String phoneNumber) {
        super(id, username, password, email, "CUSTOMER");
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getDashboardURL() {
        return "/customer/dashboard";
    }

    // getter/setter...
}