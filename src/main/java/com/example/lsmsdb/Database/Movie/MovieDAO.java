package com.example.lsmsdb.Database.Movie;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.Review.Review;
import com.example.lsmsdb.Database.User.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MovieDAO {
    static Integer MAX_NUMBER_MOVIES = 20;
    private static HashMap<Integer, String> genreNameById = new HashMap<>();

    public static List<Movie> getMoviesFromName(String titleInput){
        MongoCollection movieCollection = DatabaseMongoDB.getCollection("movie_tmdb");
        List<Movie> listMovies = new ArrayList<>();
        Pattern pattern = Pattern.compile(".*" + titleInput + ".*", Pattern.CASE_INSENSITIVE);

        Document searchQuery = new Document();
        searchQuery.put("title", pattern);


        try(MongoCursor cursorIterator = movieCollection.find(searchQuery).iterator()){
            while(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                listMovies.add(createMovie(doc));
            }
            return listMovies;

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static Movie getMovieById(String idInput){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie_tmdb");
        List<Movie> listMovies = new ArrayList<>();

        Document searchQuery = new Document();
        searchQuery.put("_id", idInput);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                Movie m = createMovie(doc);
//                Document doc_review = (Document) doc.get("review");
//                Review r = createReview(doc_review, m.getId());
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }
//    public static List<Movie> getMovieRevPage(){
//        fillGenreNameById();
//        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie_tmdb");
//        List<Movie> movieList = new ArrayList<>();
//        int count = 0;
//        try(MongoCursor cursorIterator = userCollection.find().iterator()){
//            while(cursorIterator.hasNext()){
//                count++;
//                Document doc = (Document) cursorIterator.next();
//                movieList.add(createMovie(doc));
//                List<Document> doc_review = (List<Document>) doc.get("reviews");
//                for (Document doc_rev : doc_review){
//
//                }
//                if (count > MAX_NUMBER_MOVIES) break;
//            }
//        }catch(MongoException me){
//            System.exit(-1);
//        }
//        return movieList;
//    }
    public static List<Movie> getMainPageMovies(){
        fillGenreNameById();
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie_tmdb");
        List<Movie> movieList = new ArrayList<>();
        int count = 0;
        try(MongoCursor cursorIterator = userCollection.find().iterator()){
            while(cursorIterator.hasNext()){
                count++;
                Document doc = (Document) cursorIterator.next();
                movieList.add(createMovie(doc));
                if (count > MAX_NUMBER_MOVIES) break;
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return movieList;
    }

    private static Movie createMovie(Document doc){
        try {
            String movieid = doc.getString("_id");
            String poster = getMoviePosterLink(doc.getString("poster"));
            String title = doc.getString("title");
            Integer year = getMovieYear(doc.getString("release_date"));
            Double rating = doc.getDouble("rating");
            List<String> genre = getMovieGenre((List<Integer>) doc.get("genre_ids"));
            Integer review = doc.getInteger("vote_count");
            String overview = doc.getString("overview");
            Double popularity = doc.getDouble("popularity");

            if (movieid == null || poster == null || title == null || year == null  || rating == null || genre == null || review == null ){
                return null;
            }
            Movie m = new Movie(movieid, title, year, poster, rating, genre, review, overview, popularity);
            return m;
        }
        catch (Exception e) {
            // Handle other exceptions
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String getMoviePosterLink(String link){
        return "https://image.tmdb.org/t/p/w500" + link;
    }

    private static List<String> getMovieGenre(List<Integer> genreIdList){
        List<String> genreNameList = new ArrayList<>();
        for (Integer id : genreIdList) {
            genreNameList.add(genreNameById.get(id));
        }
        return genreNameList;
    }

    private static Integer getMovieYear(String yearInput){
        String[] year = yearInput.split("-");
        return Integer.valueOf(year[0]);
    }

    private static void fillGenreNameById(){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("genre");

        try(MongoCursor cursorIterator = userCollection.find().iterator()) {
            while (cursorIterator.hasNext()) {
                Document doc = (Document) cursorIterator.next();
                genreNameById.put(doc.getInteger("id"), doc.getString("name"));
            }
        }
    }

//    private static String getDate(String timestampString){
//        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//
//        try {
//            System.out.println(timestampString);
//            Date timestampDate =  inputDateFormat.parse(timestampString);
//            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            String formattedDate = outputDateFormat.format(timestampDate);
//            return formattedDate;
//        } catch (ParseException e) {
//            System.out.println("Error parsing timestamp: " + e.getMessage());
//        }
//        return null;
//    }

//    private static Review createReview(Document doc_review, String movieid){
//        String username = doc_review.getString("username");
//        String timestamp = getDate(doc_review.getString("date"));
//        String content = doc_review.getString("content");
//        String profilepic = doc_review.getString("profilepic");
//        Double rating_user = doc_review.getDouble("rating");
//
//        if(rating_user == null) rating_user = 0.0;
//        Review r = new Review(username, content, MovieDAO.getMovieById(movieid), rating_user, timestamp, profilepic);
//        return r;
//    }


}
//Integer year = Integer.getInteger(doc.get("year").toString());
//                Integer id = Integer.getInteger(doc.get("movieid").toString());
//                if (year != null) {
//                    year.intValue();
//                } else year = 1000;
//                if (id != null) {
//                    id.intValue();
//                } else id = 1000;