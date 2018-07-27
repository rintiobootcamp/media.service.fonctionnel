package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Media;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class MediaService implements DatabaseConstants {

    @Value("${media.location}")
    String mediaDirectory;

    @Autowired
    DiskStorageService diskStorageService;
    ElasticClient elasticClient ;

    @PostConstruct
    public void init(){
        elasticClient = new ElasticClient();
    }

    /**
     * Insert the media entity in the database
     *
     * @param media
     * @throws SQLException
     */
    public void create(Media media) throws Exception {
        media.setDateCreation(System.currentTimeMillis());
        media.setDateMiseAJour(System.currentTimeMillis());
        MediaCRUD.create(media);
        createAllIndexMedia();
    }

    /**
     * Save a media file attached to a specify entity and create the media
     * entity mapping the file
     *
     * @param file
     * @param entityId
     * @param entityType
     * @return the media entity
     * @throws SQLException
     * @throws IOException
     */
    public Media saveFile(MultipartFile file, int entityId, String entityType) throws SQLException, Exception {
        Media media = diskStorageService.save(file);
        media.setEntityType(entityType);
        media.setEntityId(entityId);
        create(media);

        return media;
    }

    /**
     * Update a media entity in the database
     *
     * @param media
     * @throws SQLException
     */
    public void update(Media media) throws Exception {
        media.setDateMiseAJour(System.currentTimeMillis());
        MediaCRUD.update(media);
        createAllIndexMedia();
    }

    /**
     * Delete a media entity in the database
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Media delete(int id) throws Exception {
        Media media = read(id);
        MediaCRUD.delete(media);
        createAllIndexMedia();

        return media;
    }

    /**
     * Get a media entity in the database knowing its id
     *
     * @param id
     * @return the media entity
     * @throws SQLException
     */
    public Media read(int id) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", id));
//        List<Media> medias = MediaCRUD.read(criterias);

//        return medias.get(0);
        return getAllMediaIndex().stream().filter(t->t.getId()==id).findFirst().get();
    }

    /**
     * Get all the medias entity in the database
     *
     * @return the medias list
     * @throws SQLException
     */
    public List<Media> getAll() throws Exception {
        return getAllMediaIndex();
    }

    public List<Media> getAllMediaIndex() throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("medias");
        ModelMapper modelMapper = new ModelMapper();
        List<Media> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Media.class));
        }
        return rest;
    }

    /**
     * Get in the database all the medias entity of a specify entity
     *
     * @param entityId
     * @param entityType
     * @return the medias list
     * @throws SQLException
     */
    public List<Media> getByEntity(int entityId, String entityType) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        return MediaCRUD.read(criterias);
        return getAllMediaIndex().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType)).collect(Collectors.toList())
                .stream().filter(o->o.getEntityId()==entityId).collect(Collectors.toList());
    }

    /**
     * Get the media file of mapping a media entity
     *
     * @param internalName
     * @return the media file
     * @throws FileNotFoundException
     */
    public File getFile(String internalName) throws FileNotFoundException {
        File file = new File(mediaDirectory + internalName);
        return file;
    }

    public boolean createAllIndexMedia()throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
        List<Media> medias = MediaCRUD.read();
        for (Media media : medias){
            elasticClient.creerIndexObjectNative("medias","media",media,media.getId());
//            LOG.info("pilier "+pilier.getNom()+" created");
        }
        return true;
    }

}
