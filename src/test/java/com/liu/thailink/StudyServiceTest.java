package com.liu.thailink;

import com.liu.thailink.entities.Customer;
import com.liu.thailink.entities.Service;
import com.liu.thailink.entities.Study;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.CustomerMapper;
import com.liu.thailink.mapper.ServiceMapper;
import com.liu.thailink.mapper.StudyMapper;
import com.liu.thailink.service.StudyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class StudyServiceTest {

    @InjectMocks
    private StudyService studyService;

    @Mock
    private StudyMapper studyMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveStudy() {
        Study study = new Study();
        study.setStudyID(1);
        study.setCustomerID(1);
        study.setPrice(1000.0);

        when(studyMapper.insert(any())).thenReturn(1);
        when(serviceMapper.insert(any())).thenReturn(1);

        studyService.saveStudy(study);
    }

    @Test(expected = ServiceException.class)
    public void testSaveStudyException() {
        Study study = new Study();
        study.setStudyID(1);
        study.setCustomerID(1);
        study.setPrice(1000.0);

        when(studyMapper.insert(any())).thenThrow(ServiceException.class);

        studyService.saveStudy(study);
    }

    @Test
    public void testJoin() {
        Study study = new Study();
        study.setStudyID(1);
        study.setCustomerID(1);
        study.setPrice(1000.0);

        Service service = new Service();
        service.setServiceID(1);
        service.setCustomerID(1);
        service.setPrice(1000.0);

        Customer customer = new Customer();
        customer.setCustomerID(1);
        customer.setName("test");
        customer.setGender("Male");

        when(studyMapper.selectById(any())).thenReturn(study);
        when(serviceMapper.selectOne(any())).thenReturn(service);
        when(customerMapper.selectById(any())).thenReturn(customer);

        studyService.join("1");
    }

    @Test(expected = NullPointerException.class)
    public void testJoinException() {
        when(studyMapper.selectById(any())).thenReturn(null);

        studyService.join("1");
    }

    @Test
    public void testStatusUpdate() {
        Study study = new Study();
        study.setStudyID(1);
        study.setStatus("Phase1 unpaid");

        when(studyMapper.selectById(any())).thenReturn(study);
        when(studyMapper.updateById(any())).thenReturn(1);

        studyService.statusUpdate("1", "Phase1 unpaid");
    }

    @Test(expected = ServiceException.class)
    public void testStatusUpdateException() {
        Study study = new Study();
        study.setStudyID(1);
        study.setStatus("Phase1 unpaid");

        when(studyMapper.selectById(any())).thenReturn(study);

        studyService.statusUpdate("1", "Completed");
    }
}