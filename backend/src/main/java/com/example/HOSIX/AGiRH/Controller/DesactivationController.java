package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Service.DesactivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/residents")
public class DesactivationController {

    @Autowired
    private DesactivationService desactivationService;

    @GetMapping("/desactiver-inactifs")
    public String desactiverInactifs() {
        desactivationService.desactiverResidents();
        return "Tous les comptes inactifs ont été désactivés.";
    }
}
