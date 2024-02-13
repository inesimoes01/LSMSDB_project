package com.example.lsmsdb.Database.Movie;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.User.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieDAO {
    static Integer MAX_NUMBER_MOVIES = 20;
    private static HashMap<Integer, String> genreNameById = new HashMap<>();

    //TODO make a good search engine to retrieve multiple movies from name
    public static List<Movie> getMoviesFromName(String titleInput){
        MongoCollection movieCollection = DatabaseMongoDB.getCollection("movie");
        List<Movie> listMovies = new ArrayList<>();

        Document searchQuery = new Document();
        searchQuery.put("title", titleInput);


        try(MongoCursor cursorIterator = movieCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                listMovies.add(createMovie(doc));
                return listMovies;
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static Movie getMovieById(int idInput){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");
        List<Movie> listMovies = new ArrayList<>();

        Document searchQuery = new Document();
        searchQuery.put("movieid", idInput);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                return createMovie(doc);
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static List<Movie> getMainPageMovies(){
        fillGenreNameById();
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");
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
            Integer movieid = doc.getInteger("id");
            String poster = getMoviePosterLink(doc.getString("poster_path"));
            String title = doc.getString("title");
            Integer year = getMovieYear(doc.getString("release_date"));
            System.out.println("after" + year);
            Double rating = doc.getDouble("vote_average");
            List<String> genre = getMovieGenre((List<Integer>) doc.get("genre_ids"));

            if (movieid == null || poster == null || title == null || year == null  || rating == null || genre == null ){
                return null;
            }
            Movie m = new Movie(movieid, title, year, poster, rating, genre);
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


}
//Integer year = Integer.getInteger(doc.get("year").toString());
//                Integer id = Integer.getInteger(doc.get("movieid").toString());
//                if (year != null) {
//                    year.intValue();
//                } else year = 1000;
//                if (id != null) {
//                    id.intValue();
//                } else id = 1000;