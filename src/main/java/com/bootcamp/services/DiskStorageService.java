package com.bootcamp.services;

import com.bootcamp.entities.Media;
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
    String mediaDirectory;

    @Override
    public Media save(MultipartFile file) throws IOException {
        Media media = new Media();
        media.setOriginalName(file.getOriginalFilename());
        media.setInternalName(this.getInternalFilename(file.getOriginalFilename()));
        media.setType(file.getContentType());
        media.setLien(mediaDirectory+media.getInternalName());
        media.setDateCreation(System.currentTimeMillis());
        media.setDateMiseAJour(System.currentTimeMillis());
        
        File savedFile = new File(media.getLien());
        file.transferTo(savedFile);
        return media;
    }

    @Override
    public void delete() {

    }

    private String getInternalFilename(String filename){
      String extension = FilenameUtils.getExtension(filename);
      return MediaAppUtils.generateFileName()+"."+extension;
    }
}
