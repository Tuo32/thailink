package com.liu.thailink.controller;


import cn.hutool.core.io.FileUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.entities.File;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.FileMapper;
import com.liu.thailink.service.FileService;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.httpervletRequest;
import javax.servlet.http.httpervletResponse;
import java.io.IOException;
import java.util.List;

//upload file
//@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileService fileService;

    @Autowired
    private RoleMenuService rms;

    @Autowired
    private UserService us;


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",14);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("DELETE file/del");
        logger.info("Request Parameter = " + id);
        logger.info("=============================");

        return Result.success(fileService.deleteFile(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",14);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        StringBuffer sb = new StringBuffer();
        for (Integer id : ids) {
            sb.append(id);
            sb.append(",");
        }

        logger.info("=============================");
        logger.info("DELETE file/del");
        logger.info("Request Parameter = " + sb);
        logger.info("=============================");

        return Result.success(fileMapper.deleteBatchIds(ids));
    }

    @GetMapping
    public List<File> findAll(){
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        return fileMapper.selectList(queryWrapper);
    }

    @PostMapping("upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam Integer serviceID,  httpervletRequest request) throws IOException {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",13);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        String originalFileName = file.getOriginalFilename();
        String type = FileUtil.extName(originalFileName);
        long size = file.getSize();
       //save to hard disk
        java.io.File uploadParentFile = new java.io.File(fileUploadPath);
      //check if the file exsits already
        if(!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }
        java.io.File uploadFile = new java.io.File(fileUploadPath  + originalFileName);
        file.transferTo(uploadFile);

        String url = "http://localhost:9090/file/"+ originalFileName ;

        File saveFile = new File();
        saveFile.setName(originalFileName);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        if(serviceID == null){
            saveFile.setServiceID(null);
        }else{
            saveFile.setServiceID(serviceID);
        }
//        saveFile.setCustomerID(customerID);
        fileService.saveFile(saveFile);

        logger.info("upload file" + file.getName());

        return url;
    }

    @GetMapping("download")
    public void download(@RequestParam String fileUUID, httpervletResponse response,  httpervletRequest request) throws IOException {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",3);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        // 根据文件的唯一标识码获取文件
        java.io.File uploadFile = new java.io.File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + fileUUID);
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @GetMapping("/page")
    public IPage<File> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String name, @RequestParam String serviceID,  httpervletRequest request){

        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",3);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        if(name != ""){
            queryWrapper.like("name", name);
        }

        if(serviceID !=""){
            queryWrapper.like("serviceID", serviceID);
        }
        return fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }


}
