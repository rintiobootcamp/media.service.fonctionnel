package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.constants.AppConstant;
import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.entities.Media;
import com.bootcamp.utils.MediaAppUtils;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class MediaService implements DatabaseConstants {

    public void create(Media media) throws SQLException {
        MediaCRUD.create(media);
    }
    
//    public String saveFile(MultipartFile file) throws SQLException {
//        return file.getOriginalFilename();
//    }

    public int saveFile(MultipartFile file) throws SQLException {
        //DiskStorageService diskStorageService = new DiskStorageService();
        Media media = new Media();
        try {
            //media = diskStorageService.save(file);
            media = this.save(file);
            this.create(media);
        } catch (IOException ex) {
            Logger.getLogger(MediaService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return media.getId();
    }

    public void update(Media media) throws SQLException {
        MediaCRUD.update(media);
    }

    public Media delete(int id) throws SQLException {
        Media media = read(id);
        MediaCRUD.delete(media);

        return media;
    }

    public Media read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Media> medias = MediaCRUD.read(criterias);

        return medias.get(0);
    }

    public List<Media> getAll() throws SQLException {
        return MediaCRUD.read();
    }

    public List<Media> getByEntity(int entityId, EntityType entityType) throws SQLException {
        List<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(new Criteria(new Rule("entityId", "=", 3), "AND"));
        criterias.add(new Criteria(new Rule("entityType", "=", EntityType.PROJET), null));
        return MediaCRUD.getByCriteria(criterias);
    }
    
    public Media save(MultipartFile file) throws IOException {
        Media media = new Media();
        media.setOriginalName(file.getOriginalFilename());
        media.setInternalName(this.getInternalFilename(media.getOriginalName()));
        media.setType(file.getContentType());
        media.setLien(file.getOriginalFilename());
        media.setDateCreation(System.currentTimeMillis());
        media.setDateMiseAJour(System.currentTimeMillis());
        
        String extension = file.getOriginalFilename().split(".")[0];
        
        File savedFile = new File(AppConstant.FILE_DIRECTORY+media.getInternalName());
        file.transferTo(savedFile);
        return media;
    }

    private String getInternalFilename(String filename){
      String extension = FilenameUtils.getExtension(filename);
      return MediaAppUtils.generateFileName()+extension;
    }

}
