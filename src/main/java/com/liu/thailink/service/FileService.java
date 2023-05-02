package com.liu.thailink.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.entities.File;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FileService extends ServiceImpl<FileMapper, File> {
    @Value("${files.upload.path}")
    private String fileUploadPath;
    public boolean saveFile(File file)
    {
        try{
            saveOrUpdate(file);
        }catch(Exception e){
        throw new ServiceException("CODE_500", "system error");
    }
     return false;
    }

    public boolean deleteFile(Integer id) {
        try{
            File file = getById(id);
            java.io.File file2 = new  java.io.File(fileUploadPath + file.getName());
            if (file2.delete()) {
                return removeById(id);
            } else {
                System.out.println("无法删除 " + file.getName());
                throw new ServiceException("CODE_500", "cannot delete");
            }
        }catch (Exception e){
            throw new ServiceException("CODE_500", "system error");
        }
    }
}
