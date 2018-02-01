package com.example.andreas.teliacarrier;


public class HandlerURL{
    // Attributes
    // Key is valid up to 1000 requests/day
    private static final String accessKey = "&apikey=[YOUR_PERSONAL_KEY_GOES_HERE]";

    // Protocol to access over web
    private static final String protocol = "http://";

    // URL to get data in JSON formatted data from REST-API
    private static final String serverName = "www.omdbapi.com/";

    // URL search prefix for movie names
    private static final String searchForMoviePrefix = "?s=";

    // URL search prefix for using IMDB id
    private static final String searchForIDPrefix = "?i=";

    // Where to find resource location for JSON formatted data
    private String resource = "";

    // The complete url to make the wanted user request
    private String url = "";

    private boolean isSearchByID = false;


    // Constructor


    // Private methods

    /**
     * Generate URL of either IMDB type of
     * argument match the format. Otherwise a
     * search URL after matching names.
     * @param arg - input either IMDB ID, or name to search for
     * @return - generated URL
     */
    public String generateURL(String arg){
        // Check if arg is an IMDB id
        if(isIDTypeIMDB(arg)){
            // Is IMDB id, generate for a specific movie
            // Set if seach by ID to true
            isSearchByID = true;

            // Generate a movie ID search url
            url = generateIDSearchURL(arg);
        }
        else {
            // Not IMDB id, generate for "search" after name
            // Set if search by ID to false
            isSearchByID = false;

            // Generate a movie search by name url
            url = generateMovieSearchURL(arg);
        }
        return url;
    }


    /**
     * Provide a functional URL to REST API
     * for a name search.
     * @param movieName - name to is searched for
     * @return - null if invalid url or the actual url if valid that provide data from such a search
     */
    private String generateMovieSearchURL(String movieName){
        // Must contain a valid name
        if(movieName == null){
            // Not valid
            return null;
        }
        if(movieName.isEmpty()){
            // Not valid
            return null;
        }

        // Set resource
        resource = movieName;

        // Assemble the URL
        url = protocol + serverName + searchForMoviePrefix + resource + accessKey;

        // Translate white space (" ") from user, to (&) for a correct formatted URL
        url = url.replaceAll(" ", "&");
        //url = url.replaceAll(" ", "+"); // + both must exist so must spell all correctly. So "&" are more user friendly

        return url;
    }


    /**
     * Provide a functional URL to REST API
     * for a specified ID for a movie search.
     * @param id - imdb id to a specific movie
     * @return - null if invalid url or the actual url if valid that provide data from such a search
     */
    private String generateIDSearchURL(String id){
        // Must contain a valid name
        if(id == null){
            // Not valid
            return null;
        }
        if(id.isEmpty()){
            // Not valid
            return null;
        }

        // Set resource
        resource = id;

        // Assemble the URL
        url = protocol + serverName + searchForIDPrefix + resource + accessKey;

        // Return url
        return url;
    }


    //TODO this function need testing
    /**
     * Check if url is a detailed movie search or
     * a general search.
     * @param url - Rest api url
     * @return true if a search by ID, otherwise false
     */
    /*
    public static boolean isIDSearch(String url){
        // Get head of URL to compare if ID search or normal search
        String head = protocol + serverName + searchForIDPrefix;
        System.out.println("head:" + head);

        // Check if equals to search prefix by ID
        if(url.substring(0,head.length()-1).equals(head)){
            // Match found
            return true;
        }
        // Not matching
        return false;

    }*/



    /**
     * Check if url is valid and match the www.omdbapi.com
     * REST API.
     * @param url - url to the rest API
     * @return true if valid, otherwise false
     */
    public static boolean isValid(String url){
        // Must exist
        if(url == null){
            return false;
        }

        // Must not be empty
        if(url.isEmpty()){
            return false;
        }

        // Check that url is larger than the base of URL
        String base = protocol+serverName;
        if(base.length() < url.length()){
            return false;
        }


        // TODO A more comprehensive checking if URL is correct e.g. prefix of strings and such no white space etc. those are not allowed

        // Is a valid address
        return true;
    }


    /**
     * Check if IMDB id
     * @param str - checked if IMDB if format e.g. tt12312
     * @return - true if format is of type IMDB id, false otherwise
     */
    public static boolean isIDTypeIMDB(String str){
        if(str == null){
            // Does not exist anything
            return false;
        }
        if(str.length() < 2){
            // Must be larger than code tt to be a code
            return false;
        }

        String head = str.substring(0,2);
        String tail = str.substring(2);

        // Check if head start with right format of "tt"
        if(head.equals("tt")){
            // Check if follow by only digits
            String regex = "\\d+";
            if(tail.matches(regex)){
                // Format as IMDB id
                return true;
            }
        }
        return false;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSearchByID() {
        return isSearchByID;
    }

}
