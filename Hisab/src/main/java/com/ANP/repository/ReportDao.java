package com.ANP.repository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class ReportDao {

    public ResponseEntity<InputStreamResource> fetchPdf ()throws Exception{

            String filepath = ("f:/" + "generatedpdf"+(new Date().getTime()/1000)+".pdf");
            Document document = new Document( PageSize.A4, 20, 20, 20, 20 );
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            PdfWriter.getInstance(document, new FileOutputStream(filepath));
            document.open();
            document.add(new Paragraph("hello world"));
            document.close();

            Path pdfPath = Paths.get(filepath);
            byte[] pdf = Files.readAllBytes(pdfPath);
           ByteArrayInputStream c =  new ByteArrayInputStream(pdf);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=ParasGenerated.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(c));
    }
}
