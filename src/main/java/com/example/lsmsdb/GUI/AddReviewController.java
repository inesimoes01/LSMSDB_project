package com.example.lsmsdb.GUI;
import com.example.lsmsdb.Database.Review.ReviewDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AddReviewController {
    @FXML
    private Label ratingLabel;

    @FXML
    private Slider ratingSlider;

    @FXML
    private Button reviewMovieButton;

    @FXML
    private TextArea reviewText;

    @FXML
    void addReviewToMovie(ActionEvent event) throws IOException {
        ReviewDAO.addReview(UserController.getLoggedInUser().getUsername(), MovieController.getCurrentMovie().getId(), UserController.getLoggedInUser().getProfileImage(), reviewText.getText(), Double.parseDouble(ratingLabel.getText()));
        HelloApplication.changeScene("movie-page.fxml");
    }

    @FXML
    void goToMoviePage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("movie-page.fxml");
    }

    public void initialize(){
        // Create a DecimalFormat object with a period as the decimal separator
        DecimalFormat decimalFormat = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.ENGLISH));

        // Listen for changes in the slider value
        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Format the new value with the DecimalFormat
                String formattedValue = decimalFormat.format(newValue.doubleValue());

                // Update the label text with the formatted value
                ratingLabel.setText(formattedValue);
            }
        });
    }



}
