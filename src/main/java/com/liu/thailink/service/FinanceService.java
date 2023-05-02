package com.liu.thailink.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.controller.dto.FinanceLineChart;
import com.liu.thailink.entities.Finance;
import com.liu.thailink.entities.Study;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.FinanceMapper;
import com.liu.thailink.mapper.ServiceMapper;
import com.liu.thailink.mapper.StudyMapper;
import com.liu.thailink.mapper.VisaMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

@Service
public class FinanceService extends ServiceImpl<FinanceMapper, Finance> {
    @Resource
    private FinanceMapper fm;
    @Resource
    private ServiceMapper sm;

    @Resource
    private VisaService vs;

    @Resource
    private StudyService ss;

    @Transactional
    public boolean saveFinance(Finance finance)
    {
        try{
            save(finance);
            com.liu.thailink.entities.Service service = sm.selectById(finance.getServiceID());
            Double amount = service.getPaid() + finance.getAmount();
            service.setPaid(amount);
            sm.updateById(service);
            String serviceType = sm.selectById(finance.getServiceID()).getServiceType();
            switch (serviceType){
                case "Visa":
                    Visa visa = vs.getById(service.getInfoID());
                    vs.statusUpdateFinance(String.valueOf(visa.getVisaID()), visa.getStatus());
                    break;
                case "Study":
                    Study study = ss.getById(service.getInfoID());
                    ss.statusUpdateFinance(String.valueOf(study.getStudyID()),study.getStatus());
                    break;
            }

                return true;
        }catch (Exception e){
            throw new ServiceException("CODE_500", "system error, check your input");
        }

    }

    public FinanceLineChart getLineChartData(@Param("startDate") String startDate, @Param("endDate") String endDate) {
        FinanceLineChart flc = new FinanceLineChart();

        List<Double> amounts = fm.selectAmountForLineChart(startDate,endDate);
        List<String> dates = fm.selectDateForLineChart(startDate,endDate);

        flc.setDates(dates);
        flc.setAmounts(amounts);

        return flc;
    }

    public Double selectSum(@Param("startDate") String startDate, @Param("endDate") String endDate) {
        Double total =fm.selectSum(startDate,endDate);
        total += fm.selectSumHistory(startDate, endDate);
        return total;
    }









}
