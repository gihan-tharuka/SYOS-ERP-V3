package testclients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class LoginLoadTest {
    private static final String SERVER_URL = "http://localhost:8080/SYOS-ERP-V3/login";
    private static final int NUM_REQUESTS = 50;
    private static final int NUM_THREADS = 10;
    private static final AtomicInteger successCount = new AtomicInteger(0);
    private static final AtomicInteger failureCount = new AtomicInteger(0);
    private static final List<Long> responseTimes = new CopyOnWriteArrayList<>();
    private static final Random random = new Random();

    // Test user credentials
    private static final String[][] TEST_USERS = {
        {"admin1", "admin123", "admin"},
        {"cashier1", "cashier123", "cashier"},
        {"customer1", "customer123", "customer"}
    };

    public static void main(String[] args) {
        System.out.println("Starting Login Load Test...");
        System.out.println("Total Requests: " + NUM_REQUESTS);
        System.out.println("Concurrent Threads: " + NUM_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_REQUESTS);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_REQUESTS; i++) {
            final int requestId = i + 1;
            executor.submit(() -> {
                try {
                    sendLoginRequest(requestId);
                } catch (Exception e) {
                    System.err.println("Request " + requestId + " failed: " + e.getMessage());
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            long endTime = System.currentTimeMillis();
            printResults(endTime - startTime);
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static void sendLoginRequest(int requestId) throws Exception {
        long requestStartTime = System.currentTimeMillis();

        URL url = new URL(SERVER_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Select random test user
        String[] testUser = TEST_USERS[random.nextInt(TEST_USERS.length)];
        String username = testUser[0];
        String password = testUser[1];
        String role = testUser[2];

        // Build the request parameters
        String postData = String.format(
            "username=%s&password=%s&role=%s",
            username, password, role
        );

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        long responseTime = System.currentTimeMillis() - requestStartTime;
        responseTimes.add(responseTime);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Just read the response to clear the stream but don't print it
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                while (br.readLine() != null) {
                    // Discard the response content
                }
                System.out.println("Request " + requestId + " succeeded in " + responseTime + "ms");
                successCount.incrementAndGet();
            }
        } else {
            System.err.println("Request " + requestId + " failed with response code: " + responseCode);
            failureCount.incrementAndGet();
        }
    }

    private static void printResults(long totalTime) {
        System.out.println("\n=== Test Results ===");
        System.out.println("Total Time: " + totalTime + "ms");
        System.out.println("Successful Requests: " + successCount.get());
        System.out.println("Failed Requests: " + failureCount.get());
        
        if (!responseTimes.isEmpty()) {
            double avgResponseTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);
            long maxResponseTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0);
            long minResponseTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .min()
                    .orElse(0);

            System.out.println("Average Response Time: " + avgResponseTime + "ms");
            System.out.println("Min Response Time: " + minResponseTime + "ms");
            System.out.println("Max Response Time: " + maxResponseTime + "ms");
        }
    }
} 