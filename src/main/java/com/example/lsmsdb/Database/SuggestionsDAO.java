package com.example.lsmsdb.Database;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.GUI.UserController;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class SuggestionsDAO {
    public static List<String> getSuggestedUsers() {
        List<String> l = new ArrayList<>();
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (targetUser:user {username: $username})-[:RATED]->(likedMovie:Movie)<-[:RATED]-(similarUser:user) " +
                                "WHERE similarUser <> targetUser AND NOT EXISTS " +
                                "{MATCH (targetUser)-[:FOLLOWS]->(similarUser)}" +
                                "RETURN similarUser.username AS recommendedUser, COUNT(likedMovie) AS commonLikes " +
                                "ORDER BY commonLikes ASC " +
                                "LIMIT 5 ", parameters("username", UserController.getLoggedInUser().getUsername()));
                while (res.hasNext()){
                    Record rec = res.next();
                    l.add(String.valueOf(rec.get("recommendedUser")));
                }
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return l;
    }

    public static List<Movie> getSuggestedMovie() {
        List<Movie> l = new ArrayList<>();
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (targetUser:user {username: $username})-[:FOLLOWS]->(followingUser:user)-[r:RATED]->(movieRecommended:Movie) " +
                                "WHERE NOT EXISTS {MATCH (targetUser)-[:RATED]->(movie)} " +
                                "WITH movieRecommended, AVG(r.rating) AS avgRating " +
                                "RETURN DISTINCT movieRecommended.title AS title, " +
                                "movieRecommended.imdb_id AS movieid, " +
                                "movieRecommended.poster AS poster, " +
                                "avgRating " +
                                "ORDER BY avgRating ASC " +
                                "LIMIT 5 ",
                        parameters("username", UserController.getLoggedInUser().getUsername()));

                while (res.hasNext()){
                    Record rec = res.next();
                    Movie m = new Movie(String.valueOf(rec.get("movieid")), rec.get("title").asString(), rec.get("poster").asString());
                    l.add(m);
                }
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return l;
    }

}
