package net.scnetwork.positioner.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import net.scnetwork.positioner.domain.Report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PdfCreator {
    private String path;

    public PdfCreator(){
        this.path = UUID.randomUUID().toString();
    }

    public String CreateReport(ArrayList<Report> arg, String name, String from, String to){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));

            BaseFont bf = BaseFont.createFont("/usr/share/fonts/corefonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 16, Font.BOLD);
            Font fontPeriod = new Font(bf, 12, Font.BOLDITALIC);
            Font font = new Font(bf, 8, Font.NORMAL);

            Paragraph title = new Paragraph("Отчет по перемещениям ТС: " + name, fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);

            Paragraph period = new Paragraph("Период с: " + from + " по " + to, fontPeriod);
            period.setAlignment(Element.ALIGN_CENTER);

            Paragraph blank = new Paragraph(" ", fontTitle);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100.0f);
            table.setWidths(new int[]{2, 3, 3, 1});

            PdfPCell titleData = new PdfPCell(new Paragraph("Дата", font));
            titleData.setBorder(Rectangle.BOTTOM);
            titleData.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell titleLat = new PdfPCell(new Paragraph("Широта", font));
            titleLat.setBorder(Rectangle.BOTTOM);
            titleLat.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell titleLon = new PdfPCell(new Paragraph("Долгота", font));
            titleLon.setBorder(Rectangle.BOTTOM);
            titleLon.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell titleVelocity = new PdfPCell(new Paragraph("Скорость", font));
            titleVelocity.setBorder(Rectangle.BOTTOM);
            titleVelocity.setHorizontalAlignment(Element.ALIGN_LEFT);

            table.addCell(titleData);
            table.addCell(titleLat);
            table.addCell(titleLon);
            table.addCell(titleVelocity);

            for (Report rep : arg){
                PdfPCell cellData = new PdfPCell(new Paragraph(rep.getDate(), font));
                cellData.setBorder(Rectangle.BOTTOM);
                cellData.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell cellLat = new PdfPCell(new Paragraph(rep.getLatitude(), font));
                cellLat.setBorder(Rectangle.BOTTOM);
                cellLat.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell cellLot = new PdfPCell(new Paragraph(rep.getLongitude(), font));
                cellLot.setBorder(Rectangle.BOTTOM);
                cellLot.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell cellVelocity = new PdfPCell(new Paragraph(rep.getVelocity(), font));
                cellVelocity.setBorder(Rectangle.BOTTOM);
                cellVelocity.setHorizontalAlignment(Element.ALIGN_LEFT);

                table.addCell(cellData);
                table.addCell(cellLat);
                table.addCell(cellLot);
                table.addCell(cellVelocity);
            }

            document.open();
            document.add(title);
            document.add(period);
            document.add(blank);
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return this.path;
    }
}
