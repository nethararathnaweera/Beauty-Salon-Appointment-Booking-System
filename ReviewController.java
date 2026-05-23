package com.salon.review.other;

import com.salon.review.module.Review;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ReviewController {

    private final ReviewManager reviewManager;

    public ReviewController(ReviewManager reviewManager) {
        this.reviewManager = reviewManager;
    }

    @ModelAttribute("editMode")
    public boolean editModeDefault() {
        return false;
    }

    @ModelAttribute("pageTitle")
    public String pageTitleDefault() {
        return "New review";
    }

    @ModelAttribute("stars")
    public List<Integer> stars() {
        ArrayList<Integer> starList = new ArrayList<Integer>();
        starList.add(1);
        starList.add(2);
        starList.add(3);
        starList.add(4);
        starList.add(5);
        return starList;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/submit")
    public String showSubmitForm(Model model) {
        model.addAttribute("reviewForm", new ReviewManager.ReviewForm());
        model.addAttribute("editMode", false);
        model.addAttribute("pageTitle", "New review");
        return "review";
    }

    @PostMapping("/reviews/submit")
    public String submitReview(@Valid @ModelAttribute("reviewForm") ReviewManager.ReviewForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            model.addAttribute("pageTitle", "New review");
            model.addAttribute("reviewForm", form);
            return "review";
        }
        try {
            Review created = reviewManager.addReview(form);
            session.setAttribute("currentUser", form.getCustomerName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Review saved. ID : " + created.getId());
            return "redirect:/reviews";
        } catch (ReviewManager.ReviewDataException e) {
            bindingResult.reject("file", e.getMessage());
            model.addAttribute("editMode", false);
            model.addAttribute("pageTitle", "New review");
            model.addAttribute("reviewForm", form);
            return "review";
        }
    }

    @GetMapping("/reviews")
    public String listReviews(@RequestParam(required = false) String billId, Model model, HttpSession session) {
        model.addAttribute("reviews", new ArrayList<ReviewView>());
        model.addAttribute("selectedBillId", billId != null ? billId : "");
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        try {
            ArrayList<Review> reviewList = reviewManager.searchByBillId(billId);
            model.addAttribute("reviews", ReviewView.fromList(reviewList));
            if (billId != null && !billId.isBlank()) {
                model.addAttribute("averageRating", reviewManager.getAverageRating(billId));
            } else {
                model.addAttribute("averageRating", 0.0);
            }
        } catch (ReviewManager.ReviewDataException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "review-list";
    }

    @GetMapping("/reviews/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes,
                               HttpSession session) {
        try {
            Review review = reviewManager.findReviewById(id);
            if (review == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Review not found");
                return "redirect:/reviews";
            }
            String currentUser = (String) session.getAttribute("currentUser");
            if (currentUser == null || !currentUser.equals(review.getCustomerName())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You can only edit your own reviews");
                return "redirect:/reviews";
            }
            ReviewManager.ReviewForm form = new ReviewManager.ReviewForm();
            form.setId(review.getId());
            form.setCustomerName(review.getCustomerName());
            form.setBillId(review.getBillId());
            form.setRating(review.getRating());
            form.setComment(review.getComment());
            model.addAttribute("reviewForm", form);
            model.addAttribute("editMode", true);
            model.addAttribute("pageTitle", "Edit Review");
            return "review";
        } catch (ReviewManager.ReviewDataException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reviews";
        }
    }

    @PostMapping("/reviews/edit/{id}")
    public String updateReview(@PathVariable String id,
                               @Valid @ModelAttribute("reviewForm") ReviewManager.ReviewForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        try {
            Review existing = reviewManager.findReviewById(id);
            if (existing == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Review not found");
                return "redirect:/reviews";
            }
            if (currentUser == null || !currentUser.equals(existing.getCustomerName())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You can only edit your own reviews");
                return "redirect:/reviews";
            }
        } catch (ReviewManager.ReviewDataException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reviews";
        }
        form.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            model.addAttribute("pageTitle", "Edit Review");
            model.addAttribute("reviewForm", form);
            return "review";
        }
        try {
            boolean updated = reviewManager.updateReview(id, form);
            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "Review updated");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Review not found");
            }
            return "redirect:/reviews";
        } catch (ReviewManager.ReviewDataException e) {
            bindingResult.reject("file", e.getMessage());
            model.addAttribute("editMode", true);
            model.addAttribute("pageTitle", "Edit Review");
            model.addAttribute("reviewForm", form);
            return "review";
        }
    }

    @GetMapping("/reviews/moderate")
    public String moderatePanel(Model model) {
        model.addAttribute("reviews", new ArrayList<ReviewView>());
        try {
            ArrayList<Review> list = reviewManager.getAllReviews();
            model.addAttribute("reviews", ReviewView.fromList(list));
        } catch (ReviewManager.ReviewDataException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "review-moderate";
    }

    @PostMapping("/reviews/moderate/delete/{id}")
    public String deleteReview(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            if (reviewManager.deleteReview(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Review deleted");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Review not found");
            }
        } catch (ReviewManager.ReviewDataException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reviews/moderate";
    }

    private static class ReviewView {

        private String id;
        private String customerName;
        private String billId;
        private int rating;
        private String comment;
        private String reviewType;
        private String displayName;

        public ReviewView(Review review) {
            this.id = review.getId();
            this.customerName = review.getCustomerName();
            this.billId = review.getBillId();
            this.rating = review.getRating();
            this.comment = review.getComment();
            this.reviewType = review.getReviewType();
            this.displayName = review.getDisplayName();
        }

        public static ArrayList<ReviewView> fromList(ArrayList<Review> reviews) {
            ArrayList<ReviewView> views = new ArrayList<ReviewView>();
            for (int i = 0; i < reviews.size(); i++) {
                views.add(new ReviewView(reviews.get(i)));
            }
            return views;
        }

        public String getId() {
            return id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getBillId() {
            return billId;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public String getReviewType() {
            return reviewType;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @ControllerAdvice
    static class ReviewExceptionHandler {

        @ExceptionHandler(BindException.class)
        public String handleBind(BindException ex, RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fill all fields correctly.");
            return "redirect:/reviews/submit";
        }

        @ExceptionHandler(ReviewManager.ReviewDataException.class)
        public String handleReviewData(ReviewManager.ReviewDataException ex, Model model) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error";
        }
    }
}
