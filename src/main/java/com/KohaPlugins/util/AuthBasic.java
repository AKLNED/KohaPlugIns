package com.KohaPlugins.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;

public class AuthBasic {
   // private static final String KOHA_USER = "imran";
   // private static final String KOHA_PASS = "Imran123";
	private static String kohaUser = "";
    private static String kohaPass = "";
    
    public static synchronized void setCredentials(String user, String pass) {
        kohaUser = user;
        kohaPass = pass;
    }
    
    public static String getBasicAuthHeader() {
        String auth = kohaUser + ":" + kohaPass;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encodedAuth;
    }

    public static void setSessionTimeout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(10 * 60); // 10 minutes in seconds
    }
    
 // New method: check credentials by making a simple API call
    public static boolean validateKohaCredentials() {
        try {
            //URL url = new URL(testUrl);
        	//String testUrl1 = "http://www.seakl.neduet.edu.pk/api/v1/libraries";
        	//URI uri = new URI(testUrl);         // Validates the URL and parses it
        	//HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        	String testUrl = "http://www.seakl.neduet.edu.pk/api/v1/libraries";
        	HttpURLConnection conn = null;
        	try {
        	    URI uri = new URI(testUrl);
        	    URL url = uri.toURL();
        	    conn = (HttpURLConnection) url.openConnection();
        	    conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", getBasicAuthHeader());
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
        	} catch (URISyntaxException e) {
        	    // Handle invalid URI (e.g., log or return an error)
        	    e.printStackTrace();
        	    return false; // or whatever is appropriate for your method
        	}
        	  	

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            // Koha API returns 200 for success, 401 for unauthorized
            return (responseCode == 200);
        } catch (IOException e) {
            // Could not connect, treat as auth failure
            return false;
        }
    }
}