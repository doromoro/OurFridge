package com.example.recipe2022.handler.general;

import com.example.recipe2022.data.entity.Files;
import com.example.recipe2022.data.enumer.FilePurpose;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SingleFilesHandler {

    public Files parseFileInfo(
            FilePurpose filePurpose,
            MultipartFile multipartFiles
    ) throws Exception {

        if (multipartFiles.isEmpty()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = simpleDateFormat.format(new Date());

        String absolutePath = new File("").getAbsolutePath() + "\\";
        String path = "images/" + currentDate;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Files files = null;
        if (!multipartFiles.isEmpty()) {
            String contentType = multipartFiles.getContentType();
            String originalFileExtension;
            if (ObjectUtils.isEmpty(contentType)) {
                return null;
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else if (contentType.contains("image/gif")) {
                    originalFileExtension = ".gif";
                } else {
                    return null;
                }
            }
            String newFileName = System.nanoTime() + originalFileExtension;
            files = Files.builder()
                    .originalFile(multipartFiles.getOriginalFilename())
                    .storedFile(path + "/" + newFileName)
                    .fileSize(multipartFiles.getSize())
                    .filePurpose(filePurpose)
                    .build();
            file = new File(absolutePath + path + "/" + newFileName);
            multipartFiles.transferTo(file);
        }
        return files;
    }
}