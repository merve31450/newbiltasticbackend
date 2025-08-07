package org.u2soft.billtasticbackend.dto;

import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;

public class InvoicePdfGenerator {

    public static ByteArrayOutputStream createInvoicePdf(InvoiceRequest req) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("FATURA", titleFont));
        document.add(new Paragraph("Şirket: " + req.getCompanyName(), normalFont));
        document.add(new Paragraph("Adres: " + req.getAddress(), normalFont));
        document.add(new Paragraph("Telefon: " + req.getPhone(), normalFont));
        document.add(new Paragraph("E-posta: " + req.getEmail(), normalFont));
        document.add(new Paragraph("Website: " + req.getWebsite(), normalFont));
        document.add(new Paragraph("Banka Hesabı: " + req.getBankAccount(), normalFont));
        document.add(new Paragraph("Fatura No: " + req.getInvoiceNo(), normalFont));
        document.add(new Paragraph("Tarih: " + req.getDate(), normalFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Ürün");
        table.addCell("Birim Fiyat");
        table.addCell("Adet");
        table.addCell("Ara Toplam");

        for (InvoiceRequest.InvoiceItem item : req.getItems()) {
            table.addCell(item.getDescription());
            table.addCell(item.getUnitPrice());
            table.addCell(item.getQuantity());
            double subtotal = Double.parseDouble(item.getUnitPrice()) * Integer.parseInt(item.getQuantity());
            table.addCell(String.format("%.2f", subtotal));
        }

        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Toplam Tutar: " + req.getTotalAmount() + " TL", normalFont));

        document.close();
        return out;
    }
}
