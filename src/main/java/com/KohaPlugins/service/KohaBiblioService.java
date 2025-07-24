package com.KohaPlugins.service;

import com.KohaPlugins.util.AuthBasic;

//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.URL;

import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;



public class KohaBiblioService {
	
	public JSONObject getKohaBiblioItemByBarcode(String barcode) {

            try {
                // Call Koha API
                String url = "http://www.seakl.neduet.edu.pk/api/v1/items?external_id=" + barcode.trim();

                URI uri = new URI(url);
                HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                //conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int status = conn.getResponseCode();
                InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //conn.disconnect();
                
                JSONArray jsonArray = new JSONArray(response.toString());
                return jsonArray.length() > 0 ? jsonArray.getJSONObject(0) : null;
       
            } catch (Exception ex) {
                //error = ex.getMessage();
                ex.printStackTrace();
            }
            return null;
      
    }
	
	public JSONObject getKohaBiblioByBiblioID(int biblioId) {

        try {
            // Call Koha API
            String url = "http://www.seakl.neduet.edu.pk/api/v1/biblios/" + biblioId;

            URI uri = new URI(url);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            //conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Authorization", AuthBasic.getBasicAuthHeader());
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //conn.disconnect();
            
            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject;
            
        } catch (Exception ex) {
            //error = ex.getMessage();
            ex.printStackTrace();
        }
        return null;
  
}

}