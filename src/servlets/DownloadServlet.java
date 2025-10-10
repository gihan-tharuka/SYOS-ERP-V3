package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import template.*;
import model.BatchSelection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@WebServlet("/download/*")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String format = request.getParameter("format");
        
        if (pathInfo == null || format == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Handle download requests
        switch (pathInfo) {
            case "/bill":
                downloadBillReport(request, response, format);
                break;
            case "/stock":
                downloadStockReport(request, response, format);
                break;
            case "/reorder":
                downloadReorderReport(request, response, format);
                break;
            case "/reshelve":
                downloadReshelveReport(request, response, format);
                break;
            case "/daily-sales":
                downloadDailySalesReport(request, response, format);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // Download methods
    private void downloadBillReport(HttpServletRequest request, HttpServletResponse response, String format) 
            throws ServletException, IOException {
        BillReport report = new BillReport();
        report.generateReport();
        
        String filename = "bill_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        
        switch (format.toLowerCase()) {
            case "csv":
                downloadBillReportCSV(response, report.getBillDetails(), filename);
                break;
            case "pdf":
                downloadBillReportPDF(response, report.getBillDetails(), filename);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported format");
        }
    }

    private void downloadStockReport(HttpServletRequest request, HttpServletResponse response, String format) 
            throws ServletException, IOException {
        StockReport report = new StockReport(dao.ReportDAO.getInstance());
        report.generateReport();
        
        String filename = "stock_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        
        switch (format.toLowerCase()) {
            case "csv":
                downloadStockReportCSV(response, report.getStockDetails(), filename);
                break;
            case "pdf":
                downloadStockReportPDF(response, report.getStockDetails(), filename);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported format");
        }
    }

    private void downloadReorderReport(HttpServletRequest request, HttpServletResponse response, String format) 
            throws ServletException, IOException {
        ReorderReport report = new ReorderReport(dao.ReportDAO.getInstance());
        report.generateReport();
        
        String filename = "reorder_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        
        switch (format.toLowerCase()) {
            case "csv":
                downloadReorderReportCSV(response, report.getReorderItems(), filename);
                break;
            case "pdf":
                downloadReorderReportPDF(response, report.getReorderItems(), filename);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported format");
        }
    }

    private void downloadDailySalesReport(HttpServletRequest request, HttpServletResponse response, String format) 
            throws ServletException, IOException {
        String dateStr = request.getParameter("date");
        Date reportDate;
        
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat.parse(dateStr);
                reportDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                reportDate = new Date(System.currentTimeMillis());
            }
        } else {
            reportDate = new Date(System.currentTimeMillis());
        }

        DailySalesReport report = new DailySalesReport();
        report.setReportDate(reportDate);
        report.generateReport();
        
        String filename = "daily_sales_report_" + new SimpleDateFormat("yyyyMMdd").format(reportDate);
        
        switch (format.toLowerCase()) {
            case "csv":
                downloadDailySalesReportCSV(response, report.getItemAggregates(), filename);
                break;
            case "pdf":
                downloadDailySalesReportPDF(response, report.getItemAggregates(), filename);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported format");
        }
    }

    private void downloadReshelveReport(HttpServletRequest request, HttpServletResponse response, String format) 
            throws ServletException, IOException {
        ReshelveReport report = new ReshelveReport(
            new dao.ShelfStockDAO(dao.DatabaseConnection.getInstance().getConnection()),
            new dao.MainStockDAO(dao.DatabaseConnection.getInstance().getConnection()),
            new dao.ItemDAO(dao.DatabaseConnection.getInstance().getConnection(), new observer.ReorderSubject()),
            new strategy.OldestBatchStrategy()
        );
        report.generateReport();
        
        String filename = "reshelve_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        
        switch (format.toLowerCase()) {
            case "csv":
                downloadReshelveReportCSV(response, report.getReshelvingInfo(), report.getReshelfQuantities(), report.getItemNames(), filename);
                break;
            case "pdf":
                downloadReshelveReportPDF(response, report.getReshelvingInfo(), report.getReshelfQuantities(), report.getItemNames(), filename);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported format");
        }
    }

    // CSV Download methods
    private void downloadBillReportCSV(HttpServletResponse response, List<model.Bill> bills, String filename) 
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Serial Number,Total Price,Cash Tendered,Change Amount,Bill Date,Payment Method");
            for (model.Bill bill : bills) {
                writer.printf("%d,%.2f,%.2f,%.2f,%s,%s%n",
                    bill.getSerialNumber(),
                    bill.getTotalPrice(),
                    bill.getCashTendered(),
                    bill.getChangeAmount(),
                    bill.getBillDate(),
                    bill.getPaymentMethod());
            }
        }
    }

    private void downloadStockReportCSV(HttpServletResponse response, List<Object[]> stockDetails, String filename) 
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Item Code,Item Name,Batch Code,Quantity,Expiry Date");
            for (Object[] stock : stockDetails) {
                writer.printf("%s,%s,%s,%d,%s%n",
                    stock[0], stock[1], stock[2], stock[3], stock[4]);
            }
        }
    }

    private void downloadReorderReportCSV(HttpServletResponse response, List<Object[]> reorderItems, String filename) 
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Item Code,Item Name,Current Quantity,Quantity to Reorder");
            for (Object[] item : reorderItems) {
                writer.printf("%s,%s,%d,%d%n",
                    item[0], item[1], item[2], item[3]);
            }
        }
    }

    private void downloadDailySalesReportCSV(HttpServletResponse response, Map<String, DailySalesReport.ItemAggregate> itemAggregates, String filename) 
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Item Code,Item Name,Quantity,Total Price");
            for (DailySalesReport.ItemAggregate aggregate : itemAggregates.values()) {
                writer.printf("%s,%s,%d,%.2f%n",
                    aggregate.getItemCode(),
                    aggregate.getItemName(),
                    aggregate.getQuantity(),
                    aggregate.getTotalPrice());
            }
        }
    }

    private void downloadReshelveReportCSV(HttpServletResponse response, Map<Integer, List<BatchSelection>> reshelvingInfo, Map<Integer, Integer> reshelfQuantities, Map<Integer, String> itemNames, String filename) 
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Item ID,Item Name,Reshelf Quantity,Batch Code,Reshelf Quantity,Expiry Date");
            for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
                int itemId = entry.getKey();
                List<BatchSelection> selectedBatches = entry.getValue();
                int reshelfQuantity = reshelfQuantities.get(itemId);
                String itemName = itemNames.get(itemId);
                
                for (BatchSelection selection : selectedBatches) {
                    writer.printf("%d,%s,%d,%s,%d,%s%n",
                        itemId, itemName, reshelfQuantity, 
                        selection.getBatch().getBatchCode(), 
                        selection.getReshelfQuantity(), 
                        selection.getBatch().getExpiryDate());
                }
            }
        }
    }

    // PDF Download methods (simplified HTML format for now)
    private void downloadBillReportPDF(HttpServletResponse response, List<model.Bill> bills, String filename) 
            throws IOException {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".html\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><title>Bill Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("h1 { color: #333; }");
            writer.println("</style></head><body>");
            writer.println("<h1>Bill Report</h1>");
            writer.println("<p>Generated on: " + new java.util.Date() + "</p>");
            writer.println("<table>");
            writer.println("<tr><th>Serial Number</th><th>Total Price</th><th>Cash Tendered</th><th>Change Amount</th><th>Bill Date</th><th>Payment Method</th></tr>");
            
            for (model.Bill bill : bills) {
                writer.printf("<tr><td>%d</td><td>Rs.%.2f</td><td>Rs.%.2f</td><td>Rs.%.2f</td><td>%s</td><td>%s</td></tr>%n",
                    bill.getSerialNumber(),
                    bill.getTotalPrice(),
                    bill.getCashTendered(),
                    bill.getChangeAmount(),
                    bill.getBillDate(),
                    bill.getPaymentMethod());
            }
            writer.println("</table></body></html>");
        }
    }

    private void downloadStockReportPDF(HttpServletResponse response, List<Object[]> stockDetails, String filename) 
            throws IOException {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".html\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><title>Stock Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("h1 { color: #333; }");
            writer.println("</style></head><body>");
            writer.println("<h1>Stock Report</h1>");
            writer.println("<p>Generated on: " + new java.util.Date() + "</p>");
            writer.println("<table>");
            writer.println("<tr><th>Item Code</th><th>Item Name</th><th>Batch Code</th><th>Quantity</th><th>Expiry Date</th></tr>");
            
            for (Object[] stock : stockDetails) {
                writer.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td></tr>%n",
                    stock[0], stock[1], stock[2], stock[3], stock[4]);
            }
            writer.println("</table></body></html>");
        }
    }

    private void downloadReorderReportPDF(HttpServletResponse response, List<Object[]> reorderItems, String filename) 
            throws IOException {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".html\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><title>Reorder Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("h1 { color: #333; }");
            writer.println("</style></head><body>");
            writer.println("<h1>Reorder Report</h1>");
            writer.println("<p>Generated on: " + new java.util.Date() + "</p>");
            writer.println("<table>");
            writer.println("<tr><th>Item Code</th><th>Item Name</th><th>Current Quantity</th><th>Quantity to Reorder</th></tr>");
            
            for (Object[] item : reorderItems) {
                writer.printf("<tr><td>%s</td><td>%s</td><td>%d</td><td>%d</td></tr>%n",
                    item[0], item[1], item[2], item[3]);
            }
            writer.println("</table></body></html>");
        }
    }

    private void downloadDailySalesReportPDF(HttpServletResponse response, Map<String, DailySalesReport.ItemAggregate> itemAggregates, String filename) 
            throws IOException {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".html\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><title>Daily Sales Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("h1 { color: #333; }");
            writer.println("</style></head><body>");
            writer.println("<h1>Daily Sales Report</h1>");
            writer.println("<p>Generated on: " + new java.util.Date() + "</p>");
            writer.println("<table>");
            writer.println("<tr><th>Item Code</th><th>Item Name</th><th>Quantity</th><th>Total Price</th></tr>");
            
            for (DailySalesReport.ItemAggregate aggregate : itemAggregates.values()) {
                writer.printf("<tr><td>%s</td><td>%s</td><td>%d</td><td>Rs.%.2f</td></tr>%n",
                    aggregate.getItemCode(),
                    aggregate.getItemName(),
                    aggregate.getQuantity(),
                    aggregate.getTotalPrice());
            }
            writer.println("</table></body></html>");
        }
    }

    private void downloadReshelveReportPDF(HttpServletResponse response, Map<Integer, List<BatchSelection>> reshelvingInfo, Map<Integer, Integer> reshelfQuantities, Map<Integer, String> itemNames, String filename) 
            throws IOException {
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".html\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><title>Reshelve Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("h1 { color: #333; }");
            writer.println("</style></head><body>");
            writer.println("<h1>Reshelve Report</h1>");
            writer.println("<p>Generated on: " + new java.util.Date() + "</p>");
            writer.println("<table>");
            writer.println("<tr><th>Item ID</th><th>Item Name</th><th>Reshelf Quantity</th><th>Batch Code</th><th>Reshelf Quantity</th><th>Expiry Date</th></tr>");
            
            for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
                int itemId = entry.getKey();
                List<BatchSelection> selectedBatches = entry.getValue();
                int reshelfQuantity = reshelfQuantities.get(itemId);
                String itemName = itemNames.get(itemId);
                
                for (BatchSelection selection : selectedBatches) {
                    writer.printf("<tr><td>%d</td><td>%s</td><td>%d</td><td>%s</td><td>%d</td><td>%s</td></tr>%n",
                        itemId, itemName, reshelfQuantity, 
                        selection.getBatch().getBatchCode(), 
                        selection.getReshelfQuantity(), 
                        selection.getBatch().getExpiryDate());
                }
            }
            writer.println("</table></body></html>");
        }
    }
} 