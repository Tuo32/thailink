package com.liu.thailink;

import com.liu.thailink.entities.File;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.FileMapper;
import com.liu.thailink.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileService fileService;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @BeforeEach
    public void setUp() {
        fileService = new FileService();
    }

    @Test
    @DisplayName("测试保存文件")
    public void testSaveFile() {
        File file = new File();
        file.setName("test.txt");

        when(fileMapper.insert(Mockito.any(File.class))).thenReturn(1);

        boolean result = fileService.saveFile(file);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Test delete file")
    public void testDeleteFile() {
        File file = new File();
        file.setFileID(1);
        file.setName("test.txt");

        when(fileMapper.selectById(1)).thenReturn(file);
        when(fileMapper.deleteById(1)).thenReturn(1);

        java.io.File file2 = new  java.io.File(fileUploadPath + file.getName());

        doReturn(true).when(file2).delete();

        boolean result = fileService.deleteFile(1);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("test exception during delete file")
    public void testDeleteFileWithException() {
        when(fileMapper.selectById(1)).thenThrow(ServiceException.class);

        Assertions.assertThrows(ServiceException.class, () -> {
            fileService.deleteFile(1);
        });
    }
}
