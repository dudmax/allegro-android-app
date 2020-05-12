package com.example.myapplication;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Class help to get response from Allegro API.
 * In this class we generate url, create http connection and return json in string
 */
public class JsonAllegroResponse {

    private static final String ALLEGRO_API_BASE_URL = "https://private-987cdf-allegromobileinterntest.apiary-mock.com";
    private static final String ALLEGRO_API_METHOD_NAME = "/allegro/offers";

    private static URL generateURL() {
        Uri uri = Uri.parse(ALLEGRO_API_BASE_URL + ALLEGRO_API_METHOD_NAME);
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromURL() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) generateURL().openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            httpURLConnection.disconnect();
        }
    }

}
