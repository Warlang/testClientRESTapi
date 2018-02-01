package com.example.andreas.teliacarrier;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Array of Movies to be displayed
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    // Highlighted movie user are interested in (additional data)
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * OnClick event that trigger related content search
     * @param view - button that was clicked.
     */
    public void search(View view){
        // Request data from REST API: http://www.omdbapi.com

        // Get textView by ID
        EditText et = (EditText) this.findViewById(R.id.editText);

        // Clear focus (keyboard get hidden again)
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Check if text exist
        if(et.getText().toString().isEmpty()){
            //Error, no text (user input) available
            Toast.makeText(MainActivity.this, "Abort search, no valid input!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            // Read user input in text field
            String searchFor = et.getText().toString();

            // Make lookup
            // Trigger HTTP request
            new RequestContent().execute(searchFor);
        }

    }

    /**
     * Update the movie list with available movies
     * from list.
     */
    public void updateMovieList(){

        // run on GUI thread to make updates
        this.runOnUiThread(new Runnable() {
            public void run() {
                // Do work
                // Get handle to container
                LinearLayout view = MainActivity.this.findViewById(R.id.movie_container);

                // Clear container before adding new children (items)
                view.removeAllViews();

                // Check if any movies exists
                if(movies == null){
                    // No movies exist in list
                    return;
                }

                // Add list items to the gui
                for(Movie m : movies){

                    // Make movie item vertical container
                    LinearLayout ll = new LinearLayout(MainActivity.this);
                    ll.setOrientation(LinearLayout.VERTICAL);


                    // Make new textView for title and add it
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(MainActivity.this.getString(R.string.title) +" "+ m.getTitle());
                    tv.setTextSize(18);
                    ll.addView(tv);

                    // Make new textView for year and add it
                    tv = new TextView(MainActivity.this);
                    tv.setText(MainActivity.this.getString(R.string.released) +" "+ m.getYear() + "\n");
                    tv.setTextSize(18);
                    ll.addView(tv);

                    // Set some text to textView
                    //tv.setText(MainActivity.this.getString(R.string.title) +" "+ m.getTitle() + "\n" + MainActivity.this.getString(R.string.released) +" "+ m.getYear()+ "\n");

                    // Add movie container to view
                    view.addView(ll);

                    // Make each item (movie) to a clickable object
                    ll.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // Request webservice here. Possible use of AsyncTask and ProgressDialog
                            // Get parent
                            LinearLayout parent = (LinearLayout) v.getParent();


                            // Index of clicked menu item (from list) and if it was found
                            boolean isFound = false;
                            int index = 0;

                            // Find index
                            for(int i = 0; i < parent.getChildCount(); i++){
                                if(parent.getChildAt(i).equals(v)){
                                    index = i;
                                    isFound = true;
                                    break;
                                }
                            }

                            // Check if found movie that got clicked
                            if(isFound){
                                // Found clicked view movie
                                // Make movie handle
                                Movie movie = movies.get(index);

                                // Fetch additional details about movie
                                // Make lookup
                                // Trigger HTTP request
                                new RequestContent().execute(movie.getImdbID());

                            }
                            else {
                                // Not found, do nothing
                            }
                        }

                    });
                }
            }
        });

    }

    /**
     * Display a movie that the user
     * find high interest in
     */
    public void highlightMovie(){
        // Check if any highlight movie exist
        if(movie == null){
            // No highlight movie exist
            return;
        }

        // run on GUI thread to display the movie
        this.runOnUiThread(new Runnable() {
            public void run() {
                // Do work
                // Get string pieces to display about movie
                String title = movie.getTitle();
                String runTime = movie.getRunTime();
                String plot = movie.getPlot();
                String imdbRating = movie.getImdbRating();
                String actors = movie.getActors();

                // Create message to display with data
                String message = "Title: " + title + "\n\nIMDB Rating: " + imdbRating + "\n\nPlot: " + plot  + "\n\nLength: " + runTime + "\n\nActors: " + actors;

                // Display more info about the movie from a dialog pop up here
                InfoDialog.show(message,MainActivity.this);

            }
        });

    }

    /**
     * Provide user feedback that his search has failed
     */
    public void userResponseFeedback(boolean successful){
        if(!successful){
            // Request was unsuccessful, provide failure response
            Toast.makeText(MainActivity.this, "Specify the movie further, and check the spelling!", Toast.LENGTH_LONG).show();
        }/*
        else{
            // Returned successfully
            if(movies.isEmpty()){
                // No search r
            }
            Toast.makeText(MainActivity.this, "Specify more of the movie title and check the spelling!", Toast.LENGTH_SHORT).show();
        }*/
    }




    private class RequestContent extends AsyncTask<String, Integer, Void> {

        // Handle URLs' creating and setup
        private HandlerURL urlHandler;

        // Bool, false if response was bad, true if success
        private boolean isSuccessful = false;

        // Methods
        @Override
        protected void onPostExecute(Void aVoid) {
            // Check if highlight a movie or update movies list for user
            if(urlHandler.isSearchByID()){
                // Highlight a movie with high level of detail
                highlightMovie();
            }
            else {
                // Update the list now after search been completed
                updateMovieList();
            }

            if(!isSuccessful){
                // Response was not successful and request led to failure
                userResponseFeedback(isSuccessful);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            // Do work
            // Check if handler is initialized
            if(urlHandler == null){
                // Does not exist, create obj
                urlHandler = new HandlerURL();
            }

            // Require minimum 1 argument to exist to proceed
            if(params.length <= 0){
                // If no parameters its null.
                return null;
            }

            // Generate the URL of either search after name of specific movie ID
            urlHandler.generateURL(params[0]);

            // Check if url is valid
            if(HandlerURL.isValid(urlHandler.getUrl())){
                // URL is not valid, abort read
                return null;
            }
            else {
                // Is valid URL
                // Make HTTP request
                makeRequest();
            }

            // Each time you want the progress bar to update, you
            // call the publishProgress method, and pass the value
            // to be passed to the progressBar.setProgress method
            //publishProgress(yourPercentage);

            return null;
        }


        // Making the actual request to the server
        private void makeRequest() {
            try {
                // Get data from web-address
                fetchData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // Reset things if required

            }

        }


        /**
         * Fetch data from REST API as a JSON string
         * @throws IOException
         */
        private void fetchData() throws IOException {
            // Fetch by using the org.apache.http library
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlHandler.getUrl());

            // A server response
            HttpResponse response = httpclient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == 200) {
                // Read server response to a STRING obj
                String server_response = EntityUtils.toString(response.getEntity());

                // True if response is valid
                if(!utilsJSON.parseResponseJSON(server_response)){
                    // Not valid, response gave false
                    // Set user request successful to false for failure feedback
                    isSuccessful = false;

                    // Update movies and movie to nothing since nothing was found
                    movies = null;
                    movie = null;
                    return;
                }
                else {
                    // Set user request as successful
                    isSuccessful = true;
                }


                // Check if search by name or ID
                if(urlHandler.isSearchByID()){
                    // Specific movie by ID
                    // Parse JSON for single movie for details. Set the highlight movie
                    movie = utilsJSON.parseMovieJSON(server_response);
                }
                else {
                    // User request search for movies
                    // Parse JSON for movies from a string obj. Creating an array of movies objects
                    movies = utilsJSON.parseMoviesJSON(server_response);
                }

                // Log if success to fetch data
                Log.i("Server response", server_response);
            } else {

                // Log if failure to fetch data
                Log.i("Server response", "Failed to get server response");
            }
        }

    }
}
