import requests
from pymongo import MongoClient

# Trakt API base URL
TRAKT_API_BASE_URL = "https://api.trakt.tv"

# MongoDB connection settings
MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/"
MONGODB_DATABASE_NAME = "teste"
MONGODB_COLLECTION_NAME = "movie"

# Trakt API credentials
CLIENT_ID = "07cf3c1096c111f4e689f179f484e4026bf49079f7b7de16fb0b5b464e681fc7"
CLIENT_SECRET = "85f6f07eb1309a0e6b1644a319d4645d04551fe23868cea6ffb38adafdfeaa56"
ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"

# Initialize MongoDB client and database
client = MongoClient(MONGODB_CONNECTION_STRING)
db = client[MONGODB_DATABASE_NAME]
collection = db[MONGODB_COLLECTION_NAME]

# Trakt API endpoint for popular movies
comments_endpoint = "/movies/{}/comments/sort"

# Trakt API request headers
headers = {
    "Content-Type": "application/json",
    "trakt-api-version": "2",
    "trakt-api-key": CLIENT_ID
}

# # Make GET request to Trakt API
# response = requests.get(TRAKT_API_BASE_URL + endpoint, headers=headers)

# # Check if request was successful
# if response.status_code == 200:
#     # Parse response JSON
#     movies_data = response.json()

#     # Insert movie data into MongoDB collection
#     for movie in movies_data:
#         collection.insert_one(movie)

#     print("Movies data inserted into MongoDB collection successfully.")
# else:
#     print("Error:", response.status_code)
# List to store all movies
all_movies = []

# Iterate over pages of results
page = 1
while True:
    # Make GET request to Trakt API for the current page
    response = requests.get(TRAKT_API_BASE_URL + comments_endpoint.format(movie_id), headers=headers)

    # Check if request was successful
    if response.status_code == 200:
        # Parse response JSON
        movies_data = response.json()

        for movie in movies_data:
            collection.insert_one(movie)
        # Append movies from current page to the list
        # all_movies.extend(movies_data)

        # Move to the next page
        page += 1
        print(page)
    else:
        print("Error:", response.status_code)
        break

# Process the list of all movies as needed
print("Total movies retrieved:", len(all_movies))