package com.bootcamp.services;

import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.utils.RequestParser;
import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.entities.Media;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 11/27/17.
 */

@Component
public class MediaService implements DatabaseConstants{

    MediaCRUD mediaCRUD;

    @PostConstruct
    public void init(){
        mediaCRUD = new MediaCRUD();
    }

    public void create(Media media) throws SQLException {
         mediaCRUD.create(media);
    }

    public void update(Media media) throws SQLException {
        mediaCRUD.update(media);
    }

    public Media delete(int id) throws SQLException {
        Media media = read(id);
        mediaCRUD.delete(media);

        return media;
    }

    public Media read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Media> medias = mediaCRUD.read(criterias);

        return medias.get(0);
    }


    public List<Media> read(HttpServletRequest request) throws SQLException, IllegalAccessException, DatabaseException, InvocationTargetException {
        Criterias criterias = RequestParser.getCriterias(request);
        List<String> fields = RequestParser.getFields(request);
        List<Media> medias = null;
        if(criterias == null && fields == null)
           medias =  mediaCRUD.read();
        else if(criterias!= null && fields==null)
            medias = mediaCRUD.read(criterias);
        else if(criterias== null && fields!=null)
            medias = mediaCRUD.read(fields);
        else
            medias = mediaCRUD.read(criterias, fields);

        return medias;
    }

}
