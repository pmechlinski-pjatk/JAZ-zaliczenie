package com.pjatk.nbp.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
public class Entry {
//    @Schema(accessMode = Schema.AccessMode.AUTO)
    private String currency;
    private double median;
    private LocalDate startDate;
    private LocalDate endDate;
    private Timestamp timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Entry() {

    }
    public Entry(String currency, LocalDate startDate, LocalDate endDate, double median, Timestamp timestamp) {
        this.currency = currency;
        this.median = median;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timestamp = timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
