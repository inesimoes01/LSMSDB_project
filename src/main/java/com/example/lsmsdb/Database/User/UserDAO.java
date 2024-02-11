package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {

    public static boolean checkUsernameCredentials(String username, String password){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("username", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                if(password.equals(doc.get("password").toString())){
                    User.setUser(username, doc.get("name").toString());
                    User.setLoggedIn(true);
                    return true;
                }
            }

        }catch(MongoException me){
            System.exit(-1);
        }
        return false;
    }

    public static boolean checkUsernameExists(String username){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("username", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                // username exists
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
            User.setUser(username, fullname);
            User.setLoggedIn(true);
            return true;
        } catch (MongoException me) {
            // Handle MongoDB exception
            me.printStackTrace();
            return false;
        }
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
