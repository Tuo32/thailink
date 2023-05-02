package com.liu.thailink;

import com.liu.thailink.controller.dto.VisaServiceDTO;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.service.VisaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(JUnit4.class)
@SpringBootTest
public class VisaServiceTest {

    @Autowired
    private VisaService visaService;

    @Test
    public void testSaveVisa() {
        Visa visa = new Visa();
        visa.setCustomerID(1);
        visa.setPrice(100.0);
        boolean result = visaService.saveVisa(visa);
        Assert.assertTrue(result);
    }

    @Test
    public void testStatusUpdate() {
        String visaID = "1";
        String status = "Phase1 processing";
        boolean result = visaService.statusUpdate(visaID, status);
        Assert.assertTrue(result);
    }

    @Test
    public void testStatusUpdateFinance() {
        String visaID = "1";
        String status = "Phase1 unpaid";
        boolean result = visaService.statusUpdateFinance(visaID, status);
        Assert.assertTrue(result);
    }

    @Test
    public void testStatusRevert() {
        String visaID = "1";
        String status = "Phase1 unpaid";
        boolean result = visaService.statusRevert(visaID, status);
        Assert.assertTrue(result);
    }

    @Test
    public void testJoin() {
        String visaID = "1";
        VisaServiceDTO visaServiceDTO = visaService.join(visaID);
        Assert.assertNotNull(visaServiceDTO);
    }
}
