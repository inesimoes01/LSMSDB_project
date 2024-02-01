//package com.example.lsmsdb.API;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//
//public class TraktAPI {
//    private static final String API_URL = "https://api.trakt.tv";
//    private static final String CLIENT_ID = "YOUR_CLIENT_ID"; // Replace with your actual client ID
//
//    public void connect() throws IOException {
//
//        TraktV2 trakt = new TraktV2("api_key");
//        Shows traktShows = trakt.shows();
//        try {
//            // Get trending shows
//            Response<List<TrendingShow>> response = traktShows.trending(1, null, Extended.FULL).execute();
//            if (response.isSuccessful()) {
//                List<TrendingShow> shows = response.body();
//                for (TrendingShow trending : shows) {
//                    System.out.println("Title: " + trending.show.title);
//                }
//            } else {
//                if (response.code() == 401) {
//                    // authorization required, supply a valid OAuth access token
//                } else {
//                    // the request failed for some other reason
//                }
//            }
//        } catch (Exception e) {
//            // see execute() javadoc
//        }
////        URL url = new URL(API_URL);
////        HttpURLConnection con = (HttpURLConnection) url.openConnection();
////        con.setRequestMethod("GET");
////
////        con.setRequestProperty("Content-Type", "application/json");
////        con.setRequestProperty("trakt-api-key", "00e2dd4298dfb45d23bb9dfc159980f456210392c8e6ad1bbdac42c94b9b5780");
////        con.setRequestProperty("trakt-api-version", "2");
////        ObjectMapper mapper = new ObjectMapper();
////        APOD apod = mapper.readValue(responseStream, APOD.class);
////
////// Finally we have the response
////        System.out.println(apod.title);
////
////        InputStream responseStream = con.getInputStream();
//    }
//}
