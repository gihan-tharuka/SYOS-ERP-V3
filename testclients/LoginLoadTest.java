package testclients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginLoadTest {
    private static final String BASE_URL = "http://localhost:8080/SYOS-ERP-V3";
    private static final int NUM_CONCURRENT_REQUESTS = 10;
    private static final int REQUEST_TIMEOUT_SECONDS = 30;

    public static void main(String[] args) {
        System.out.println("Starting Login Load Test...");
        System.out.println("Number of concurrent requests: " + NUM_CONCURRENT_REQUESTS);
        
        // Create a thread pool for concurrent requests
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CONCURRENT_REQUESTS);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // Create HTTP client with timeout
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                .build();

        // Launch concurrent requests
        for (int i = 0; i < NUM_CONCURRENT_REQUESTS; i++) {
            final int requestId = i + 1;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    sendLoginRequest(client, requestId);
                } catch (Exception e) {
                    System.err.println("Request " + requestId + " failed: " + e.getMessage());
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // Shutdown executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Load test completed!");
    }

    private static void sendLoginRequest(HttpClient client, int requestId) throws IOException, InterruptedException {
        System.out.println("Sending request " + requestId + "...");
        
        // Create form data
        String formData = String.format("username=testuser%d&password=testpass%d&role=cashier", requestId, requestId);
        
        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                .build();

        // Send request and get response
        long startTime = System.currentTimeMillis();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.currentTimeMillis();
        
        // Print only essential information
        System.out.printf("Request %d - Status: %d, Time: %d ms%n", 
                requestId, response.statusCode(), (endTime - startTime));
        
        if (response.statusCode() == 302) {
            System.out.println("Request " + requestId + " was redirected to: " + response.headers().firstValue("Location").orElse("unknown"));
        }
    }
} 