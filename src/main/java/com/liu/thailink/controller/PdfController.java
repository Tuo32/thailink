package com.liu.thailink.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import cn.hutool.core.io.FileUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.liu.thailink.entities.*;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.httpervletRequest;
import javax.servlet.http.httpervletResponse;

//@CrossOrigin
@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService us;

    @Autowired
    private RoleMenuService rms;


    @Value("${receipts.upload.path}")
    private String receiptsUploadPath;

    @Transactional
    @PostMapping("/generate-receipt")
    public String generateReceipt(@RequestBody List<Integer> ids, @RequestParam Integer requesterID,  httpervletRequest request) throws IOException {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",5);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        Receipt receipt = new Receipt();
        receipt.setUseID(requesterID);
        receiptService.save(receipt);

        List<Finance> financeList = financeService.listByIds(ids);
        Finance finance = financeList.get(0);

        Integer serviceID = finance.getServiceID();
        Service service = serviceService.getById(serviceID);

        Integer customerID = service.getCustomerID();
        Customer customer = customerService.getById(customerID);

        File file = new File();
        file.setServiceID(serviceID);


        try {
            //read the receipt template
            PdfReader reader = new PdfReader(receiptsUploadPath+"shouju.pdf");
            //create output stream
            String newFileName = "receipt"+receipt.getReceiptID()+".pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(receiptsUploadPath + newFileName));

            //get first page
            PdfContentByte page = stamper.getOverContent(1);
            //add text
            Font font = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // customer name
            ColumnText.showTextAligned(page, Element.ALIGN_LEFT, new Phrase(customer.getName(), font), 100, 680, 0);
            // receipt code
            ColumnText.showTextAligned(page, Element.ALIGN_LEFT, new Phrase(String.valueOf(receipt.getReceiptID()), font), 430, 680, 0);
            // receipt time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            ColumnText.showTextAligned(page, Element.ALIGN_LEFT, new Phrase(currentTime, font), 430, 658, 0);
            //Total
            Double total = 0.0;

            // Descriptions and amounts
            int positionX = 70;
            int positionY = 582;
            int positionX2 = 520;
            int positionY2 = 582;
            for ( Finance fin : financeList){
                ColumnText.showTextAligned(page, Element.ALIGN_LEFT, new Phrase(fin.getDescription(), font), positionX, positionY, 0);
                positionY = positionY - 20;

                ColumnText.showTextAligned(page, Element.ALIGN_RIGHT, new Phrase(String.valueOf(fin.getAmount()), font), positionX2, positionY2, 0);
                total += fin.getAmount();
                positionY2 = positionY2 - 20;
            }

            // put total
            Font font2 = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED, 14, Font.BOLD, new BaseColor(0, 51, 102));
            ColumnText.showTextAligned(page, Element.ALIGN_RIGHT, new Phrase("Total(RMB)", font2), positionX2 - 60, positionY2, 0);
            ColumnText.showTextAligned(page, Element.ALIGN_RIGHT, new Phrase(String.valueOf(total), font), positionX2, positionY2, 0);

            //close stamper and reader
            stamper.close();
            reader.close();

            return  newFileName;

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping("download")
    public void download(@RequestParam String fileName, httpervletResponse response,  httpervletRequest request) throws IOException {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",5);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        // get file base on the name
        java.io.File uploadFile = new java.io.File(receiptsUploadPath + fileName);
        // set output io stream
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        // read the io stream
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }



}
