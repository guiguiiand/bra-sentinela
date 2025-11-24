package com.projeto.reportservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    private static final Logger log = LoggerFactory.getLogger(PdfReportService.class);

    private final ReportService reportService;

    public PdfReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public byte[] generateMonthlyReportPdf(int months) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            buildHeader(document, months);

            List<MonthlyComplaintDetailedReportResponse> data = reportService.getLastMonthsDetailed(months);

            Table table = buildTable(data);
            document.add(table);
            document.close();

            log.info("PDF generated successfully with {} months of data", months);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage());
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private void buildHeader(Document document, int months) {
        document.add(new Paragraph("Relatório Detalhado de Denúncias")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Período: Últimos " + months + " meses")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Gerado em: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
    }

    private Table buildTable(List<MonthlyComplaintDetailedReportResponse> data) {
        float[] columnWidths = {2, 1, 1, 1, 1, 1, 1, 1, 1};
        Table table = new Table(columnWidths);

        table.addHeaderCell("Mês");
        table.addHeaderCell("Total");
        table.addHeaderCell("Pendente");
        table.addHeaderCell("Aberta");
        table.addHeaderCell("Em Análise");
        table.addHeaderCell("Validada");
        table.addHeaderCell("Inconsistente");
        table.addHeaderCell("Média/Dia");
        table.addHeaderCell("Variação %");

        for (MonthlyComplaintDetailedReportResponse row : data) {
            table.addCell(row.monthName() + "/" + row.year());
            table.addCell(String.valueOf(row.total()));
            table.addCell(String.valueOf(row.pending()));
            table.addCell(String.valueOf(row.open()));
            table.addCell(String.valueOf(row.inProgress()));
            table.addCell(String.valueOf(row.validated()));
            table.addCell(String.valueOf(row.inconsistent()));
            table.addCell(String.format("%.2f", row.averagePerDay()));
            table.addCell(String.format("%.2f%%", row.percentageChange()));
        }

        return table;
    }
}
