package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.GUI.UserController;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class WatchListDAO {

    private static List<Integer> watchListFromUser = new ArrayList<>();

    public static void initializeWatchList(){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        Document searchQuery = new Document();
        searchQuery.put("username", UserController.getLoggedInUser().getUsername());

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                watchListFromUser = (List<Integer>) doc.get("watchlist");
            }
        }catch(MongoException me){
            System.exit(-1);
        }
    }

    public static void addMovieToUserWatchList(int id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        Document searchQuery = new Document();
        searchQuery.put("username", UserController.getLoggedInUser().getUsername());

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$addToSet", new Document("watchlist", id));
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
            watchListFromUser.add(id);
        }catch(MongoException me){
            System.exit(-1);
        }

    }

    public static void removeMovieFromUserWatchList(int id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
        Document searchQuery = new Document();
        searchQuery.put("username", UserController.getLoggedInUser().getUsername());

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$pull", new Document("watchlist", id));
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
            watchListFromUser.remove((Object) id);
        }catch(MongoException me){
            System.exit(-1);
        }

    }

    public static List<Movie> getMoviesFromWatchList(){
        List<Movie> list = new ArrayList<>();

        for(Integer id : watchListFromUser){
            list.add(MovieDAO.getMovieById(id));
        }
        return list;
    }

    public static boolean checkIfMovieInWatchList(int id){
        return watchListFromUser.contains(id);
    }
}
//        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");
//        List<Integer> idList;
//        List<Movie> watchList = new ArrayList<>();
//        Document searchQuery = new Document();
//        searchQuery.put("username", User.getUsername());



//        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
//            if(cursorIterator.hasNext()){
//                Document doc = (Document) cursorIterator.next();
//                System.out.println("trying to retrieve watchlist " + doc);
//                idList = (List<Integer>) doc.get("watchlist");
//                if (idList != null ){
//                    if (idList.contains(id)){
//                        return true;
//                    } else return false;
//                }
//            }
//        }catch(MongoException me){
//            System.exit(-1);
//        }
//        return false;