package com.liu.thailink.controller;

import com.liu.thailink.controller.dto.TotalPieChartDTO;
import com.liu.thailink.service.StudyService;
import com.liu.thailink.service.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private StudyService ss;

    @Autowired
    private VisaService vs;

    @GetMapping("/pie")
    public TotalPieChartDTO getPie (){
        TotalPieChartDTO tpc = new TotalPieChartDTO();
        tpc.setStudyTotal(ss.count());
        tpc.setVisaTotal(vs.count());
        return tpc;
    }

}
