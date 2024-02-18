//package com.example.lsmsdb.GUI;
//
//import com.example.lsmsdb.Database.Movie.Movie;
//import com.example.lsmsdb.Database.StatisticsDAO;
//import com.example.lsmsdb.Database.SuggestionsDAO;
//import com.example.lsmsdb.HelloApplication;
//import javafx.application.Platform;
//import javafx.concurrent.Task;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Label;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import java.io.IOException;
//import java.util.List;
//
//public class SuggestionsController {
//
//    @FXML
//    private Label titleLabel;
//
//    @FXML
//    private VBox movieVBox;
//    public static void setAction(String action) {
//        SuggestionsController.action = action;
//    }
//
//    private static String action;
//    public void initialize(){
//        switch (action) {
//            case "newUsers" -> {
//                // movies based on the users they follow
//                titleLabel.setText("Recommended users based on your ratings");
//                List<String> userList = SuggestionsDAO.getSuggestedUsers();
//
//            }
//            case "newMoviesBasedOnFollowing" -> {
//                // movies based on the users they follow rating
//
//            }
//        }
//    }
//
//    private static Thread movieLoadingThread;
//
//    public void displayMovies(List<Movie> movieList) {
//        // Clear existing movie items
//        movieVBox.getChildren().clear();
//
//        // Create a new task
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                for (Movie movie : movieList) {
//                    if (isCancelled()) { // Check if the task is cancelled
//                        break; // Exit the loop if the task is cancelled
//                    }
//                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item.fxml"));
//                    try {
//                        HBox grid = fxmlLoader.load();
//                        MovieItemController mi = fxmlLoader.getController();
//                        mi.setData(movie);
//                        // Add the movie item to the VBox
//                        Platform.runLater(() -> movieVBox.getChildren().add(grid));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (Thread.currentThread().isInterrupted()) {
//                        break;
//                    }
//                }
//                return null;
//            }
//        };
//
//        // Start the background task
//        movieLoadingThread = new Thread(task);
//        movieLoadingThread.start();
//    }
//    public static void cancelMovieLoading() {
//        if (movieLoadingThread != null) {
//            movieLoadingThread.interrupt(); // Interrupt the thread
//
//        }
//    }
//}
