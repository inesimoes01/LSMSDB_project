package com.example.lsmsdb.Database;
import org.neo4j.driver.*;
public class DatabaseNeo4j implements AutoCloseable{
    private static Driver driverNeo4j;

    public static void connectNeo4j() {
        // Connect to the Neo4j database
        try{
            driverNeo4j = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "adminadmin"));
            try{
                Session session = driverNeo4j.session();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws RuntimeException {
        driverNeo4j.close();
    }
}
