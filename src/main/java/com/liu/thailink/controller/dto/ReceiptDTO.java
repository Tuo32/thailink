package com.liu.thailink.controller.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

//this is an object for transfer receipt data
@Data
public class ReceiptDTO {
    private String customerName;
    private String receiptCode;
    private LocalDate receiptDate;
    private List<Double> amounts;
    private List<String> descriptions;
}
