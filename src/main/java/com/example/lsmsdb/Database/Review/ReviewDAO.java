package com.example.lsmsdb.Database.Review;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.DatabaseNeo4j;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.User.UserDAO;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.exceptions.Neo4jException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

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
                    System.out.println(document.get("rating"));
                    Number rating;
                    Object ratingObj = document.get("rating");


                    if (ratingObj instanceof Integer) {
                        rating = (Integer) ratingObj;
                        rating = 0.0;
                    } else if (ratingObj instanceof Double) {
                        rating = (Double) ratingObj;
                    } else {
                        // Handle unexpected types or null values
                        rating = 0.0; // Or set a default value or throw an exception
                    }


                    Review r = new Review(username, content, movieid, Double.valueOf((Double) rating), timestamp, profilepic);
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
            //System.out.println(timestampString);
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
                addReviewNeo4j(username, movieid, rating);

            }
        } catch (MongoException me) {
            System.exit(-1);
        }
    }

    public static void addReviewNeo4j(String username, String movieid, Double rating) {
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (u:user) WHERE u.username=$username " +
                                "MATCH (m:Movie) WHERE m.movieid=$movieid " +
                                "CREATE (u)-[rel:RATED {rate:rate($rating)}]->(m)",
                        parameters("username", username, "movieid", movieid, "rating", rating));
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
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


