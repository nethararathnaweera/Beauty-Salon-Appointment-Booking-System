package com.salon.review.module;

import com.salon.review.other.ReviewManager.ReviewDataException;


public abstract class Review implements Printable {

    protected String id;
    protected String customerName;
    protected int rating;
    protected String comment;
    protected BillDetail billDetail;

    public Review() {
        id = "";
        customerName = "";
        rating = 1;
        comment = "";
        billDetail = new BillDetail();
    }

    public Review(String id, String customerName, String billId, int rating, String comment) {
        this.id = id;
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
        billDetail = new BillDetail(billId);
    }

    public void assignDetails(String id, String customerName, String billId, int rating, String comment) {
        if (rating < 1) {
            rating = 1;
        }
        if (rating > 5) {
            rating = 5;
        }
        this.id = id;
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
        billDetail.setBillId(billId);
    }

    public void displayDetails() {
        System.out.println("Review ID : " + id);
        System.out.println("Customer Name : " + customerName);
        billDetail.displayDetails();
        System.out.println("Rating : " + rating);
        System.out.println("Comments : " + comment);
        System.out.println("Reviewer Type : " + getReviewType());
    }

    @Override
    public void printDetails() {
        displayDetails();
    }

    public abstract String getDisplayName();

    public abstract String getReviewType();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillId() {
        return billDetail.getBillId();
    }

    public void setBillId(String billId) {
        billDetail.setBillId(billId);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1) {
            this.rating = 1;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = rating;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BillDetail getBillDetail() {
        return billDetail;
    }

    public String toFileLine() {
        return id + "," + customerName + "," + getBillId() + "," + rating + ","
                + cleanText(comment) + "," + getReviewType();
    }

    private String cleanText(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ").replace("\n", " ").trim();
    }

    public static Review createFromLine(String line) throws ReviewDataException {
        if (line == null || line.trim().isEmpty()) {
            throw new ReviewDataException("Empty line in file");
        }
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new ReviewDataException("Invalid line : " + line);
        }

        String reviewId = parts[0].trim();
        String name = parts[1].trim();
        String billId = parts[2].trim();
        int rate = Integer.parseInt(parts[3].trim());
        String comm = parts[4].trim();
        String type = parts.length > 5 ? parts[5].trim() : "VERIFIED";

        Review review;
        if (type.equalsIgnoreCase("GUEST")) {
            review = new GuestReview(reviewId, name, billId, rate, comm);
        } else {
            review = new VerifiedReview(reviewId, name, billId, rate, comm);
        }
        return review;
    }
}
