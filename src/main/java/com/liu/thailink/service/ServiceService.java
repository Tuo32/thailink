package com.liu.thailink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liu.thailink.entities.Service;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@org.springframework.stereotype.Service
public class ServiceService extends ServiceImpl<ServiceMapper, com.liu.thailink.entities.Service> {

    @Resource
    private ServiceMapper serviceMapper;
    @Resource
    private FinanceMapper financeMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private VisaMapper visaMapper;

    @Resource
    private StudyMapper studyMapper;

    public boolean saveService(Service service)
    {
        try{
            return  saveOrUpdate(service);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input");
        }
    }

    public boolean removeService(Integer id)
    {
        try{
            return  removeById(id);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, cannot delete");
        }
    }

    @Transactional
    public boolean history(Integer id) {

            try {

                    //remove record from study or visa table
                    String serviceType = serviceMapper.getServiceType(id);
                    String infoID = serviceMapper.getInfoID(id);
                    switch (serviceType){
                        case "Visa":
                            visaMapper.deleteById(infoID);
                            break;
                        case "Study":
                            studyMapper.deleteById(infoID);
                            break;
                    }

                    //move the service and finance record to service_history and finance_history
                    serviceMapper.moveToHistory(id);
                    serviceMapper.moveFinanceToHistory(id);

                    //remove from original tables and relative files
                    QueryWrapper queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("serviceID", id);
                    financeMapper.delete(queryWrapper);
                    fileMapper.delete(queryWrapper);
                    serviceMapper.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new ServiceException("CODE_500", "system error");
            }


    }
}
