package testclients;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class LoginLoadTest {
    private static final Logger logger = Logger.getLogger(LoginLoadTest.class.getName());
    private static final int NUM_CLIENTS = 20;
    private static final String BASE_URL = "http://localhost:8080/SYOS-ERP-V3/login.jsp";
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);
        
        // Test with different user roles
        String[] roles = {"admin", "cashier", "customer"};
        
        for (int i = 0; i < NUM_CLIENTS; i++) {
            final int userId = i;
            final String role = roles[i % roles.length];
            executor.submit(() -> {
                try {
                    sendLoginRequest("user" + userId, "password" + userId, role, userId);
                    latch.countDown();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error in client " + userId, e);
                }
            });
        }
        
        try {
            latch.await();
            logger.info("All login requests completed");
            executor.shutdown();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupted while waiting for tests to complete", e);
        }
    }
    
    private static void sendLoginRequest(String username, String password, String role, int clientId) throws Exception {
        String urlParameters = "username=" + URLEncoder.encode(username, "UTF-8") +
                             "&password=" + URLEncoder.encode(password, "UTF-8") +
                             "&role=" + URLEncoder.encode(role, "UTF-8");
        
        byte[] postData = urlParameters.getBytes("UTF-8");
        int postDataLength = postData.length;
        
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        
        long startTime = System.currentTimeMillis();
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        
        int responseCode = conn.getResponseCode();
        long endTime = System.currentTimeMillis();
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            
            logger.info(String.format("Client %d (%s) - Response Code: %d, Time: %dms, Response: %s",
                    clientId, role, responseCode, (endTime - startTime), response.toString()));
        }
    }
}
