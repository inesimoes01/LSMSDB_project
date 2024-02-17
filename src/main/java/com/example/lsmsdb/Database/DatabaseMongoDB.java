package com.example.lsmsdb.Database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseMongoDB {

    private static MongoClient clientMongoDB;
    private static MongoDatabase databaseMongoDB;
    public static void connectMongoDB(){
        clientMongoDB = MongoClients.create("mongodb://localhost:27017");
        databaseMongoDB = clientMongoDB.getDatabase("lsmdb_project");
        //clientMongoDB.listDatabaseNames().forEach(System.out::println);
        //MongoCollection collection
    }

    public static void closeMongoDB(){
        clientMongoDB.close();
    }

    public static MongoClient getClientMongoDB() {
        return clientMongoDB;
    }

    public static MongoCollection getCollection(String collectionName){
        return databaseMongoDB.getCollection(collectionName);
    }


}

//        String connectionString = "mongodb+srv://admin:5DZhmT1mndczxt6K@cluster0.ajuknyn.mongodb.net/?retryWrites=true&w=majority";
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(connectionString))
//                .serverApi(serverApi)
//                .build();
//        // Create a new client and connect to the server
//        try (MongoClient mongoClient = MongoClients.create(settings)) {
//            try {
//                // Send a ping to confirm a successful connection
//                MongoDatabase database = mongoClient.getDatabase("teste");
//                database.runCommand(new Document("ping", 1));
//                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
//                return mongoClient;
//            } catch (MongoException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }