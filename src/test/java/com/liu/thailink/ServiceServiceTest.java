package com.liu.thailink;

import com.liu.thailink.entities.Service;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.*;
import com.liu.thailink.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceServiceTest {
    @Mock
    private ServiceMapper serviceMapper;
    @Mock
    private FinanceMapper financeMapper;
    @Mock
    private FileMapper fileMapper;
    @Mock
    private VisaMapper visaMapper;
    @Mock
    private StudyMapper studyMapper;
    @InjectMocks
    private ServiceService serviceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveService() {
        Service service = new Service();
        when(serviceService.saveOrUpdate(service)).thenReturn(true);
        boolean result = serviceService.saveService(service);
        assertTrue(result);
        verify(serviceService, times(1)).saveOrUpdate(service);
    }

    @Test
    public void testSaveServiceException() {
        Service service = new Service();
        when(serviceService.saveOrUpdate(service)).thenThrow(new RuntimeException());
        assertThrows(ServiceException.class, () -> serviceService.saveService(service));
        verify(serviceService, times(1)).saveOrUpdate(service);
    }

    @Test
    public void testRemoveService() {
        when(serviceService.removeById(1)).thenReturn(true);
        boolean result = serviceService.removeService(1);
        assertTrue(result);
        verify(serviceService, times(1)).removeById(1);
    }

    @Test
    public void testRemoveServiceException() {
        when(serviceService.removeById(1)).thenThrow(new RuntimeException());
        assertThrows(ServiceException.class, () -> serviceService.removeService(1));
        verify(serviceService, times(1)).removeById(1);
    }

    @Test
    public void testHistory() {
        when(serviceMapper.getServiceType(1)).thenReturn("Visa");
        when(serviceMapper.getInfoID(1)).thenReturn("1");
        when(visaMapper.deleteById("1")).thenReturn(1);
        when(serviceMapper.moveToHistory(1)).thenReturn(1);
        when(serviceMapper.moveFinanceToHistory(1)).thenReturn(1);
        when(financeMapper.delete(any())).thenReturn(1);
        when(fileMapper.delete(any())).thenReturn(1);
        when(serviceMapper.deleteById(1)).thenReturn(1);

        boolean result = serviceService.history(1);
        assertTrue(result);

        verify(serviceMapper, times(1)).getServiceType(1);
        verify(serviceMapper, times(1)).getInfoID(1);
        verify(visaMapper, times(1)).deleteById("1");
        verify(serviceMapper, times(1)).moveToHistory(1);
        verify(serviceMapper, times(1)).moveFinanceToHistory(1);
        verify(financeMapper, times(1)).delete(any());
        verify(fileMapper, times(1)).delete(any());
        verify(serviceMapper, times(1)).deleteById(1);
    }

}
