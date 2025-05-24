package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/reorderlevel/events")
public class ReorderLevelSSEServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();
    private static final AtomicInteger clientCounter = new AtomicInteger(0);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        
        PrintWriter writer = response.getWriter();
        clients.add(writer);
        
        // Send initial connection established message
        writer.write("event: connected\n");
        writer.write("data: " + clientCounter.incrementAndGet() + "\n\n");
        writer.flush();
        
        // Keep the connection alive
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(30000); // Send heartbeat every 30 seconds
                writer.write(": heartbeat\n\n");
                writer.flush();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public static void broadcastUpdate(String eventType, String data) {
        for (PrintWriter client : clients) {
            try {
                client.write("event: " + eventType + "\n");
                client.write("data: " + data + "\n\n");
                client.flush();
            } catch (Exception e) {
                clients.remove(client);
            }
        }
    }
    
    @Override
    public void destroy() {
        for (PrintWriter client : clients) {
            client.close();
        }
        clients.clear();
    }
} 