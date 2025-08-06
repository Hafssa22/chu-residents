package com.example.HOSIX.AGiRH.Event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountDesactivatedEvent {
    private String matricule;
    private String reason;
    private LocalDate date;
    private String phoneNumber;

}
