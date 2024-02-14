import requests
from pymongo import MongoClient

# TMDB API base URL
TMDB_API_BASE_URL = "https://api.themoviedb.org/3"

# MongoDB connection settings
MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/"
MONGODB_DATABASE_NAME = "teste"
MONGODB_COLLECTION_NAME = "review"

# TMDB API key
API_KEY = "76056d7e5ae115fdddbfcb0918c539d1"

# Initialize MongoDB client and database
client = MongoClient(MONGODB_CONNECTION_STRING)
db = client[MONGODB_DATABASE_NAME]
collection = db[MONGODB_COLLECTION_NAME]

# TMDB endpoint for popular movies
endpoint = "/movie/933131/reviews"

# TMDB API request parameters
params = {
    "api_key": API_KEY,
    "page": 1  # Start with the first page of results
}

# List to store all movies
all_users = []

# Make requests for multiple pages of results
while True:
    # Make GET request to TMDB API
    response = requests.get(TMDB_API_BASE_URL + endpoint, params=params)
    
    # Check if request was successful
    if response.status_code == 200:
        # Parse response JSON
        users_data = response.json()["results"]
        for user in users_data:
            collection.insert_one(user)
        # Append movies from current page to the list
        all_users.extend(users_data)
        print("Total users retrieved:", len(all_users))
        # Move to the next page
        params["page"] += 1
    else:
        print("Error:", response.status_code)
        break

#collection.insert_many(all_users)
print("Users data inserted into MongoDB collection successfully.")