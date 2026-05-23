package com.salon.review.other;

import com.salon.review.database.ReviewFileManager;
import com.salon.review.module.GuestReview;
import com.salon.review.module.Review;
import com.salon.review.module.VerifiedReview;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class ReviewManager {

    private static int totalReviewsLoaded = 0;

    private ReviewFileManager fileManager;
    private ReviewRegistry registry;

    public ReviewManager() {
        fileManager = new ReviewFileManager();
        registry = new ReviewRegistry();
    }

    @PostConstruct
    public void loadData() throws ReviewDataException {
        ArrayList<Review> list = fileManager.readAllReviews();
        registry.setReviewList(list);
        totalReviewsLoaded = list.size();
    }

    public static int getTotalReviewsLoaded() {
        return totalReviewsLoaded;
    }

    public Review addReview(ReviewForm form) throws ReviewDataException {
        String newId = generateNextId();
        Review review = createReviewObject(newId, form);
        fileManager.appendReview(review);
        registry.addReview(review);
        totalReviewsLoaded = registry.getSize();
        return review;
    }

    public ArrayList<Review> getAllReviews() throws ReviewDataException {
        reloadFromFile();
        return registry.getReviewList();
    }

    public ArrayList<Review> searchByBillId(String billId) throws ReviewDataException {
        reloadFromFile();
        return searchByBillId(billId, 0);
    }

    public ArrayList<Review> searchByBillId(String billId, int minimumRating) throws ReviewDataException {
        ArrayList<Review> result = new ArrayList<Review>();
        ArrayList<Review> all = registry.getReviewList();

        if (billId == null || billId.trim().isEmpty()) {
            for (int i = 0; i < all.size(); i++) {
                result.add(all.get(i));
            }
            return result;
        }

        String searchId = billId.trim();
        for (int i = 0; i < all.size(); i++) {
            Review review = all.get(i);
            boolean billMatch = review.getBillId().equalsIgnoreCase(searchId);
            boolean ratingMatch = minimumRating == 0 || review.getRating() >= minimumRating;
            if (billMatch && ratingMatch) {
                result.add(review);
            }
        }
        return result;
    }

    public Review findReviewById(String id) throws ReviewDataException {
        reloadFromFile();
        return registry.findById(id);
    }

    public double getAverageRating(String billId) throws ReviewDataException {
        ArrayList<Review> list = searchByBillId(billId);
        if (list.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getRating();
        }
        return (double) total / list.size();
    }

    public boolean updateReview(String id, ReviewForm form) throws ReviewDataException {
        reloadFromFile();
        Review oldReview = registry.findById(id);
        if (oldReview == null) {
            return false;
        }

        Review updatedReview = createReviewObject(id, form);
        ArrayList<Review> list = registry.getReviewList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                list.set(i, updatedReview);
                break;
            }
        }
        fileManager.saveAllReviews(list);
        return true;
    }

    public boolean deleteReview(String id) throws ReviewDataException {
        reloadFromFile();
        boolean removed = registry.removeReview(id);
        if (removed) {
            fileManager.saveAllReviews(registry.getReviewList());
            totalReviewsLoaded = registry.getSize();
        }
        return removed;
    }

    private void reloadFromFile() throws ReviewDataException {
        ArrayList<Review> list = fileManager.readAllReviews();
        registry.setReviewList(list);
        totalReviewsLoaded = list.size();
    }

    private Review createReviewObject(String id, ReviewForm form) {
        String name = form.getCustomerName();
        if (name != null && name.toUpperCase().startsWith("GUEST")) {
            return new GuestReview(id, name, form.getBillId(), form.getRating(), form.getComment());
        }
        return new VerifiedReview(id, name, form.getBillId(), form.getRating(), form.getComment());
    }

    private String generateNextId() throws ReviewDataException {
        reloadFromFile();
        int max = 0;
        ArrayList<Review> list = registry.getReviewList();
        for (int i = 0; i < list.size(); i++) {
            String id = list.get(i).getId();
            try {
                int num = Integer.parseInt(id.replaceAll("[^0-9]", ""));
                if (num > max) {
                    max = num;
                }
            } catch (NumberFormatException e) {
            }
        }
        return "R" + String.format("%03d", max + 1);
    }

    public static class ReviewDataException extends Exception {

        public ReviewDataException() {
            super();
        }

        public ReviewDataException(String message) {
            super(message);
        }
    }

    static class ReviewRegistry {

        private ArrayList<Review> reviewList;

        public ReviewRegistry() {
            reviewList = new ArrayList<Review>();
        }

        public ReviewRegistry(ArrayList<Review> reviewList) {
            this.reviewList = reviewList;
        }

        public ArrayList<Review> getReviewList() {
            return reviewList;
        }

        public void setReviewList(ArrayList<Review> reviewList) {
            this.reviewList = reviewList;
        }

        public void addReview(Review review) {
            reviewList.add(review);
        }

        public boolean removeReview(String id) {
            for (int i = 0; i < reviewList.size(); i++) {
                if (reviewList.get(i).getId().equals(id)) {
                    reviewList.remove(i);
                    return true;
                }
            }
            return false;
        }

        public Review findById(String id) {
            for (int i = 0; i < reviewList.size(); i++) {
                Review r = reviewList.get(i);
                if (r.getId().equals(id)) {
                    return r;
                }
            }
            return null;
        }

        public int getSize() {
            return reviewList.size();
        }

        public boolean isEmpty() {
            return reviewList.isEmpty();
        }
    }

    static class ReviewForm {

        private String id;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotBlank(message = "Bill ID is required")
        private String billId;

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        private Integer rating = 5;

        @NotBlank(message = "Comments are required")
        @Size(max = 500, message = "Comments must be at most 500 characters")
        private String comment;

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
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
