package com.bootcamp.services;

import com.bootcamp.interfaces.Storage;
import com.bootcamp.utils.MediaAppUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by darextossa on 11/28/17.
 */
@Component
public class DiskStorageService implements Storage {

    @Value("${media.location}")
    private String fileDirectory;

    @Override
    public String save(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String internaleFilename = getInternalFilename(originalFilename);

        File savedFile = new File(internaleFilename);
        file.transferTo(savedFile);
        return internaleFilename;
    }

    @Override
    public void delete() {

    }

    private String getInternalFilename(String filename){
      String extension = FilenameUtils.getExtension(filename);
      return MediaAppUtils.generateFileName()+extension;
    }
}
