from pymongo import MongoClient
from neo4j import GraphDatabase

# MongoDB connection settings
MONGO_URI = "mongodb://localhost:27017/"
MONGO_DB_NAME = "project"
MONGO_COLLECTION_NAME = "user"
MONGO_WATCHLIST_FIELD = "watchlist"

# Neo4j connection settings
NEO4J_URI = "bolt://localhost:7687"
NEO4J_USER = "neo4j"
NEO4J_PASSWORD = "adminadmin"

import random

def generate_random_number():
    return random.uniform(0.1, 10)


# Define a function to establish MongoDB connection
def connect_to_mongodb(uri, db_name):
    client = MongoClient(uri)
    db = client[db_name]
    return db

# Define a function to establish Neo4j connection
def connect_to_neo4j(uri, user, password):
    driver = GraphDatabase.driver(uri, auth=(user, password))
    return driver

# Define a function to create relationships between users and movies in Neo4j
def create_relationships(session, user_id, movie_id):
    query = (
        "MATCH (u:user {username: $user_id}) "
        "MATCH (m:Movie {imdb_id: $movie_id}) "
        "CREATE (u)-[:RATED {rating: $rating}]->(m)"
    )
    session.run(query, user_id=user_id, movie_id=movie_id, rating=generate_random_number())

# Main function to iterate over MongoDB documents and create relationships in Neo4j
def main():
    mongo_db = connect_to_mongodb(MONGO_URI, MONGO_DB_NAME)
    neo4j_driver = connect_to_neo4j(NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD)

    with neo4j_driver.session() as session:
        # Iterate over MongoDB documents
        for user_doc in mongo_db[MONGO_COLLECTION_NAME].find():
            user_id = user_doc["_id"]
            watchlist = user_doc.get(MONGO_WATCHLIST_FIELD, [])
            # Iterate over user's watchlist
            for movie_id in watchlist:
                id = movie_id["_id"]
                create_relationships(session, str(user_id), str(id))
                print(user_id, id)

    neo4j_driver.close()

if __name__ == "__main__":
    main()
