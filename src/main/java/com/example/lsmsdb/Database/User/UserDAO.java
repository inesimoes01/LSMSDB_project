package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {

    public static User checkUsernameCredentials(String username, String password){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("username", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                if(password.equals(doc.get("password").toString())){
                    return new User(username, doc.getString("name"), (List<Integer>) doc.get("watchlist"));
                }
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static boolean checkUsernameExists(String username){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("username", username);

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
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document newUser = new Document("username", username)
                .append("password", password)
                .append("name", fullname);

        try {
            userCollection.insertOne(newUser);

            return true;
        } catch (MongoException me) {
            // Handle MongoDB exception
            me.printStackTrace();
            return false;
        }
    }

    public static User getUserFromUsername(String usernameInput){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("username", usernameInput);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                String username = doc.getString("username");
                String fullname = doc.getString("name");
                List<Integer> watchlist = (List<Integer>) doc.get("watchlist");
                return new User(username, fullname, watchlist);
            }
        }catch(MongoException me){
            System.exit(-1);
        }
        return null;
    }

    public static boolean followUser(){

        return false;
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
//            System.out.println(document.toJson());
//            count++;
//        }
//
//        //System.out.println("This si collection " + collection.toString());
//        return count > 0;
