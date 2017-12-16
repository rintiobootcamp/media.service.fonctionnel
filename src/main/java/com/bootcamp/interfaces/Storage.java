package com.bootcamp.interfaces;

import com.bootcamp.entities.Media;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by darextossa on 11/28/17.
 */
public interface Storage {

    /**
     * Save a media file
     *
     * @param file
     * @return the created media entity
     * @throws IOException
     */
    public Media save(MultipartFile file) throws IOException;

    /**
     *
     */
    public void delete();
}
