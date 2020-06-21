package com.company;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Responsible for
 * communicating with the server
 * using a REST API
 */
public class API {

    private final String URL_PREFIX = "https://www.omdbapi.com/?";//i=tt3896198
    private final String ID = "i=";
    private final String TITLE = "t=";
    private final String API_KEY = "&apikey=a68db2d";

    private  String URL;// = URL_PREFIX + API_KEY;

    private CloseableHttpClient httpClient;
    private Gson gson;

    public API() {
        gson = new Gson();
    }

    public Movie fetchMovie(Enum<Search> searchEnum , String searchValue) throws IOException {

       // URL =  searchEnum == Search.ID ?  URL_PREFIX + ID + searchValue + API_KEY : URL_PREFIX + TITLE + searchValue + API_KEY;

        URL =  searchEnum == Search.ID ?
                URL_PREFIX + ID + URLEncoder.encode(searchValue, StandardCharsets.UTF_8.toString()) + API_KEY :
                URL_PREFIX + TITLE + URLEncoder.encode(searchValue, StandardCharsets.UTF_8.toString()) + API_KEY;


        httpClient = HttpClients.createDefault();
        MovieJson movieJson = null;
        Movie movie = null;

        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(URL));

            try {
//                if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
//                    logger.info("Status is 200 OK!");
//                else logger.warning("Status code is bad!");
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Response: " +result);

                    if (result.equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}"))
                        throw new MovieNotFoundException(searchValue);



                    movieJson = gson.fromJson(result, MovieJson.class);
                    movie =  convertJsonToMovie(movieJson);
                }
            } finally { response.close(); }
        } finally { httpClient.close(); }


        return movie;
    }

    private Movie convertJsonToMovie(MovieJson movieJson) {
            Random random = new Random();
            Double price = (random.nextDouble() * 100 + 20);
            price = price.intValue() + 0.0;
        Movie movie = new Movie();
            movie.setRating(movieJson.getImdbRating());
            movie.setPlot(movieJson.getPlot());
            movie.setTitle(movieJson.getTitle());
            movie.setDirector( movieJson.getDirector());
            movie.setImdbID(movieJson.getImdbID());
            movie.setYear( movieJson.getYear());
            movie.setGenre(movieJson.getGenre());
            movie.setPrice(price );
            return movie;
    }
}

