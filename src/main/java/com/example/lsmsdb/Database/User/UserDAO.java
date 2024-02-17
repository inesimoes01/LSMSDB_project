package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.DatabaseNeo4j;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.GUI.UserController;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.scene.chart.PieChart;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.neo4j.driver.Values.parameters;

public class UserDAO {

    public static User checkUsernameCredentials(String username, String password){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");

        Document searchQuery = new Document();
        searchQuery.put("_id", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                if(password.equals(doc.get("password").toString())){
                    WatchListDAO w = new WatchListDAO();
                    return new User(username, doc.getString("fullname"), doc.getString("profilepic"), w.initializeWatchList(username));
                }
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static boolean checkUsernameExists(String username){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");

        Document searchQuery = new Document();
        searchQuery.put("_id", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                return true;
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return false;
    }

    public static boolean createUser(String username, String password, String fullname){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");

        Document newUser = new Document("_id", username)
                .append("password", password)
                .append("fullname", fullname)
                .append("profilepic", User.getDefaultProfileImage());

        try {
            userCollection.insertOne(newUser);
        } catch (MongoException me) {
            // Handle MongoDB exception
            me.printStackTrace();
            return false;
        }

        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run(
                        "CREATE (ee:user{username: $username})",
                        parameters("username", username));
                return null;
            });

        }catch(Neo4jException ne){ //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return true;
    }

    public static User getUserFromUsername(String usernameInput){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("user");

        Document searchQuery = new Document();
        searchQuery.put("_id", usernameInput);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                String username = doc.getString("_id");
                String fullname = doc.getString("fullname");
                String profilepic = doc.getString("profilepic");

                List<String> watchlist = (List<String>) doc.get("watchlist");
                WatchListDAO w = new WatchListDAO();
                return new User(username, fullname, profilepic, w.initializeWatchList(username));
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static void followUser(String followeeId){
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run(
                        "MATCH (u:user) WHERE u.username=$follower " +
                                "MATCH (u2:user) WHERE u2.username=$followee " +
                                "CREATE (u)-[rel:FOLLOWS {since:date($date)}]->(u2)",
                        parameters("follower", UserController.getLoggedInUser().getUsername(), "followee", followeeId,
                                "date", java.time.LocalDate.now().toString()));
                return null;
            });

        }catch(Neo4jException ne){ //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
    }
    public static void unfollowUser(String followeeId){
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run(
                        "MATCH (u:user)-[rel:FOLLOWS]->(u2:user) WHERE u.username = $follower AND u2.username=$followee " +
                                "DELETE rel",
                        parameters("follower", UserController.getLoggedInUser().getUsername(), "followee", followeeId));
                return null;
            });

        }catch(Neo4jException ne){ //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }

    }

    public static List<String> getUserFollowing(String username) {
        List<String> userFollowers = new ArrayList<>();
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (u:user {username: $username})-[rel:FOLLOWS]->(following:user) RETURN following.username",
                        parameters("username", username));
                while (res.hasNext()) {
                    Record rec = res.next();
                    userFollowers.add(rec.get("following.username").asString());
                }
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return userFollowers;
    }

    public static List<String> getUserFollowers(String username) {
        List<String> userFollowers = new ArrayList<>();
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (u:user)-[rel:FOLLOWS]->(follower:user {username: $username})  RETURN u.username",
                        parameters("username", username));
                while (res.hasNext()) {
                    Record rec = res.next();
                    userFollowers.add(rec.get("u.username").asString());
                }
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return userFollowers;
    }

}
//DatabaseMongoDB.getClientMongoDB().startSession();
//        System.out.println("WE tried ");
//        MongoCollection<Document> collection = DatabaseMongoDB.getClientMongoDB().getDatabase("teste").getCollection("users");
//        Pattern pattern1 = Pattern.compile(".*" + username + ".*", Pattern.CASE_INSENSITIVE);
//        Bson filter = Filters.regex("_id", pattern1);
//        System.out.println("WE tried2 ");
//        MongoCursor<Document> cursor = collection.find(eq("_id", username)).limit(1).iterator();
//        System.out.println("WE tried 3");
//        int count = 0;
//
//        while (cursor.hasNext()) {
//            Document document = cursor.next();
//            // Process the retrieved document
//            System.out.println(document.toJson()) ;
//            count++;
//        }
//
//        //System.out.println("This si collection " + collection.toString());
//        return count > 0;
