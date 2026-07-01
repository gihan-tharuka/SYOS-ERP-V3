package com.syos.erp.report;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Read-only inventory and reorder reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reorder")
    public List<ReorderAlertResponse> getReorderReport() {
        return reportService.getReorderAlerts();
    }

    @GetMapping("/stock")
    public List<StockSummaryResponse> getStockReport() {
        return reportService.getStockSummary();
    }
}
