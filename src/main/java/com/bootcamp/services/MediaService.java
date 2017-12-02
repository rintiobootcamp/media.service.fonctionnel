package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.entities.Media;
import java.io.IOException;

import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class MediaService implements DatabaseConstants {

    @Autowired
    DiskStorageService diskStorageService;

    public void create(Media media) throws SQLException {
        MediaCRUD.create(media);
    }


    public Media saveFile(MultipartFile file, int entityId, EntityType entityType) throws SQLException, IOException {
        Media media = diskStorageService.save(file);
        media.setEntityType(entityType);
        media.setEntityId(entityId);
        create(media);

        return media;
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
        criterias.add(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.add(new Criteria(new Rule("entityType", "=", entityType), null));

        return MediaCRUD.getByCriteria(criterias);
    }

}
