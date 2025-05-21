package com.KohaPlugins.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;

import org.json.JSONObject;

public class AuthManager {
    private static final String CLIENT_ID = "c5a68108-4e82-4f4f-a843-0e769c79ad44"; // Replace with your client ID
    private static final String CLIENT_SECRET = "7fd830eb-c29d-4fa3-9a5e-a0e34a7d98b3"; // Replace with your client secret
    private static final String TOKEN_ENDPOINT = "http://seakl.neduet.edu.pk/api/v1/oauth/token";

    private static String accessToken = null;
    private static long tokenExpiry = 0;

    /**
     * Retrieves the current access token. If the token is expired or missing,
     * it fetches a new one.
     *
     * @return The valid access token.
     * @throws Exception If there is an issue during the token request.
     */
    public static synchronized String getAccessToken() throws Exception {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiry) {
            fetchNewToken();
        }
        return accessToken;
    }

    /**
     * Fetches a new token from the Koha API and updates the accessToken and tokenExpiry.
     *
     * @throws Exception If there is an issue during the token request.
     */
    private static void fetchNewToken() throws Exception {
        String urlParameters = "grant_type=client_credentials" +
                "&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8");

        // Disable SSL verification for self-signed Koha certificate
        SSLHelper.disableSSLVerification();

        // Create connection
        //URL url = new URL(TOKEN_ENDPOINT);
        URI uri = new URI(TOKEN_ENDPOINT); // Use URI for validation
        URL url = uri.toURL(); // Convert URI to URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Send the POST request
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        // Get response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder apiResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    apiResponse.append(inputLine);
                }

                // Parse the access token
                JSONObject jsonResponse = new JSONObject(apiResponse.toString());
                accessToken = jsonResponse.getString("access_token");
                tokenExpiry = System.currentTimeMillis() + (jsonResponse.getInt("expires_in") * 1000); // Save expiry time
            }
        } else {
            throw new Exception("Failed to obtain access token. Response Code: " + responseCode);
        }
    }

    /**
     * Returns the token expiry time in milliseconds.
     *
     * @return The expiry time of the current token.
     */
    public static long getTokenExpiry() {
        return tokenExpiry;
    }
}