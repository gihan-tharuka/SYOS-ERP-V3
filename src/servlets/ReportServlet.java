package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import template.*;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/reports/*")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Show report menu
            request.getRequestDispatcher("/jsp/report/reportMenu.jsp").forward(request, response);
            return;
        }

        // Handle specific report requests
        switch (pathInfo) {
            case "/bill":
                generateBillReport(request, response);
                break;
            case "/stock":
                generateStockReport(request, response);
                break;
            case "/reorder":
                generateReorderReport(request, response);
                break;
            case "/reshelve":
                generateReshelveReport(request, response);
                break;
            case "/daily-sales":
                generateDailySalesReport(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void generateBillReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        BillReport report = new BillReport();
        report.generateReport();
        request.setAttribute("report", report);
        request.getRequestDispatcher("/jsp/report/billReport.jsp").forward(request, response);
    }

    private void generateStockReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        StockReport report = new StockReport(dao.ReportDAO.getInstance());
        report.generateReport();
        request.setAttribute("report", report);
        request.getRequestDispatcher("/jsp/report/stockReport.jsp").forward(request, response);
    }

    private void generateReorderReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        ReorderReport report = new ReorderReport(dao.ReportDAO.getInstance());
        report.generateReport();
        request.setAttribute("report", report);
        request.getRequestDispatcher("/jsp/report/reorderReport.jsp").forward(request, response);
    }

    private void generateReshelveReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        ReshelveReport report = new ReshelveReport(
            dao.ShelfStockDAO.getInstance(),
            dao.MainStockDAO.getInstance(),
            dao.ItemDAO.getInstance(),
            strategy.BatchSelectionStrategy.getInstance()
        );
        report.generateReport();
        request.setAttribute("report", report);
        request.getRequestDispatcher("/jsp/report/reshelveReport.jsp").forward(request, response);
    }

    private void generateDailySalesReport(HttpServletRequest request, HttpServletResponse response) 
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
        request.setAttribute("report", report);
        request.getRequestDispatcher("/jsp/report/dailySalesReport.jsp").forward(request, response);
    }
}    