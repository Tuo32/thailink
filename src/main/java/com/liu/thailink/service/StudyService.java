package com.liu.thailink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.controller.dto.StudyServiceDTO;
import com.liu.thailink.controller.dto.VisaServiceDTO;
import com.liu.thailink.entities.Customer;
import com.liu.thailink.entities.Finance;
import com.liu.thailink.entities.Study;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.CustomerMapper;
import com.liu.thailink.mapper.FinanceMapper;
import com.liu.thailink.mapper.ServiceMapper;
import com.liu.thailink.mapper.StudyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class StudyService extends ServiceImpl<StudyMapper, Study> {

    @Resource
    private CustomerMapper cm;

    @Resource
    private StudyMapper studyMapper;

    @Resource
    private ServiceMapper sm;

    @Resource
    private FinanceMapper fm;


    @Transactional
    public boolean saveStudy(Study study)
    {
        try{
            if(study.getStudyID() != null){
                return  saveOrUpdate(study);
            }
            studyMapper.insert(study);
            com.liu.thailink.entities.Service service = new com.liu.thailink.entities.Service();
            service.setServiceType("Study");
            service.setInfoID(study.getStudyID());
            service.setCustomerID(study.getCustomerID());
            service.setPrice(study.getPrice());
            sm.insert(service);



            return true;

        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input");
        }
    }

    public StudyServiceDTO join(String studyID) {
        Study study = getById(studyID);
        StudyServiceDTO ssd = new StudyServiceDTO();

        ssd.setStudyID(study.getStudyID());
        ssd.setReligion(study.getReligion());
        ssd.setBloodType(study.getBloodType());
        ssd.setFatherName(study.getFatherName());
        ssd.setMotherName(study.getMotherName());
        ssd.setFormerSchool(study.getFormerSchool());
        ssd.setApplySchool(study.getApplySchool());
        ssd.setStudyComment(study.getComment());
        ssd.setStatus(study.getStatus());
        ssd.setUpdated(study.getUpdated());
        ssd.setCreateTime(study.getCreateTime());


        QueryWrapper<com.liu.thailink.entities.Service> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("infoID", studyID);
        queryWrapper.eq("serviceType", "Study");
        com.liu.thailink.entities.Service service = sm.selectOne(queryWrapper);

        ssd.setServiceID(service.getServiceID());
        ssd.setPrice(service.getPrice());
        ssd.setPaid(service.getPaid());

        Customer customer = cm.selectById(service.getCustomerID());
        ssd.setCustomerID(customer.getCustomerID());
        ssd.setName(customer.getName());
        ssd.setGender(customer.getGender());
        ssd.setBirthDate(customer.getBirthDate());
        ssd.setBirthPlace(customer.getBirthPlace());
        ssd.setPassport(customer.getPassport());
        ssd.setAddress(customer.getAddress());
        ssd.setPostcode(customer.getPostcode());
        ssd.setPhone(customer.getPhone());
        ssd.setEmail(customer.getEmail());
        ssd.setCustomerComment(customer.getComment());
        return ssd;
    }

    //this method is used to update service status when a finance record is created.
    //the transcation is nested so that if finance method fail, this method will also turn database back
    @Transactional(propagation = Propagation.NESTED)
    public boolean statusUpdate(String studyID, String status) {
        Study study = getById(studyID);
        String nextStatus = "";

        switch (status){
            case "Phase1 processing":
                nextStatus = "Phase2 unpaid";
                break;
            case "Phase2 processing":
                nextStatus = "Phase3 unpaid";
                break;
            case "Phase3 processing":
                nextStatus = "Completed";
                break;
            case "Completed":
                throw new ServiceException("CODE_400", "can not proceed to next level");
            default:
                throw new ServiceException("CODE_400", "You cannot proceed: " + status);
        }

        study.setStatus(nextStatus);
        LocalDateTime update = LocalDateTime.now();
        study.setUpdated(update.toString());
        return updateById(study);
    }

    @Transactional
    public boolean statusUpdateFinance(String studyID, String status) {
        Study study = getById(studyID);
        String nextStatus = "";

        switch (status){
            case "Phase1 unpaid":
                nextStatus = "Phase1 processing";
                break;
//            case "Phase1 processing":
//                nextStatus = "Phase2 unpaid";
//                break;
            case "Phase2 unpaid":
                nextStatus = "Phase2 processing";
                break;
//            case "Phase2 processing":
//                nextStatus = "Phase3 unpaid";
//                break;
            case "Phase3 unpaid":
                nextStatus = "Phase3 processing";
                break;
//            case "Phase3 processing":
//                nextStatus = "Completed";
//                break;
            case "Completed":
                throw new ServiceException("CODE_400", "can not proceed to next level");
            default:
                throw new ServiceException("CODE_400", "You cannot proceed: " + status);
        }

        study.setStatus(nextStatus);
        LocalDateTime update = LocalDateTime.now();
        study.setUpdated(update.toString());
        return updateById(study);
    }

    public boolean statusRevert(String studyID, String status) {
        Study study = getById(studyID);
        String timestampStr = study.getUpdated();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, formatter);

        Instant instant1 = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = Instant.now();

        long diffInMinutes = ChronoUnit.MINUTES.between(instant1, instant2);

// only allow to revert the services which is updated within 5 minutes
        if (Math.abs(diffInMinutes) <= 5) {
            switch (status){
                case "Phase1 unpaid":
                    throw new ServiceException("CODE_400", "can not revert status");
                case "Phase1 processing":
                    study.setStatus("Phase1 unpaid");
                    break;
                case "Phase2 unpaid":
                    study.setStatus("Phase1 processing");
                    break;
                case "Phase2 processing":
                    study.setStatus("Phase2 unpaid");
                    break;
                case "Phase3 unpaid":
                    study.setStatus("Phase2 processing");
                    break;
                case "Phase3 processing":
                    study.setStatus("Phase3 unpaid");
                    break;
                case "Completed":
                    study.setStatus("Phase3 processing");
            }
            //set the updated time to be 6 minutes ago, to prevent multiple revert
            LocalDateTime update = LocalDateTime.now().minusMinutes(6);
            study.setUpdated(update.toString());
            studyMapper.updateById(study);
        } else {
            throw new ServiceException("CODE_400", "can not revert, it has been more than 5 minutes");
        }

        return true;
    }


}
