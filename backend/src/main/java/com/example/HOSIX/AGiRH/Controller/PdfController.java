package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import com.example.HOSIX.AGiRH.Service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private CHURepository chuResidentRepository;

    @GetMapping("/fiche/{id}")
    public ResponseEntity<byte[]> generateFiche(@PathVariable Long id) throws IOException {
        CHUResident resident = chuResidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Résident non trouvé"));

        byte[] pdf = pdfService.generateResidentPdf(resident);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fiche_resident_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

