package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.User;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {

    public static boolean checkUsernameCredentials(String username, String password){
        MongoCollection userCollection = DatabaseMongoDB.getCollection("users");

        Document searchQuery = new Document();
        searchQuery.put("id", username);

        try(MongoCursor cursorIterator = userCollection.find(searchQuery).iterator()){
            if(cursorIterator.hasNext()){
                Document doc = (Document) cursorIterator.next();
                if(password.equals(doc.get("password").toString())){
                    User.setUsername(username);
                    User.setFullName(doc.get("name").toString());
                    User.setLoggedIn(true);
                    return true;
                }
                //System.out.println("Username " + cursorIterator.next());
            }
        }catch(MongoException me){
            System.exit(-1);
        }
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
