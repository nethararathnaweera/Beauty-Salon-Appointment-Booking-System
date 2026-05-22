package com.example.demo.entity;

public class Customer extends Person {

    private String membershipType;

    public Customer() {
    }

    @Override
    public String getContactInfo() {

        return super.getContactInfo()
                + " | Membership: "
                + membershipType;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(
            String type) {

        this.membershipType = type;
    }
}