package com.liu.thailink.controller.dto;

import lombok.Data;

import java.util.List;

//this is an object to contain the data of financial line chart
@Data
public class FinanceLineChart {
    private List<String> dates;
    private List<Double> amounts;
}
