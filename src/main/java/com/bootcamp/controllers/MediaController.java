package com.bootcamp.controllers;

import com.bootcamp.entities.Media;
import com.bootcamp.services.MediaService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController("MediaController")
@RequestMapping("/medias")
@Api(value = "Media API", description = "Media API")
public class MediaController {

    @Autowired
    MediaService mediaService;

    @RequestMapping(method = RequestMethod.POST, value = "/{entityType}/{entityId}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new media file", notes = "Save a new media file")
    public ResponseEntity<Media> create(@RequestParam("file") MultipartFile file, @PathVariable(name = "entityId") int entityId, @PathVariable(name = "entityType") String entityType) throws SQLException, IOException {

        Media id = mediaService.saveFile(file, entityId, entityType);

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

    @RequestMapping(method = RequestMethod.GET)
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
    @ApiOperation(value = "Read a medias", notes = "Read a medias")
    public ResponseEntity<List<Media>> readByEntity(@PathVariable("entityId") int entityId, @PathVariable("entityType") String entityType) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/file/{internalName}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get a media location", notes = "Get a media location")
    public ResponseEntity<ByteArrayResource> getMedia(@PathVariable("internalName") String internalName) throws FileNotFoundException, IOException {

        File file = null;
        HttpStatus httpStatus = null;

        file = mediaService.getFile(internalName);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }
}
