package com.salon.review.database;

import com.salon.review.module.Review;
import com.salon.review.other.ReviewManager.ReviewDataException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class ReviewFileManager {

    public static final String FILE_NAME = "data/reviews.txt";
    private String filePath;

    public ReviewFileManager() {
        filePath = FILE_NAME;
        checkFile();
    }

    private void checkFile() {
        File file = new File(filePath);
        File folder = file.getParentFile();
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                copySampleData(file);
            } catch (IOException e) {
                System.out.println("File create error : " + e.getMessage());
            }
        }
    }

    private void copySampleData(File file) {
        try (InputStream in = getClass().getResourceAsStream("/data/reviews.txt")) {
            if (in != null) {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.out.println("Sample data copy error : " + e.getMessage());
        }
    }

    public ArrayList<Review> readAllReviews() throws ReviewDataException {
        ArrayList<Review> list = new ArrayList<Review>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            int lineNo = 0;

            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Review review = Review.createFromLine(line);
                    list.add(review);
                } catch (ReviewDataException e) {
                    throw new ReviewDataException("Error at line " + lineNo + " : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new ReviewDataException("Cannot read file : " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Reader close error : " + e.getMessage());
                }
            }
        }
        return list;
    }

    public void appendReview(Review review) throws ReviewDataException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath, true);
            writer.write(review.toFileLine());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            throw new ReviewDataException("Cannot append review : " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Writer close error : " + e.getMessage());
                }
            }
        }
    }

    public void saveAllReviews(ArrayList<Review> reviewList) throws ReviewDataException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath, false);
            for (int i = 0; i < reviewList.size(); i++) {
                Review review = reviewList.get(i);
                writer.write(review.toFileLine());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ReviewDataException("Cannot save file : " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Writer close error : " + e.getMessage());
                }
            }
        }
    }
}
