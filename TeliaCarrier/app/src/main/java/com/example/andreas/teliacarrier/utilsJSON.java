package com.example.andreas.teliacarrier;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Support function to handle JSON
 */

public class utilsJSON {

    /**
     * Parse JSON for movies
     * @param jsonStr - Movies with JSON format
     * @return List of movies
     */
    public static ArrayList<Movie> parseMoviesJSON(String jsonStr) {

        // Container for search result
        ArrayList<Movie> movies = new ArrayList<Movie>();

        // Parse JSON
        try {
            // Note: Exist two types in JSON, jsonObject that has keyword:{} and jsonArray that has keyword: []
            // Array has many elements of same type. Object is unique with key:value

            // Create a JSON obj
            JSONObject json = new JSONObject(jsonStr);
            //JSONObject dataObject = json.getJSONObject("Search"); (old not useful currently)

            // First comes a json array.
            JSONArray items = json.getJSONArray("Search");

            System.out.println("json Items:"  + items);
            System.out.println("json Items num: "  + items.length());

            // Loop the array for its items
            for (int i = 0; i < items.length(); i++) {
                JSONObject movieObject = items.getJSONObject(i);
                Movie movie = new Movie(movieObject.getString("Title"),
                        movieObject.getString("Year"),
                        movieObject.getString("imdbID"));

                // Add movie to list of found elements
                movies.add(movie);
            }

        } catch (JSONException e) {
            // manage exceptions
            Log.i("JSON parser", "Exception in parse of JSON");
        }

        // Return the movies
        return movies;
    }

    /**
     * Parse JSON for a movie
     * @param jsonStr - Much details about movie in JSON format
     * @return The movie with high details
     */
    public static Movie parseMovieJSON(String jsonStr) {
        // Parse JSON
        // JSON format e.g.
        /*{"Title":"Game of Thrones","Year":"2011–","Rated":"TV-MA","Released":"17 Apr 2011","Runtime":"57 min",
                "Genre":"Adventure, Drama, Fantasy","Director":"N/A","Writer":"David Benioff, D.B. Weiss",
                "Actors":"Peter Dinklage, Lena Headey, Emilia Clarke, Kit Harington",
                "Plot":"Nine noble families fight for control over the mythical lands of Westeros, while a forgotten race returns after being dormant for thousands of years.",
                "Language":"English","Country":"USA, UK","Awards":"Won 1 Golden Globe. Another 253 wins & 441 nominations.",
                "Poster":"https://images-na.ssl-images-amazon.com/images/M/MV5BMjE3NTQ1NDg1Ml5BMl5BanBnXkFtZTgwNzY2NDA0MjI@._V1_SX300.jpg",
                "Ratings":[{"Source":"Internet Movie Database","Value":"9.5/10"}],
                "Metascore":"N/A",
                "imdbRating":"9.5","imdbVotes":"1,268,485",
                "imdbID":"tt0944947","Type":"series","totalSeasons":"8","Response":"True"}*/
        try {
            // Note: Exist two types in JSON, jsonObject that has keyword:{} and jsonArray that has keyword: []
            // Array has many elements of same type. Object is unique with key:value

            // Create a JSON obj
            JSONObject json = new JSONObject(jsonStr);

            // Grab data about movie you find interesting
            String title = json.getString("Title");
            String genre = json.getString("Genre");
            String runTime = json.getString("Runtime");
            String imdbRating = json.getString("imdbRating");
            String plot = json.getString("Plot");
            String actors = json.getString("Actors");

            // Make high detail movie
            Movie movie = new Movie(title,genre,runTime,imdbRating,plot,actors);
            return movie;
        } catch (JSONException e) {
            // manage exceptions
            Log.i("JSON parser", "Exception in parse of JSON");
        }

        // Return null if it could not be parsed
        return null;
    }

    /**
     * Parse JSON response value.
     * @param jsonStr - return of data in JSON format
     * @return true if server response is valid, false otherwise
     */
    public static boolean parseResponseJSON(String jsonStr) {
        // Parse JSON
        try {
            // Note: Exist two types in JSON, jsonObject that has keyword:{} and jsonArray that has keyword: []
            // Array has many elements of same type. Object is unique with key:value

            // Create a JSON obj
            JSONObject json = new JSONObject(jsonStr);

            // Grab data about movie you find interesting
            String response = json.getString("Response");

            // Return the readable bool from the string response
            return Boolean.valueOf(response);

        } catch (JSONException e) {
            // manage exceptions
            Log.i("JSON parser", "Exception in parse of JSON");
        }

        // Return null if it could not be parsed
        return false;
    }

}
