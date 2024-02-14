import requests
import pymongo

def get_all_movies(api_key, page=1):
    url = f"http://www.omdbapi.com/?apikey={api_key}&s=&type=movie&page={page}"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        print("Error fetching data:", response.text)
        return None

def main():
    # Connect to MongoDB
    client = pymongo.MongoClient("mongodb://localhost:27017/")
    db = client["teste"]
    collection = db["movie_omdb"]

    # Your OMDB API key
    api_key = "fa533fe5"

    # Retrieve all movies using pagination
    page = 1
    while True:
        movies_data = get_all_movies(api_key, page)
        if movies_data is not None:
            if movies_data.get('Response') == 'True':
                movies = movies_data.get('Search', [])
                for movie in movies:
                    # Insert each movie as a document into MongoDB
                    collection.insert_one(movie)
                if int(movies_data.get('totalResults', 0)) <= page * 10:
                    break  # Reached the end of results
                else:
                    page += 1
            else:
                print("OMDB API Error:", movies_data.get('Error'))
                break  # Exit loop if there's an error
        else:
            print("Failed to fetch data from OMDB API.")
            break  # Exit loop if there's no data

    print("All movies inserted into MongoDB.")

if __name__ == "__main__":
    main()





# import requests

# def get_all_movies(api_key, page=1):
#     url = f"http://www.omdbapi.com/?apikey={api_key}&s=&type=movie&page={page}"
#     response = requests.get(url)
#     if response.status_code == 200:
#         return response.json()
#     else:
#         return None

# def main():
#     # Your OMDB API key
#     api_key = "fa533fe5"

#     all_movies = []

#     # Retrieve all movies using pagination
#     page = 1
#     while True:
#         movies_data = get_all_movies(api_key, page)
#         if movies_data is not None and movies_data.get('Response') == 'True':
#             all_movies.extend(movies_data.get('Search', []))
#             if int(movies_data.get('totalResults', 0)) <= page * 10:
#                 break  # Reached the end of results
#             else:
#                 page += 1
#             if page>10:
#                 break
#         else:
#             break  # Exit loop if there's an error or no more results

#     # Process or store the retrieved movie data
#     for movie in all_movies:
#         print(movie)  # For demonstration, you can modify this to store in a database or file

# if __name__ == "__main__":
# #     main()
# import requests
# from pymongo import MongoClient

# # TMDB API base URL
# TMDB_API_BASE_URL = "http://www.omdbapi.com/?apikey=fa533fe5&type=movie&page={page}"

# # MongoDB connection settings
# MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/"
# MONGODB_DATABASE_NAME = "teste"
# MONGODB_COLLECTION_NAME = "movie_omdb"

# # TMDB API key
# API_KEY = "fa533fe5"

# # Initialize MongoDB client and database
# client = MongoClient(MONGODB_CONNECTION_STRING)
# db = client[MONGODB_DATABASE_NAME]
# collection = db[MONGODB_COLLECTION_NAME]

# # TMDB endpoint for popular movies
# endpoint = "/movie"

# # TMDB API request parameters
# params = {
#     "page": 1  # Start with the first page of results
# }

# # List to store all movies
# all_movies = []

# # Make requests for multiple pages of results
# while True:
#     # Make GET request to TMDB API
#     movies_data = requests.get(TMDB_API_BASE_URL, params=params)
    
# # Check if request was successful
#     if movies_data is not None and movies_data.get('Response') == 'True':
#         movies = movies_data.get('Search', [])
#         for movie in movies:
#             # Insert each movie as a document into MongoDB
#             collection.insert_one(movie)
#         if int(movies_data.get('totalResults', 0)) <= page * 10:
#             break  # Reached the end of results
#         else:
#             page += 1
#     else:
#         break 

# # collection.insert_many(all_movies)
# print("Movies data inserted into MongoDB collection successfully.")