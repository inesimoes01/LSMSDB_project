package com.example.lsmsdb.Database.Review;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.User.UserDAO;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAO {

    public static List<Review> getReviewsFromMovie(String movieid){
        List<Review> revList = new ArrayList<>();
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");

        Document searchQuery = new Document();
        searchQuery.put("_id", movieid);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                List<Document> doc_review = (List<Document>) doc.get("reviews");
                for (Document document : doc_review){
                    String username = document.getString("username");
                    String timestamp = getDate(document.getString("date"));
                    String content = document.getString("content");
                    String profilepic = document.getString("profilepic");
                    Double rating = document.getDouble("rating");
                    if(rating == null) rating = 0.0;

                    Review r = new Review(username, content, movieid, rating, timestamp, profilepic);
                    revList.add(r);
                }
                return revList;
            }
            return revList;
        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    private static String getDate(String timestampString){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            System.out.println(timestampString);
            Date timestampDate =  inputDateFormat.parse(timestampString);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = outputDateFormat.format(timestampDate);
            return formattedDate;
        } catch (ParseException e) {
            System.out.println("Error parsing timestamp: " + e.getMessage());
        }
        return null;
    }

    public static void addReview(String username, String movieid, String poster, String reviewText, Double rating) {
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");

        Document searchQuery = new Document();
        searchQuery.put("_id", movieid);

        Document movieInfo = new Document("username", username)
                .append("profilepic", poster)
                .append("rating", rating)
                .append("date", getTodayDate())
                .append("content", reviewText);

        try (MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()) {
            if (cursorIterator.hasNext()) {
                Document doc = (Document) cursorIterator.next();

                Document updateDocument = new Document("$addToSet", new Document("reviews", movieInfo));
                userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));

            }
        } catch (MongoException me) {
            System.exit(-1);
        }
    }

    private static String getTodayDate(){
        LocalDateTime now = LocalDateTime.now();

        // Define the date-time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Format the current date and time using the formatter
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}


