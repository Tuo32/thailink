package com.liu.thailink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.controller.dto.VisaServiceDTO;
import com.liu.thailink.entities.Customer;
import com.liu.thailink.entities.Finance;
import com.liu.thailink.entities.Study;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.CustomerMapper;
import com.liu.thailink.mapper.FinanceMapper;
import com.liu.thailink.mapper.ServiceMapper;
import com.liu.thailink.mapper.VisaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@Service
public class VisaService extends ServiceImpl<VisaMapper, Visa> {
    @Resource
    private VisaMapper visaMapper;
    @Resource
    private ServiceMapper serviceMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private FinanceMapper fm;
    @Transactional
    public boolean saveVisa(Visa visa)
    {
        try{
            if(visa.getVisaID() != null) {
                return  saveOrUpdate(visa);
            }
            visaMapper.insert(visa);
            com.liu.thailink.entities.Service service = new com.liu.thailink.entities.Service();
            service.setServiceType("Visa");
            service.setInfoID(visa.getVisaID());
            service.setCustomerID(visa.getCustomerID());
            service.setPrice(visa.getPrice());
            serviceMapper.insert(service);

            return true;
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input");
        }
    }

    @Transactional
    public boolean statusUpdate(String visaID, String status) {
        Visa visa = getById(visaID);
        String nextStatus = "";
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("infoID", visaID);
        qw.eq("serviceType", "Visa");

        switch (status){
            case "Phase1 processing":
                nextStatus = "Phase2 unpaid";
                break;
            case "Phase2 processing":
                nextStatus = "Completed";
                break;
            case "Completed":
                throw new ServiceException("CODE_400", "can not proceed to next level");

            default:
                throw new ServiceException("CODE_400", "You cannot proceed: " + status);
        }


        visa.setStatus(nextStatus);
        LocalDateTime update = LocalDateTime.now();
        visa.setUpdated(update.toString());
        return updateById(visa);
    }

    //this method is used to update service status when a finance record is created.
    //the transcation is nested so that if finance method fail, this method will also turn database back
    @Transactional(propagation = Propagation.NESTED)
    public boolean statusUpdateFinance(String visaID, String status) {
        Visa visa = getById(visaID);
        String nextStatus = "";
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("infoID", visaID);
        qw.eq("serviceType", "Visa");

        switch (status){
            case "Phase1 unpaid":
                nextStatus = "Phase1 processing";
                break;
            case "Phase2 unpaid":
                nextStatus = "Phase2 processing";
                break;
            default:
                throw new ServiceException("CODE_400", "You cannot proceed: " + status);
        }
        visa.setStatus(nextStatus);
        LocalDateTime update = LocalDateTime.now();
        visa.setUpdated(update.toString());
        return updateById(visa);
    }

    public boolean statusRevert(String visaID, String status) {
        Visa visa = getById(visaID);

        String timestampStr = visa.getUpdated();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, formatter);

        Instant instant1 = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = Instant.now();

        long diffInMinutes = ChronoUnit.MINUTES.between(instant1, instant2);


// only allow to revert the services which is updated within 5 minutes
        if (Math.abs(diffInMinutes) <= 5) {
            String previousStatus;

            switch (status){
                case "Phase1 unpaid":
                    throw new ServiceException("CODE_400", "can not revert status");
                case "Phase1 processing":
                    visa.setStatus("Phase1 unpaid");
                    break;
                case "Phase2 unpaid":
                    visa.setStatus("Phase1 processing");
                    break;
                case "Phase2 processing":
                    break;
                case "Completed":
                    visa.setStatus("Phase2 processing");
            }
            //set the updated time to be 6 minutes ago, to prevent multiple revert
            LocalDateTime update = LocalDateTime.now().minusMinutes(6);
            visa.setUpdated(update.toString());
            visaMapper.updateById(visa);
        } else {
            throw new ServiceException("CODE_400", "can not revert, it has been more than 5 minutes");
        }

         return true;
    }


    //fetch customer, service, visa record
    public VisaServiceDTO join(String visaID) {
        Visa visa = getById(visaID);

        QueryWrapper<com.liu.thailink.entities.Service> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("infoID", visaID);
        queryWrapper.eq("serviceType", "Visa");
        com.liu.thailink.entities.Service service = serviceMapper.selectOne(queryWrapper);
        VisaServiceDTO vsd = new VisaServiceDTO();

        vsd.setServiceID(service.getServiceID());
        vsd.setPrice(service.getPrice());
        vsd.setPaid(service.getPaid());

        vsd.setVisaID(visa.getVisaID());
        vsd.setVisaType(visa.getVisaType());
        vsd.setVisaComment(visa.getComment());
        vsd.setVisaStatus(visa.getStatus());
        vsd.setVisaUpdated(visa.getUpdated());
        vsd.setVisaCreated(visa.getCreateTime());

        Customer customer = customerMapper.selectById(service.getCustomerID());
        vsd.setCustomerID(customer.getCustomerID());
        vsd.setName(customer.getName());
        vsd.setGender(customer.getGender());
        vsd.setBirthDate(customer.getBirthDate());
        vsd.setBirthPlace(customer.getBirthPlace());
        vsd.setPassport(customer.getPassport());
        vsd.setAddress(customer.getAddress());
        vsd.setPostcode(customer.getPostcode());
        vsd.setPhone(customer.getPhone());
        vsd.setEmail(customer.getEmail());
        vsd.setCustomerComment(customer.getComment());

        return vsd;
    }



}
