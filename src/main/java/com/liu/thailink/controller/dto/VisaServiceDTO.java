package com.liu.thailink.controller.dto;

import lombok.Data;

import java.util.Date;

//this is used to contain the fetched data from visa, customer, service tables
@Data
public class VisaServiceDTO {
    private Integer serviceID;
    private String infoID;
    private String serviceType;
    private Double price;
    private Double paid;

    private Integer visaID;
    private String visaType;
    private String visaComment;
    private String visaStatus;
    private String visaUpdated;
    private String visaCreated;

    private Integer customerID;
    private String name;
    private String gender;
    private String birthDate;
    private String birthPlace;
    private String passport;
    private String address;
    private String postcode;
    private String phone;
    private String email;
    private String customerComment;

}
