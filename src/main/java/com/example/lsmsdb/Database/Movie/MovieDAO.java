package com.example.lsmsdb.Database.Movie;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.User.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    //TODO make a good search engine to retrieve multiple movies from name
    public static List<Movie> getMoviesFromName(String title){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");
        List<Movie> listMovies = new ArrayList<>();

        Document searchQuery = new Document();
        searchQuery.put("title", title);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                Movie m = new Movie(doc.getInteger("movieid"), doc.getString("title"), doc.getInteger("year"), doc.getString("poster"), doc.getDouble("rating"), doc.getString("genre"));
                listMovies.add(m);
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

                Movie m = new Movie(doc.getInteger("movieid"), doc.getString("title"), doc.getInteger("year"), doc.getString("poster"), doc.getDouble("rating"), doc.getString("genre"));
                return m;
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static List<Movie> getListMovies(){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("movie");
        List<Movie> movieList = new ArrayList<>();

        try(MongoCursor cursorIterator = userCollection.find().iterator()){
            while(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                movieList.add(new Movie(doc.getInteger("movieid"), doc.getString("title"), doc.getInteger("year"), doc.getString("poster"), doc.getDouble("rating"), doc.getString("genre")));
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return movieList;
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