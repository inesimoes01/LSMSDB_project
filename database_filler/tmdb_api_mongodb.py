import requests
from pymongo import MongoClient
import pymongo
import time 
import string
import random 


from neo4j import GraphDatabase

class Neo4jDriver:
    def __init__(self, uri, user, password):
        self._uri = uri
        self._user = user
        self._password = password
        self._driver = None

    def close(self):
        if self._driver is not None:
            self._driver.close()

    def connect(self):
        self._driver = GraphDatabase.driver(self._uri, auth=(self._user, self._password))

    def add_user(self, name, pic):
        with self._driver.session() as session:
            result = session.write_transaction(self._create_user, name, pic)
            return result

    @staticmethod
    def _create_user(tx, username, profilepic):
        query = (
            "CREATE (u:user {username: $username, profilepic: $profilepic})"
            "RETURN u"
        )
        result = tx.run(query, username=username, profilepic=profilepic)
        return result.single()[0]
    
    def add_movie(self, title, year, genre, poster, id, vote_count):
        with self._driver.session() as session:
            result = session.write_transaction(self._create_movie, title, year, genre, poster, id, vote_count)
            return result

    @staticmethod
    def _create_movie(tx, title, year, genre, poster, id, vote_count):
        query = (
            "CREATE (m:Movie {title: $title, year: $year, genre: $genre, poster: $poster, id: $id, vote_count: $vote_count})"
            "RETURN m"
        )
        result = tx.run(query, title=title, year=year, genre=genre, poster=poster, id=id, vote_count=vote_count)
        return result.single()[0]
    
    # Function to add relationship between user and movie
    def add_user_rated_movie(self, username, movie_id, rating):
        with self._driver.session() as session:
            result = session.write_transaction(self._create_user_likes_movie_relation, username, movie_id, rating)
            return result

    @staticmethod
    def _create_user_likes_movie_relation(tx, username, movie_id, rating):
        query = (
            "MATCH (u:user {username: $username})"
            "MATCH (m:Movie {id: $movie_id})"
            "CREATE (u)-[:RATED {rating: $rating}]->(m)"
        )
        result = tx.run(query, username=username, movie_id=movie_id, rating=rating)
        return result


def generate_random_string(length):
    characters = string.ascii_letters + string.digits
    return ''.join(random.choice(characters) for _ in range(length))

def get_movie_imdb(tmdb_id):
    r = requests.get(TMDB_API_BASE_URL + "/movie/" + str(tmdb_id), params=params_movie)
    if r.status_code == 200:
        movie_imdb = r.json()
        imdb = movie_imdb['imdb_id']
        return imdb

def get_movie_reviews(tmdb, title, poster, imdb_id, genre):
    all_reviews=[]
    all_users=[]
    params_reviews["page"] = 1
    while True:
        r = requests.get(TMDB_API_BASE_URL + "/movie/" + str(tmdb) + "/reviews", params=params_reviews)

        if r.status_code == 200:
            
            movies_review = r.json()["results"]
            pages = r.json()["total_pages"]

            for review in movies_review:
                username = review['author_details']['username']
                profilepic = review['author_details']['avatar_path']
                rating = review['author_details']['rating']
                date = review['created_at']
                content = review['content']
                fullname = review['author_details']['name']

                # review array
                review_doc = {'username': username, 'profilepic': profilepic, 'rating': rating, 'date': date, 'content': content}
                review_doc = {k: v for k, v in review_doc.items() if v is not None}

                # watchlist array
                watchlist = {'_id': imdb_id, 'title': title, 'poster':poster, 'genre_ids':genre}
                watchlist = {k: v for k, v in watchlist.items() if v is not None}

                # user document
                user_doc= {'_id': username, 'profilepic': profilepic, 'fullname':fullname, 'password':generate_random_string(5), "watchlist":[]}
                user_doc = {k: v for k, v in user_doc.items() if v is not None}
                user_neo = {"username": username, "profilepic": profilepic}

                
                try:
                    if user_doc: 
                        # add user to mongodb
                        collection_user.insert_one(user_doc)

                        # add user to neo4j
                        neo4j_driver.add_user(user_neo["username"], user_neo["profilepic"])
                        neo4j_driver.add_user_rated_movie(username, imdb_id, rating)

                        # add watchlist
                        query = {'_id': username}
                        update = {'$push': {'watchlist': watchlist}}
                        collection_user.update_one(query, update)

                except pymongo.errors.DuplicateKeyError:
                    neo4j_driver.add_user_rated_movie(username, imdb_id, rating)
                    query = {'_id': username}
                    update = {'$push': {'watchlist': watchlist}}
                    collection_user.update_one(query, update)
                    print(f"Skipping duplicate user")

            
                all_users.append(user_doc)
                all_reviews.append(review_doc)

            # print("Total reviews retrieved:", len(all_reviews))
            # print("Total users retrieved:", len(all_users))
            params_reviews["page"] += 1

            if (params_reviews["page"] > pages): 
                break
        else:
            print("Error:", response.status_code)
            break
    return all_reviews, all_users

def get_movie_reviews_Trakt(title, poster, imdb_id, genre):
 
    all_reviews = []
    response = requests.get(TRAKT_API_BASE_URL + comments_endpoint.format(imdb_id), headers=headers)

    # Check if request was successful
    if response.status_code == 200:
        # Parse response JSON
        movies_data = response.json()

        for review in movies_data:
            username = review['user']['username']
            rating = review['user_stats']['rating']
            date = review['created_at']
            content = review['comment']
            fullname = review['user'].get('name', 'Unknown')

            # review array
            review_doc = {'username': username, 'rating': rating, 'date': date, 'content': content}
            review_doc = {k: v for k, v in review_doc.items() if v is not None}

            # watchlist array
            watchlist = {'_id': imdb_id, 'title': title, 'poster':poster, 'genre_ids':genre}
            watchlist = {k: v for k, v in watchlist.items() if v is not None}

            # user document
            user_doc= {'_id': username, 'fullname':fullname, 'password':generate_random_string(5), "watchlist":[]}
            user_doc = {k: v for k, v in user_doc.items() if v is not None}
            user_doc = {k: v for k, v in user_doc.items() if v != 'Unknown'}
            user_neo = {"username": username}

        
            #collection_user.insert_one(user_doc)
            try:
                if user_doc: 
                    # add user to mongodb
                    collection_user.insert_one(user_doc)
                    
                    # add user to neo4j
                    neo4j_driver.add_user(user_neo["username"], "")
                    neo4j_driver.add_user_rated_movie(username, imdb_id, rating)

                    # add watchlist
                    query = {'_id': username}
                    update = {'$push': {'watchlist': watchlist}}
                    collection_user.update_one(query, update)

            except pymongo.errors.DuplicateKeyError:
                neo4j_driver.add_user_rated_movie(username, imdb_id, rating)
                query = {'_id': username}
                update = {'$push': {'watchlist': watchlist}}
                collection_user.update_one(query, update)
                print(f"Skipping duplicate user Trakt", username)
            except pymongo.errors as e:
                print(f"MongoDB operation failed: {e}")

            all_reviews.append(review_doc)

        # Append movies from current page to the list
        # all_movies.extend(movies_data)

        # Move to the next page
        
    else:
        print("Error:", response.status_code)
        return 
    return all_reviews


# TMDB API base URL
TMDB_API_BASE_URL = "https://api.themoviedb.org/3"
# Trakt API base URL
TRAKT_API_BASE_URL = "https://api.trakt.tv"

# MongoDB connection settings
MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/"
MONGODB_DATABASE_NAME = "project"
MONGODB_COLLECTION_NAME = "movie"
MONGODB_COLLECTION_USER = "user"
# TMDB API key
API_KEY = "76056d7e5ae115fdddbfcb0918c539d1"
# Trakt API credentials
CLIENT_ID = "07cf3c1096c111f4e689f179f484e4026bf49079f7b7de16fb0b5b464e681fc7"
CLIENT_SECRET = "85f6f07eb1309a0e6b1644a319d4645d04551fe23868cea6ffb38adafdfeaa56"
ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"

# Initialize MongoDB client and database
client = MongoClient(MONGODB_CONNECTION_STRING)
db = client[MONGODB_DATABASE_NAME]
collection_user = db[MONGODB_COLLECTION_USER]
collection_movie = db[MONGODB_COLLECTION_NAME]

# TMDB endpoint for popular movies
endpoint = "/movie/popular"

comments_endpoint = "/movies/{}/comments/sort"

# Trakt API request headers
headers = {
    "Content-Type": "application/json",
    "trakt-api-version": "2",
    "trakt-api-key": CLIENT_ID,
}

# Example usage:
neo4j_uri = "bolt://localhost:7687"  # URI of your Neo4j instance
neo4j_user = "neo4j"  # Neo4j username
neo4j_password = "adminadmin"  # Neo4j password

neo4j_driver = Neo4jDriver(neo4j_uri, neo4j_user, neo4j_password)
neo4j_driver.connect()

# TMDB API request parameters
params_reviews = {
    "api_key": API_KEY,
    "page": 1  # Start with the first page of results
}

params_movie = {
    "api_key": API_KEY,
    "page": 505  # Start with the first page of results
}
# List to store all movies

movie_length = 0
# Make requests for multiple pages of results
while True:
    # Make GET request to TMDB API
    response = requests.get(TMDB_API_BASE_URL + endpoint, params=params_movie)
    
    # Check if request was successful
    if response.status_code == 200:
        # Parse response JSON
        movies_data = response.json()["results"]

        for movie in movies_data:
            
            title = movie['title']
            tmdb_id = movie['id']
            poster = movie['poster_path']
            overview = movie['overview']
            popularity = movie['popularity']
            rating = movie['vote_average']
            vote_count = movie['vote_count']
            genre_ids = movie['genre_ids']
            release_date = movie['release_date']
            imdb_id = get_movie_imdb(tmdb_id)
            reviews, users = get_movie_reviews(tmdb_id, title, poster, imdb_id, genre_ids)
            trakt_reviews = get_movie_reviews_Trakt(title, poster, imdb_id, genre_ids)

            if trakt_reviews is not None:
                reviews.extend(trakt_reviews)


            print("NEW MOVIE " ,title)

            movie_doc = {
                'title': title, 
                'tmdb_id': tmdb_id, 
                'poster': poster, 
                'overview': overview, 
                'popularity': popularity, 
                'rating':rating, 
                'vote_count':vote_count, 
                'genre_ids':genre_ids, 
                'release_date':release_date, 
                '_id':imdb_id, 
                'reviews':reviews}
            
            # remove null values
            #movie_doc_nonull = {k: v for k, v in movie_doc.items() if v is not None}

            try:
                collection_movie.insert_one(movie_doc)
                neo4j_driver.add_movie(movie_doc["title"], 
                                        movie_doc["release_date"], 
                                        movie_doc["genre_ids"], 
                                        movie_doc["poster"], 
                                        movie_doc["_id"],
                                        movie_doc["vote_count"])
                    
            except pymongo.errors.DuplicateKeyError:
                print(f"Skipping duplicate movie ", title, imdb_id)


            movie_length += 1
            
        print("Total movies retrieved:", movie_length)
        params_movie["page"] += 1
        print("Last page :", params_movie["page"])
        time.sleep(0.1)

    else:
        print("Error:", response.status_code)
        break

print("Last page :", params_movie["page"])


