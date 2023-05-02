package com.liu.thailink;

import com.liu.thailink.controller.dto.FinanceLineChart;
import com.liu.thailink.entities.Finance;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.FinanceMapper;
import com.liu.thailink.mapper.ServiceMapper;
import com.liu.thailink.service.FinanceService;
import com.liu.thailink.service.StudyService;
import com.liu.thailink.service.VisaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceServiceTest {

    private FinanceService financeService;

    @Mock
    private FinanceMapper financeMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private VisaService visaService;

    @Mock
    private StudyService studyService;

    @BeforeEach
    public void setUp() {
        financeService = new FinanceService();
    }

    @Test
    public void saveFinanceTest() throws Exception {
        Finance finance = new Finance();
        finance.setAmount(100.0);
        finance.setServiceID(1);

        com.liu.thailink.entities.Service service = new com.liu.thailink.entities.Service();
        service.setServiceID(1);
        service.setPaid(50.0);
        service.setInfoID(1);
        service.setServiceType("Visa");

        when(serviceMapper.selectById(1)).thenReturn(service);

        Visa visa = new Visa();
        visa.setVisaID(1);
        visa.setStatus("Pending");

        when(visaService.getById(1)).thenReturn(visa);

        boolean result = financeService.saveFinance(finance);
        Assertions.assertTrue(result);
        verify(financeMapper, times(1)).insert(finance);
        verify(serviceMapper, times(1)).selectById(1);
        verify(serviceMapper, times(1)).updateById(service);
        verify(visaService, times(1)).statusUpdateFinance("1", "Pending");
    }

    @Test
    public void saveFinanceTest_Exception() {
        Finance finance = new Finance();
        finance.setAmount(100.0);
        finance.setServiceID(1);

        when(serviceMapper.selectById(anyInt())).thenThrow(ServiceException.class);

        Assertions.assertThrows(ServiceException.class, () -> {
            financeService.saveFinance(finance);
        });

        verify(financeMapper, times(0)).insert(finance);
        verify(serviceMapper, times(1)).selectById(anyInt());
        verify(serviceMapper, times(0)).updateById(any());
    }

    @Test
    public void testGetLineChartData() {
        String startDate = "2022-01-01";
        String endDate = "2022-12-31";

        List<Double> mockAmounts = Arrays.asList(100.0, 200.0, 300.0);
        List<String> mockDates = Arrays.asList("2022-01-01", "2022-02-01", "2022-03-01");

        when(financeMapper.selectAmountForLineChart(startDate, endDate)).thenReturn(mockAmounts);
        when(financeMapper.selectDateForLineChart(startDate, endDate)).thenReturn(mockDates);

        FinanceLineChart result = financeService.getLineChartData(startDate, endDate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockDates, result.getDates());
        Assertions.assertEquals(mockAmounts, result.getAmounts());

        verify(financeMapper, times(1)).selectAmountForLineChart(startDate, endDate);
        verify(financeMapper, times(1)).selectDateForLineChart(startDate, endDate);
    }

    @Test
    public void testSelectSum() {
        String startDate = "2022-01-01";
        String endDate = "2022-12-31";

        Double mockSum1 = 1000.0;
        Double mockSum2 = 500.0;

        when(financeMapper.selectSum(startDate, endDate)).thenReturn(mockSum1);
        when(financeMapper.selectSumHistory(startDate, endDate)).thenReturn(mockSum2);

        Double result = financeService.selectSum(startDate, endDate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockSum1 + mockSum2, result);

        verify(financeMapper, times(1)).selectSum(startDate, endDate);
        verify(financeMapper, times(1)).selectSumHistory(startDate, endDate);
    }
}