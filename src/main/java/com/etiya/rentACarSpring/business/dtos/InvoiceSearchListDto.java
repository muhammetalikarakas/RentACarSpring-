package com.etiya.rentACarSpring.business.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceSearchListDto {
    private int id;
    private String invoiceNumber;
    private double invoiceAmount;
    private int countOfRentalDays;
    private LocalDate creationDate;
    private int rentalId;
}
