package com.example.HOSIX.AGiRH.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PDFService {

    public byte[] generateResidentPdf(CHUResident resident) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 80, 80, 50, 50);
        PdfWriter.getInstance(document, baos);
        document.open();

        // --- Ajout du logo centré ---
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/image/chulogo.png");

        if (is != null) {
            Image logo = Image.getInstance(is.readAllBytes());
            logo.scaleToFit(150, 150);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } else {
            System.err.println("⚠️ Logo introuvable dans static/image/chulogo.png");
        }


        // Espace après logo
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("\n\n\n\n"));
        // --- Styles pour le texte ---
        Font boldFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        // --- Contenu centré ---

        // Exemple pour afficher un label + valeur dans un paragraphe centré
        Paragraph p;

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Nom : ", boldFont));
        p.add(new Chunk(resident.getFullName() != null ? resident.getFullName() : "N/A", normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Matricule : ", boldFont));
        p.add(new Chunk(resident.getMatricule() != null ? resident.getMatricule() : "N/A", normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Service : ", boldFont));
        p.add(new Chunk(resident.getServiceAffectation() != null ? resident.getServiceAffectation() : "N/A", normalFont));
        document.add(p);


        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Email : ", boldFont));
        p.add(new Chunk(resident.getEmail() != null ? resident.getEmail() : "N/A", normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Telephone : ", boldFont));
        p.add(new Chunk(resident.getTelephone() != null ? resident.getTelephone() : "N/A", normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Date Affectation : ", boldFont));
        p.add(new Chunk(String.valueOf(resident.getDateAffectation() != null ? resident.getDateAffectation() : "N/A"), normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Type de service : ", boldFont));
        p.add(new Chunk(String.valueOf(resident.getTypeService() != null ? resident.getTypeService() : "N/A"), normalFont));
        document.add(p);

        p = new Paragraph();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk("Statut : ", boldFont));
        p.add(new Chunk(String.valueOf(resident.getStatutGlobal() != null ? resident.getStatutGlobal() : "N/A"), normalFont));
        document.add(p);


        document.close();
        return baos.toByteArray();
    }

}
