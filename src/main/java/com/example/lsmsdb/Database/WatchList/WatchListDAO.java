package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class WatchListDAO {
    public static void addMovieToUserWatchList(String username, int id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        Document searchQuery = new Document();
        searchQuery.put("username", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$addToSet", new Document("watchlist", id));
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
        }catch(MongoException me){
            System.exit(-1);
        }

    }

    public static List<Movie> getMoviesFromWatchList(){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        List<Integer> idList;
        List<Movie> watchList = new ArrayList<>();
        Document searchQuery = new Document();
        searchQuery.put("username", User.getUsername());

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                System.out.println(doc);
                idList = (List<Integer>) doc.get("watchlist");
                if (idList != null ){

                    for (Integer id : idList){
                        watchList.add(MovieDAO.getMovieById(id));
                    }
                }
                return watchList;
                //movieList.add(new Movie(doc.getInteger("movieid"), doc.getString("title"), doc.getInteger("year"), doc.getString("poster"), doc.getDouble("rating"), doc.getString("genre")));
            }
        }catch(MongoException me){
            System.exit(-1);
        }

        return null;
    }

    public static void removeMovieFromUserWatchList(String username, int id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        Document searchQuery = new Document();
        searchQuery.put("username", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$pull", new Document("watchlist", id));
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
        }catch(MongoException me){
            System.exit(-1);
        }

    }

    public static boolean checkIfMovieInWatchList(String username, int id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        List<Integer> idList;
        List<Movie> watchList = new ArrayList<>();
        Document searchQuery = new Document();
        searchQuery.put("username", User.getUsername());

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                System.out.println(doc);
                idList = (List<Integer>) doc.get("watchlist");
                if (idList != null ){
                    if (idList.contains(id)){
                        return true;
                    } else return false;
                }
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return false;
    }
}
