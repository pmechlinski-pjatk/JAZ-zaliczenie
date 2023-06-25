package com.pjatk.nbp.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Entry {
//    @Schema(accessMode = Schema.AccessMode.AUTO)
    private String currency;
    private int queryDays;
    private double result;
    private Date date;
    private Timestamp timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Entry() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Entry(String currency, int queryDays, double result, Date date) {
        this.currency = currency;
        this.queryDays = queryDays;
        this.result = result;
        this.date = date;
        this.timestamp = new Timestamp(date.getTime());
    }
}
