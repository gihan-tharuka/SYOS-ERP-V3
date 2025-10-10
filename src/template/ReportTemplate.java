package template;

import java.util.Date;

public abstract class ReportTemplate {
    private String reportName;
    protected Date reportDate;


    public final void generateReport() {
        fetchData();
        processData();
        generateOutput();
    }

    protected abstract void fetchData();
    protected abstract void processData();
    protected abstract void generateOutput();


    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}


