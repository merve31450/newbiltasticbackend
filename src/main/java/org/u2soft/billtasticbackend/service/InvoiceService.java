package org.u2soft.billtasticbackend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import org.u2soft.billtasticbackend.entity.Invoice;
import org.u2soft.billtasticbackend.entity.InvoiceItem;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.repository.InvoiceRepository;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    /* ============================================================
       KULLANICI GETİR
    ============================================================ */
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        return userRepository.findByEmail(email);
    }

    /* ============================================================
       TÜM FATURALARI GETİR
    ============================================================ */
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findByUserId(getCurrentUser().getId());
    }

    /* ============================================================
       CREATE
    ============================================================ */
    @Transactional
    public Invoice saveInvoiceFromRequest(InvoiceRequest req) {

        User currentUser = getCurrentUser();
        Invoice invoice = new Invoice();
        invoice.setUser(currentUser);

        mapBasicFields(req, invoice);
        mapItems(req, invoice);

        return invoiceRepository.save(invoice);
    }

    /* ============================================================
       UPDATE
    ============================================================ */
    @Transactional
    public Invoice updateInvoiceFromRequest(Long id, InvoiceRequest req) {

        User currentUser = getCurrentUser();
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Bu faturayı güncelleme yetkiniz yok!");
        }

        mapBasicFields(req, invoice);

        invoice.getItems().clear();
        mapItems(req, invoice);

        return invoiceRepository.save(invoice);
    }

    /* ============================================================
       DELETE
    ============================================================ */
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        invoiceRepository.delete(invoice);
    }

    /* ============================================================
       BASIC FIELD MAPPER
    ============================================================ */
    private void mapBasicFields(InvoiceRequest req, Invoice invoice) {

        invoice.setInvoiceNumber(req.getInvoiceNo());
        invoice.setIssueDate(parseDate(req.getDate()));
        invoice.setDueDate(LocalDate.now().plusDays(30));

        invoice.setCompanyName(req.getCompanyName());
        invoice.setAddress(req.getAddress());
        invoice.setPhone(req.getPhone());
        invoice.setEmail(req.getEmail());
        invoice.setWebsite(req.getWebsite());
        invoice.setBankAccount(req.getBankAccount());

        invoice.setAmount(req.getTotalAmount());
        invoice.setAmountUsd(req.getTotalAmountUsd());
        invoice.setAmountEur(req.getTotalAmountEur());
    }

    /* ============================================================
       ITEMS MAPPER
    ============================================================ */
    private void mapItems(InvoiceRequest req, Invoice invoice) {
        if (req.getItems() == null) return;

        req.getItems().forEach(dto -> {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setUnitPriceUsd(dto.getUnitPriceUsd());
            item.setUnitPriceEur(dto.getUnitPriceEur());
            invoice.getItems().add(item);
        });
    }

    private LocalDate parseDate(String dateStr) {
        try { return LocalDate.parse(dateStr); }
        catch (Exception e) { return LocalDate.now(); }
    }

    /* ============================================================
       GET BY ID
    ============================================================ */
    public Invoice getInvoiceById(Long id) {

        User currentUser = getCurrentUser();

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı"));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Yetki yok!");
        }

        return invoice;
    }

    public byte[] generateInvoicePdf(Long id) throws Exception {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);

        PdfWriter.getInstance(doc, out);
        doc.open();

        // Renkler
        BaseColor navy = new BaseColor(12, 51, 83);
        BaseColor grayLine = new BaseColor(200, 200, 200);

        // Fontlar
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, navy);
        Font bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 10);

        /* ------------------- HEADER BAR ------------------- */
        PdfPTable headerBar = new PdfPTable(1);
        headerBar.setWidthPercentage(100);

        PdfPCell barCell = new PdfPCell(new Phrase(" "));
        barCell.setBackgroundColor(navy);
        barCell.setFixedHeight(25);
        barCell.setBorder(Rectangle.NO_BORDER);
        headerBar.addCell(barCell);

        doc.add(headerBar);

        /* ------------------- TITLE ROW ------------------- */
        PdfPTable titleRow = new PdfPTable(2);
        titleRow.setWidthPercentage(100);
        titleRow.setWidths(new float[]{1, 1});

        PdfPCell leftTitle = new PdfPCell(new Phrase("BILLTASTIC", titleFont));
        leftTitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell rightTitle = new PdfPCell(new Phrase("FATURA", titleFont));
        rightTitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTitle.setBorder(Rectangle.NO_BORDER);

        titleRow.addCell(leftTitle);
        titleRow.addCell(rightTitle);

        doc.add(titleRow);

        /* ------------------- INFO TABLE ------------------- */
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);
        info.setWidths(new float[]{1, 1});

        PdfPCell billTo = new PdfPCell();
        billTo.setBorder(Rectangle.NO_BORDER);
        billTo.addElement(new Paragraph("Bill To:", bold));
        billTo.addElement(new Paragraph(invoice.getCompanyName(), normal));
        billTo.addElement(new Paragraph(invoice.getEmail(), normal));
        billTo.addElement(new Paragraph(invoice.getPhone(), normal));

        PdfPCell rightInfo = new PdfPCell();
        rightInfo.setBorder(Rectangle.NO_BORDER);
        rightInfo.addElement(new Paragraph("Fatura No: " + invoice.getInvoiceNumber(), normal));
        rightInfo.addElement(new Paragraph("Tarih: " + invoice.getIssueDate(), normal));
        rightInfo.addElement(new Paragraph("Banka: " + invoice.getBankAccount(), normal));

        info.addCell(billTo);
        info.addCell(rightInfo);

        doc.add(info);

        /* ------------------- LINE ------------------- */
        LineSeparator line = new LineSeparator();
        line.setLineColor(grayLine);
        doc.add(new Chunk(line));

        /* ------------------- ITEM TABLE ------------------- */
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1, 1});

        String[] headers = {"Açıklama", "Adet", "Birim Fiyat", "Toplam"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, bold));
            cell.setBackgroundColor(new BaseColor(240, 240, 240));
            cell.setPadding(6);
            table.addCell(cell);
        }

        for (InvoiceItem item : invoice.getItems()) {

            BigDecimal unit = item.getUnitPrice() == null
                    ? BigDecimal.ZERO
                    : item.getUnitPrice();

            int qty = item.getQuantity() == null
                    ? 1
                    : item.getQuantity().intValue();

            BigDecimal total = unit.multiply(BigDecimal.valueOf(qty));

            table.addCell(new Phrase(item.getDescription(), normal));
            table.addCell(new Phrase(String.valueOf(qty), normal));
            table.addCell(new Phrase(unit + " ₺", normal));
            table.addCell(new Phrase(total + " ₺", normal));
        }

        doc.add(table);

        /* ------------------- TOTAL ------------------- */
        Paragraph totalText = new Paragraph(
                "Toplam Tutar: " + invoice.getAmount() + " ₺",
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
        );
        totalText.setAlignment(Element.ALIGN_RIGHT);
        totalText.setSpacingBefore(10);
        doc.add(totalText);

        /* ------------------- FOOTER ------------------- */
        Paragraph footer = new Paragraph(
                invoice.getEmail() + "   |   " + invoice.getWebsite() + "   |   " + invoice.getPhone(),
                new Font(Font.FontFamily.HELVETICA, 9)
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(40);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

}
