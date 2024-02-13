import requests
from pymongo import MongoClient

# TMDB API base URL
TMDB_API_BASE_URL = "https://api.themoviedb.org/3"

# MongoDB connection settings
MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/"
MONGODB_DATABASE_NAME = "teste"
MONGODB_COLLECTION_NAME = "movie"

# TMDB API key
API_KEY = "76056d7e5ae115fdddbfcb0918c539d1"

# Initialize MongoDB client and database
client = MongoClient(MONGODB_CONNECTION_STRING)
db = client[MONGODB_DATABASE_NAME]
collection = db[MONGODB_COLLECTION_NAME]

# TMDB endpoint for popular movies
endpoint = "/movie/popular"

# TMDB API request parameters
params = {
    "api_key": API_KEY,
    "page": 1  # Start with the first page of results
}

# List to store all movies
all_movies = []

# Make requests for multiple pages of results
while True:
    # Make GET request to TMDB API
    response = requests.get(TMDB_API_BASE_URL + endpoint, params=params)
    
    # Check if request was successful
    if response.status_code == 200:
        # Parse response JSON
        movies_data = response.json()["results"]
        # collection.insert_many(movies_data)
        # Append movies from current page to the list
        all_movies.extend(movies_data)
        print("Total movies retrieved:", len(all_movies))
        # Move to the next page
        params["page"] += 1
    else:
        print("Error:", response.status_code)
        break

collection.insert_many(all_movies)
print("Movies data inserted into MongoDB collection successfully.")