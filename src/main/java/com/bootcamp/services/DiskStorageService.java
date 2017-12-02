package com.bootcamp.services;

import com.bootcamp.constants.AppConstant;
import com.bootcamp.entities.Media;
import com.bootcamp.interfaces.Storage;
import com.bootcamp.utils.MediaAppUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by darextossa on 11/28/17.
 */
public class DiskStorageService implements Storage {

    @Override
    public Media save(MultipartFile file) throws IOException {
        Media media = new Media();
        media.setOriginalName(file.getOriginalFilename());
        media.setInternalName(this.getInternalFilename(media.getOriginalName()));
        media.setType(file.getContentType());
        media.setLien(file.getOriginalFilename());
        media.setDateCreation(System.currentTimeMillis());
        media.setDateMiseAJour(System.currentTimeMillis());
        
        File savedFile = new File(AppConstant.FILE_DIRECTORY+media.getInternalName());
        file.transferTo(savedFile);
        return media;
    }

    @Override
    public void delete() {

    }

    private String getInternalFilename(String filename){
      String extension = FilenameUtils.getExtension(filename);
      return MediaAppUtils.generateFileName()+extension;
    }
}
