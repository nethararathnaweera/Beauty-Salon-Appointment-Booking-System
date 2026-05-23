package com.salon.review.module;


public class VerifiedReview extends Review {

    public VerifiedReview() {
        super();
    }

    public VerifiedReview(String id, String customerName, String billId, int rating, String comment) {
        super(id, customerName, billId, rating, comment);
    }

    @Override
    public String getDisplayName() {
        return customerName;
    }

    @Override
    public String getReviewType() {
        return "VERIFIED";
    }

    @Override
    public void displayDetails() {
        System.out.println("--- Verified Review ---");
        super.displayDetails();
    }
}
