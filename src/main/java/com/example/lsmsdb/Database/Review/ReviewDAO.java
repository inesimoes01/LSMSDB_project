package com.example.lsmsdb.Database.Review;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.User.UserDAO;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAO {

    public static List<Review> getReviewsFromMovie(int id){
        //TODO change later to be inside the movie
        List<Review> revList = new ArrayList<>();
        MongoCollection userCollection = DatabaseMongoDB.getCollection("review");


        try(MongoCursor cursorIterator = userCollection.find().iterator()){
            while(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                Document doc_author = (Document) doc.get("author_details");
                //TODO change when reviews are correct
                //User u = UserDAO.getUserFromUsername(doc.getString("author_details.username"));

                User u = new User(doc_author.getString("username"), doc.getString("author"), new ArrayList<>());
                String timestamp = getDate(doc.getString("created_at"));
                String content = doc.getString("content");
                Double rating = doc_author.getDouble("rating");

                if(rating == null) rating = 0.0;

                Review r = new Review(u, content, MovieDAO.getMovieById(id), rating, timestamp);
                //System.out.println(u.getUsername() + " " + content  + " " + rating);
                revList.add(r);
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

            Date timestampDate =  inputDateFormat.parse(timestampString);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = outputDateFormat.format(timestampDate);
            return formattedDate;
        } catch (ParseException e) {
            System.out.println("Error parsing timestamp: " + e.getMessage());
        }
        return null;
    }

}
