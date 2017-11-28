package com.bootcamp.controllers;

import com.bootcamp.commons.exceptions.DatabaseException;
import com.bootcamp.commons.ws.models.MediaWs;
import com.bootcamp.commons.ws.models.MediaWss;
import com.bootcamp.commons.ws.models.Error;
import com.bootcamp.entities.Media;
import com.bootcamp.services.MediaService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;


@RestController("MediaController")
@RequestMapping("/media")
@Api(value = "Media API", description = "Media API")
public class MediaController {

    @Autowired
    MediaService mediaService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Create a new media", notes = "Create a new media")
    public ResponseEntity<MediaWs> create(@RequestBody @Valid Media media) {

        MediaWs mediaWs = new MediaWs();
        HttpStatus httpStatus = null;

        try {
            mediaService.create(media);
            mediaWs.setData(media);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            String errorMessage = exception.getMessage()==null?exception.getMessage():exception.getCause().getMessage();
            Error error = new Error();
            error.setMessage(errorMessage);
            mediaWs.setError(error);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<MediaWs>(mediaWs, httpStatus);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Update a new media", notes = "Update a new media")
    public ResponseEntity<MediaWs> update(@RequestBody @Valid Media media) {

        MediaWs mediaWs = new MediaWs();
        HttpStatus httpStatus = null;

        try {
            mediaService.update(media);
            mediaWs.setData(media);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            String errorMessage = exception.getMessage()==null?exception.getMessage():exception.getCause().getMessage();
            Error error = new Error();
            error.setMessage(errorMessage);
            mediaWs.setError(error);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<MediaWs>(mediaWs, httpStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a media", notes = "Delete a media")
    public ResponseEntity<MediaWs> delete(@PathVariable(name = "id") int id) {

        MediaWs mediaWs = new MediaWs();
        HttpStatus httpStatus = null;

        try {
            Media media = mediaService.delete(id);
            mediaWs.setData(media);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            String errorMessage = exception.getMessage()==null?exception.getMessage():exception.getCause().getMessage();
            Error error = new Error();
            error.setMessage(errorMessage);
            mediaWs.setError(error);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<MediaWs>(mediaWs, httpStatus);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a media", notes = "Read a media")
    public ResponseEntity<MediaWs> read(@PathVariable(name = "id") int id) {

        MediaWs mediaWs = new MediaWs();
        HttpStatus httpStatus = null;

        try {
            Media media = mediaService.read(id);
            mediaWs.setData(media);
            httpStatus = HttpStatus.OK;
        }catch (SQLException exception){
            String errorMessage = exception.getMessage()==null?exception.getMessage():exception.getCause().getMessage();
            Error error = new Error();
            error.setMessage(errorMessage);
            mediaWs.setError(error);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<MediaWs>(mediaWs, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Read a media", notes = "Read a media")
    public ResponseEntity<MediaWss> read() {

        MediaWss mediaWss = new MediaWss();
        HttpStatus httpStatus = null;

        try {
            List<Media> medias = mediaService.read(request);
            mediaWss.setData(medias);
            httpStatus = HttpStatus.OK;
        }catch (SQLException | IllegalAccessException | DatabaseException | InvocationTargetException exception){
            String errorMessage = exception.getMessage()==null?exception.getMessage():exception.getCause().getMessage();
            Error error = new Error();
            error.setMessage(errorMessage);
            mediaWss.setError(error);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<MediaWss>(mediaWss, httpStatus);
    }
}
