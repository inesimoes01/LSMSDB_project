package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Review.Review;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ReviewItemController {

    private User curr;

    @FXML
    private Label rating;
    @FXML
    private ImageView profilepic;
    @FXML
    private Label review;
    @FXML
    private Label timestamp;
    @FXML
    private Hyperlink usernameLink;

    public void setData(Review rev){
//        if (rev.getProfilepic() == null){
//            Image profile = new Image("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png");
//            profilepic.setImage(profile);
//        }else {
//
//        }
        Image profile = new Image(rev.getProfilepic());
        profilepic.setImage(profile);
        if (rev.getRating() == 0.0){
            rating.setText(" ");
        }else rating.setText(rev.getRating().toString());

        review.setText(rev.getReviewText().replaceAll("\\.\\s+", ". "));
        usernameLink.setText(rev.getUsername());
        timestamp.setText(rev.getTimestamp());
        curr = rev.getReviewUser(rev.getUsername());
        //System.out.println();
    }
    @FXML
    void goToUserProfile(ActionEvent event) throws IOException {
        UserProfilePageController.setData(curr);
        HelloApplication.changeScene("user-profile.fxml");
    }




}
