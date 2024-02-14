package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Review.Review;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.io.IOException;

public class ReviewItemController {

    @FXML
    private Label rating;
    @FXML
    private Label review;
    @FXML
    private Label timestamp;
    @FXML
    private Hyperlink usernameLink;

    public void setData(Review rev){
        rating.setText(rev.getRating().toString());
        review.setText(rev.getReviewText().replaceAll("\\.\\s+", ". "));
        usernameLink.setText(rev.getReviewUser().getUsername());
        timestamp.setText(rev.getTimestamp());
    }

    @FXML
    void goToUserProfile(ActionEvent event) throws IOException {
        HelloApplication.changeScene("user-profile.fxml");
    }


}
