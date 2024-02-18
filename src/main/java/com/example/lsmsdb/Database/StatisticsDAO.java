package com.example.lsmsdb.Database;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.GUI.UserController;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Filters.*;

import static org.neo4j.driver.Values.parameters;

public class StatisticsDAO {
    static MongoCollection userCollection = DatabaseMongoDB.getCollection("user");
    static MongoCollection moviesCollection = DatabaseMongoDB.getCollection("movie");

    public static List<String> getMostFollowedUser() {
        List<String> l = new ArrayList<>();
        try (Session session = DatabaseNeo4j.driverNeo4j.session()) { //try to add

            session.writeTransaction((TransactionWork<Void>) tx -> {
                Result res = tx.run(
                        "MATCH (u:user)-[rel:FOLLOWS]->() " +
                                "WITH u, COUNT(rel) AS followersCount " +
                                "ORDER BY followersCount DESC " +
                                "RETURN u.username AS mostFollowedUser, followersCount " +
                                "LIMIT 1");

                Record rec = res.next();
                l.add(rec.get("mostFollowedUser").asString());
                l.add(String.valueOf(rec.get("followersCount")));
                return null;
            });

        } catch (Neo4jException ne) { //fail, next cycle try to delete on MongoDB
            ne.printStackTrace();
        }
        return l;
    }

    public static List<String> getMostAddedMovie() {
        List<String> u = new ArrayList<>();
        Document movieWithMostWatchlistAdditions = (Document) moviesCollection.aggregate(
                Arrays.asList(
                        project(fields(include("title", "watchlist"), computed("watchlistCount", new Document("$size", "$watchlist")))),
                        sort(descending("watchlistCount")),
                        limit(1)
                )
        ).first();
        System.out.println("Movie that has been added to a watchlist most times: " + movieWithMostWatchlistAdditions.toJson());
        u.add(String.valueOf(movieWithMostWatchlistAdditions.getString("title")));
        u.add(String.valueOf(movieWithMostWatchlistAdditions.getInteger("watchlistCount")));
        return u;
    }

    public static List<String> getMostVersatileUser() {
        List<String> l = new ArrayList<>();
        Document userWithHighestGenresInWatchlist = (Document) userCollection.aggregate(
                Arrays.asList(
                        project(fields(include("username", "watchlist"), computed("genreCount", new Document("$size", "$watchlist.genre_ids")))),
                        sort(descending("genreCount")),
                        limit(1)
                )
        ).first();
        System.out.println("User with the highest number of genres in their watchlist: " + userWithHighestGenresInWatchlist.toJson());
        l.add(String.valueOf(userWithHighestGenresInWatchlist.getString("_id")));
        l.add(String.valueOf(userWithHighestGenresInWatchlist.getInteger("genreCount")));
        return null;
    }

    public static List<String> getMostCriticalUser() {
        List<String> l = new ArrayList<>();
        Document lowestRatingUser = (Document) moviesCollection.aggregate(
                Arrays.asList(
                        unwind("$reviews"),
                        match(ne("reviews.rating", null)),
                        group("$reviews.username", avg("avgRating", "$reviews.rating"), sum("count", 1)),
                        match(gte("count", 5)),
                        sort(ascending("avgRating")),
                        limit(1)
                )
        ).first();
        System.out.println("User that gives the lowest ratings: " + lowestRatingUser.toJson());
        l.add(lowestRatingUser.getString("_id"));
        l.add(String.valueOf(lowestRatingUser.getDouble("avgRating")));
        l.add(String.valueOf(lowestRatingUser.getInteger("count")));
        return l;
    }

    public static List<String> getMostTalkedAboutMovies() {
        List<String> l = new ArrayList<>();
        Document highestReviewedMovies = (Document) moviesCollection.aggregate(
                Arrays.asList(
                        project(fields(include("title", "reviews"), computed("reviewCount", new Document("$size", "$reviews")))),
                        sort(descending("reviewCount")),
                        limit(1)
                )
        ).first();
        System.out.println("Movies with the highest number of reviews: " + highestReviewedMovies);

        l.add(String.valueOf(highestReviewedMovies.getString("title")));
        l.add(String.valueOf(highestReviewedMovies.getInteger("reviewCount")));
        return l;
    }

    public static List<String> getMostFamousGenres() {
        List<String> l = new ArrayList<>();
        Document highestReviewedGenres = (Document) moviesCollection.aggregate(
                Arrays.asList(
                        unwind("$genre_ids"),
                        group("$genre_ids", sum("reviewCount", new Document("$size", "$reviews"))),
                        sort(descending("reviewCount"))
                )
        ).first();
        System.out.println("Genres with the highest number of reviews: " + highestReviewedGenres);
        l.add(String.valueOf(highestReviewedGenres.getInteger("_id")));
        l.add(String.valueOf(highestReviewedGenres.getInteger("reviewCount")));
        return l;
    }
}


//                        "MATCH (targetUser:user {username: $username})-[:FOLLOWS]->(similarUser:user)-[:RATED]->(movieRecommended:Movie) " +
//                                "WHERE NOT EXISTS {MATCH (targetUser)-[:RATED]->(movieRecommended)} " +
//                                "WITH movieRecommended, COUNT(*) AS recS " +
//                                "RETURN movieRecommended.title AS title, " +
//                                        "movieRecommended.imdb_id AS movieid, " +
//                                        "movieRecommended.poster AS poster, " +
//                                "ORDER BY recS DESC" +
//                                "LIMIT 5\n", parameters("username", UserController.getLoggedInUser().getUsername()));


//Result res = tx.run(
//        "MATCH (u1:user)-[:FOLLOWS]->(u2:user)-[:RATED]->(m:Movie) " +
//                "WHERE u1.username = $username AND NOT EXISTS {MATCH (u1)-[:RATED]->(m)} " +
//                "RETURN DISTINCT movieRecommended.title AS title, " +
//                "movieRecommended.imdb_id AS movieid, " +
//                "movieRecommended.poster AS poster, " +
//                "COUNT(u2) AS num_followed_users " +
//                "ORDER BY num_followed_users ", parameters("username", UserController.getLoggedInUser().getUsername()));