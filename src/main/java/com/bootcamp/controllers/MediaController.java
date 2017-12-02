package com.bootcamp.controllers;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.entities.Media;
import com.bootcamp.services.MediaService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.multipart.MultipartFile;


@RestController("MediaController")
@RequestMapping("/media")
@Api(value = "Media API", description = "Media API")
public class MediaController {

    @Autowired
    MediaService mediaService;


    @RequestMapping(method = RequestMethod.POST, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new media file", notes = "Save a new media file")
    public ResponseEntity<Media> create(@RequestParam("file") MultipartFile file, @PathVariable(name = "entityId") int entityId,  @PathVariable(name = "entityType") String entityType) throws SQLException, IOException {
        EntityType type = EntityType.valueOf(entityType);
        Media id = mediaService.saveFile(file, entityId, type);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a media", notes = "Read a media")
    public ResponseEntity<Media> read(@PathVariable(name = "id") int id) {

        Media media = new Media();
        HttpStatus httpStatus = null;

        try {
            media = mediaService.read(id);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Media>(media, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read all media", notes = "Read all media")
    public ResponseEntity<List<Media>> read() {

        List<Media> medias = new ArrayList<Media>();
        HttpStatus httpStatus = null;

        try {
            medias = mediaService.getAll();
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<Media>>(medias, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a comments", notes = "Read a comments")
    public ResponseEntity<List<Media>> readByEntity(@PathVariable("entityId") int entityId, @PathVariable("entityType") String entityTypeName) {
        EntityType entityType = EntityType.valueOf(entityTypeName);
        List<Media> medias = new ArrayList<Media>();
        HttpStatus httpStatus = null;

        try {
            medias = mediaService.getByEntity(entityId, entityType);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<List<Media>>(medias, httpStatus);
    }
}