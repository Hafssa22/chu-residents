package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @GetMapping("/migrate-residents")
    public String migrate() {
        migrationService.migrateResidents();
        return "Migration terminée.";
    }
}

