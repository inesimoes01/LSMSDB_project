package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.DatabaseNeo4j;
import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.GUI.UserController;
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

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class WatchListDAO {

    private List<Movie> watchListFromUser = new ArrayList<>();

    public List<Movie> initializeWatchList(String username){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");
        Document searchQuery = new Document();
        searchQuery.put("_id", username);
        watchListFromUser = new ArrayList<>();
        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                List<Document> doc_watch = (List<Document>) doc.get("watchlist");
                if (doc_watch != null){
                    for (Document document : doc_watch){
                        String title = document.getString("title");
                        String poster = document.getString("poster");
                        String movieid = document.getString("_id");
                        Movie m = new Movie(movieid, title, poster);
                        watchListFromUser.add(m);
                    }
                }
                System.out.println(watchListFromUser);
                return watchListFromUser;
                //watchListFromUser = (List<String>) doc.get("watchlist");
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public void addMovieToUserWatchList(String username, String id, String title, String poster){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");
        Document searchQuery = new Document();
        searchQuery.put("_id", username);

        Document movieInfo = new Document("_id", id)
                .append("title", title)
                .append("poster", poster);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$addToSet", new Document("watchlist", movieInfo));
            Movie m = new Movie(id, title, poster);
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
            watchListFromUser.add(m);
        }catch(MongoException me){
            System.exit(-1);
        }

    }

    public void removeMovieFromUserWatchList(String username, String id){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");
        Document searchQuery = new Document();
        searchQuery.put("_id", UserController.getLoggedInUser().getUsername());

        Document movieInfo = new Document("_id", id);
        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            Document updateDocument = new Document("$pull", new Document("watchlist", movieInfo));
            userCollection.updateOne(searchQuery, updateDocument, new UpdateOptions().upsert(true));
            watchListFromUser.remove((Object) id);
        }catch(MongoException me){
            System.exit(-1);
        }
    }

    public List<Movie> getMoviesFromWatchList(){
        return watchListFromUser;
    }

    public List<Movie> getMoviesFromWatchList(List<Movie> watchListFromUser){
        List<Movie> list = new ArrayList<>();

        if (watchListFromUser == null){
            return null;
        }else {
            for(Movie movie : watchListFromUser){
                list.add(MovieDAO.getMovieById(movie.getId()));
            }
            return list;
        }
    }

    public boolean checkIfMovieInWatchList(String id){
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