package com.salon.review.module;


public class GuestReview extends Review {

    public GuestReview() {
        super();
    }

    public GuestReview(String id, String customerName, String billId, int rating, String comment) {
        super(id, customerName, billId, rating, comment);
    }

    @Override
    public String getDisplayName() {
        return "Guest - " + customerName;
    }

    @Override
    public String getReviewType() {
        return "GUEST";
    }

    @Override
    public void displayDetails() {
        System.out.println("--- Guest Review ---");
        super.displayDetails();
    }
}
