package com.liu.thailink.controller.dto;

import lombok.Data;

//this is used to contain the fetched data from study, customer, service tables
@Data
public class StudyServiceDTO {
    private Integer serviceID;
    private String infoID;
    private String serviceType;
    private Double price;
    private Double paid;

    private Integer studyID;
    private String bloodType;
    private String religion;
    private String formerSchool;
    private String fatherName;
    private String motherName;
    private String applySchool;
    private String status;
    private String studyComment;
    private String updated;
    private String createTime;

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
