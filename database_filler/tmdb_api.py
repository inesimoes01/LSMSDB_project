import requests
from pymongo import MongoClient



def get_movie_details(tmdb_id, api_key):
    # Step 1: Retrieve basic movie details

    if response.status_code == 200:
        data = response.json()
        if data["results"]:
            # Step 2: Extract TMDb ID from the response
            
            
            # Step 3: Make a second request to get more detailed information
            url = f"https://api.themoviedb.org/3/movie/{tmdb_id}"
            params = {"api_key": api_key}
            response = requests.get(url, params=params)
            
            if response.status_code == 200:
                detailed_data = response.json()
                return {**data["results"][0], **detailed_data}  # Step 4: Merge the data
            else:
                print("Failed to retrieve detailed movie information.")
        else:
            print("Movie not found.")
    else:
        print("Failed to retrieve movie details.")
    return None

def main():
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

            tmdb_id = movies_data["results", "id"]
            detailed_data = get_movie_details(tmdb_id, params["api_key"])
            # collection.insert_many(movies_data)
            # Append movies from current page to the list
            all_movies.extend(detailed_data)

            print("Total movies retrieved:", len(all_movies))
            # Move to the next page
            params["page"] += 1

            if params["page"] >20: break
        else:
            print("Error:", response.status_code)
            break

    collection.insert_many(all_movies)
    print("Movies data inserted into MongoDB collection successfully.")

if __name__ == "__main__":
    main()