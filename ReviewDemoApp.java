package com.salon.review.other;

import com.salon.review.module.Review;

import java.util.ArrayList;


public class ReviewDemoApp {

    public static void main(String[] args) {
        try {
            ReviewManager manager = new ReviewManager();
            manager.loadData();

            System.out.println("===== All Reviews =====");
            printReviewList(manager.getAllReviews());

            System.out.println("\n===== Search Bill BILL-1001 =====");
            printReviewList(manager.searchByBillId("BILL-1001"));

            System.out.println("\nTotal reviews loaded : " + ReviewManager.getTotalReviewsLoaded());
        } catch (ReviewManager.ReviewDataException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void printReviewList(ArrayList<Review> list) {
        for (int i = 0; i < list.size(); i++) {
            Review review = list.get(i);
            review.displayDetails();
            System.out.println("Display Name : " + review.getDisplayName());
            System.out.println("------------------------");
        }
    }
}
