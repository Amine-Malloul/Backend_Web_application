package com.example.WebApplication.config;

import com.example.WebApplication.Model.Student;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class StudentPDFExpoter {

    private List<Student> studentList = null;

    public StudentPDFExpoter(List<Student> studentList) {
        this.studentList = studentList;
    }


    // Method to write the header of the PDF table
    private void HeaderTableWriter(PdfPTable table){

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.decode("#A0522D"));
        cell.setPadding(7);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        String[] pdfHeader = {"Nom", "Prénom", "CIN", "Note Finale"};
        for (String string : pdfHeader){
            cell.setPhrase(new Phrase(string, font));
            table.addCell(cell);
        }
    }

    // Method to write the data rows of the PDF table
    private void DataTableWriter(PdfPTable table){
        for (Student student : studentList){
            table.addCell(student.getLastname());
            table.addCell(student.getName());
            table.addCell(student.getCin());
            table.addCell(String.valueOf(student.getNote()));
        }
    }

    // Method to export the student list to a PDF file
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(20);
        font.setColor(Color.black);

        Font font2 = FontFactory.getFont(FontFactory.HELVETICA);
        font2.setSize(15);
        font2.setColor(Color.black);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3f, 3f});
        table.setSpacingBefore(10);

        // Load images for the document
        Image fsoimage = Image.getInstance("src/main/resources/photos/Extras/logoFSO.png");
        Image umpimage = Image.getInstance("src/main/resources/photos/Extras/logoUMP.png");

        // Calculate page size and positioning for images
        float pagesizeheight = document.getPageSize().getHeight();
        float pagesizewidth = document.getPageSize().getWidth();
        float wid = 150f;
        fsoimage.scaleToFit(100f, 200f);
        fsoimage.setAbsolutePosition(10f, pagesizeheight -150f );

        umpimage.scaleToFit(wid, 280f);
        umpimage.setAbsolutePosition( pagesizewidth - wid , pagesizeheight - 150f);

        document.add(fsoimage);
        document.add(umpimage);

        // Add blank lines for spacing
        document.add(new Paragraph("\n\n\n\n\n\n\n\n"));

        // Add a title for the document
        Paragraph p = new Paragraph("Master Ingénierie Informatique(M2I)" , font2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        document.add(new Paragraph("\n\n"));

        // Add a subtitle for the document
        Paragraph pp = new Paragraph("La liste des étudiants" , font);
        pp.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(pp);


        // Write the header and data rows to the table
        HeaderTableWriter(table);
        DataTableWriter(table);
        document.add(table);
        document.close();

    }
}
