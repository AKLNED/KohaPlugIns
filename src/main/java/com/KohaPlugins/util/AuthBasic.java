package com.KohaPlugins.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
        session.setMaxInactiveInterval(1 * 60); // 15 minutes in seconds
    }
}