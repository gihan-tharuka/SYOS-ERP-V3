package com.syos.erp.inventory;

import java.util.List;

import com.syos.erp.report.ReorderAlertResponse;
import com.syos.erp.report.ReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reorder-levels")
@Tag(name = "Reorder Levels", description = "Reorder thresholds and low-stock alerts")
public class ReorderLevelController {

    private final ReorderLevelService reorderLevelService;
    private final ReportService reportService;

    public ReorderLevelController(ReorderLevelService reorderLevelService, ReportService reportService) {
        this.reorderLevelService = reorderLevelService;
        this.reportService = reportService;
    }

    @GetMapping
    public List<ReorderLevelResponse> findAll() {
        return reorderLevelService.findAll();
    }

    @GetMapping("/alerts")
    public List<ReorderAlertResponse> getAlerts() {
        return reportService.getReorderAlerts();
    }

    @GetMapping("/{id}")
    public ReorderLevelResponse findById(@PathVariable Long id) {
        return reorderLevelService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReorderLevelResponse create(@Valid @RequestBody ReorderLevelRequest request) {
        return reorderLevelService.create(request);
    }

    @PutMapping("/{id}")
    public ReorderLevelResponse update(@PathVariable Long id, @Valid @RequestBody ReorderLevelRequest request) {
        return reorderLevelService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reorderLevelService.delete(id);
    }
}
