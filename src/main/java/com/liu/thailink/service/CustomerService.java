package com.liu.thailink.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.entities.Customer;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    public boolean saveCustomer(Customer cs)
    {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            cs.setModifyTime(currentTime);
            return  saveOrUpdate(cs);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input");
        }
    }


    public boolean delete(Integer id) {
        try{
            return removeById(id);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, the record may have relative records.");
        }

    }

    public boolean batchDelete(List<Integer> ids) {
        try{
            return removeBatchByIds(ids);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, the record may have relative records.");
        }
    }

    public boolean clearCustomer(List<Integer> ids){
        try{
            for(Integer id : ids) {
                Customer cu = getById(id);
                cu.setCustomerID(id);
                cu.setAddress(null);
                cu.setComment(null);
                cu.setBirthDate(null);
                cu.setBirthPlace(null);
                cu.setPostcode(null);
                cu.setPassport(null);
                saveOrUpdate(cu);
            }
        }catch (Exception e){
                throw new ServiceException("CODE_500", "system error");
            }
        return true;
    }
}
