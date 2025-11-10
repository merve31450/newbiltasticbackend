
package org.u2soft.billtasticbackend.dto;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class InvoicePdfGenerator {

    public static ByteArrayOutputStream createInvoicePdf(InvoiceRequest req) throws Exception {
        Document document = new Document(PageSize.A4, 40, 40, 50, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        /* =====================================================
           🧾 Üst Bilgiler
        ===================================================== */
        document.add(new Paragraph("FATURA", titleFont));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Şirket: " + safe(req.getCompanyName()), normalFont));
        document.add(new Paragraph("Adres: " + safe(req.getAddress()), normalFont));
        document.add(new Paragraph("Telefon: " + safe(req.getPhone()), normalFont));
        document.add(new Paragraph("E-posta: " + safe(req.getEmail()), normalFont));
        document.add(new Paragraph("Website: " + safe(req.getWebsite()), normalFont));
        document.add(new Paragraph("Banka Hesabı: " + safe(req.getBankAccount()), normalFont));
        document.add(new Paragraph("Fatura No: " + safe(req.getInvoiceNo()), normalFont));
        document.add(new Paragraph("Tarih: " + safe(req.getDate()), normalFont));
        document.add(Chunk.NEWLINE);

        /* =====================================================
           📦 Ürün Tablosu
        ===================================================== */
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 2f, 2f, 2f, 2f});

        addHeaderCell(table, "Ürün");
        addHeaderCell(table, "₺ Fiyat");
        addHeaderCell(table, "USD Fiyat");
        addHeaderCell(table, "EUR Fiyat");
        addHeaderCell(table, "Adet");

        if (req.getItems() != null) {
            for (InvoiceRequest.InvoiceItem item : req.getItems()) {
                table.addCell(safe(item.getDescription()));
                table.addCell(format(item.getUnitPrice(), df));
                table.addCell(format(item.getUnitPriceUsd(), df));
                table.addCell(format(item.getUnitPriceEur(), df));
                table.addCell(format(item.getQuantity(), df));
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        /* =====================================================
           💰 Toplamlar + KDV Hesabı
        ===================================================== */
        BigDecimal totalTry = req.getTotalAmount() != null ? req.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal totalUsd = req.getTotalAmountUsd() != null ? req.getTotalAmountUsd() : BigDecimal.ZERO;
        BigDecimal totalEur = req.getTotalAmountEur() != null ? req.getTotalAmountEur() : BigDecimal.ZERO;

        BigDecimal vatRate = new BigDecimal("0.20"); // %20 KDV
        BigDecimal vatTry = totalTry.multiply(vatRate);
        BigDecimal vatUsd = totalUsd.multiply(vatRate);
        BigDecimal vatEur = totalEur.multiply(vatRate);

        BigDecimal grandTry = totalTry.add(vatTry);
        BigDecimal grandUsd = totalUsd.add(vatUsd);
        BigDecimal grandEur = totalEur.add(vatEur);

        document.add(new Paragraph("Net Tutar (₺): " + format(totalTry, df) + " ₺", normalFont));
        document.add(new Paragraph("KDV (%20): " + format(vatTry, df) + " ₺", normalFont));
        document.add(new Paragraph("Genel Toplam (₺): " + format(grandTry, df) + " ₺", boldFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Net Tutar (USD): " + format(totalUsd, df) + " $", normalFont));
        document.add(new Paragraph("KDV (%20): " + format(vatUsd, df) + " $", normalFont));
        document.add(new Paragraph("Genel Toplam (USD): " + format(grandUsd, df) + " $", boldFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Net Tutar (EUR): " + format(totalEur, df) + " €", normalFont));
        document.add(new Paragraph("KDV (%20): " + format(vatEur, df) + " €", normalFont));
        document.add(new Paragraph("Genel Toplam (EUR): " + format(grandEur, df) + " €", boldFont));

        document.close();
        return out;
    }

    /* =====================================================
       Yardımcı Metotlar
    ===================================================== */
    private static void addHeaderCell(PdfPTable table, String text) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        table.addCell(cell);
    }

    private static String safe(String text) {
        return text != null ? text : "-";
    }

    private static String format(BigDecimal value, DecimalFormat df) {
        return value != null ? df.format(value) : "-";
    }
}
