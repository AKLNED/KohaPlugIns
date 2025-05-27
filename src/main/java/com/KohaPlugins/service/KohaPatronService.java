package com.KohaPlugins.service;

//import com.KohaPlugins.util.AuthManager;
import com.KohaPlugins.util.AuthBasic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;


public class KohaPatronService {

    public JSONObject getKohaPatronByCardNumber(String studentId) {
        try {
            String url = "http://www.seakl.neduet.edu.pk/api/v1/patrons?cardnumber=" + studentId + "&_match=exact";
            //String token = AuthManager.getAccessToken();
                       
            URI uri = new URI(url);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                return jsonArray.length() > 0 ? jsonArray.getJSONObject(0) : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getExtendedAttributes(int patronId) {
        try {
            String url = "http://www.seakl.neduet.edu.pk/api/v1/patrons/" + patronId + "/extended_attributes";
 //           String token = AuthManager.getAccessToken();

            URI uri = new URI(url);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
//            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());


            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                return new JSONArray(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray(); // empty
    }
    

}