package com.KohaPlugins.util;

import java.text.SimpleDateFormat;
import java.util.Date;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

public class AppUtils {

    // Function to extract only the date from a datetime string (Java 8+)
    public static String extractDate(String datetimeString) {
        try {
            
        	// Define a SimpleDateFormat for the datetime format
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            
            // Parse the datetime string to a Date object
            Date DateTime = dateTimeFormat.parse(datetimeString);
            
            // Now create a SimpleDateFormat for the date only
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            
            // Format the datetime to just the date
            String dateStr = dateFormat.format(DateTime);
            
        	//DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //LocalDateTime datetime = LocalDateTime.parse(datetimeString, inputFormatter);
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to format a Date object to a string
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    // Function to format numbers (e.g., currency formatting)
    public static String formatCurrency(double amount) {
        return String.format("$%.2f", amount);  // Formats the number to 2 decimal places
    }

    // Function to capitalize the first letter of a string
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    // You can add more utility methods as needed (e.g., for string, number, or date manipulation)
}
