package com.bootcamp.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by darextossa on 11/28/17.
 */
public interface Storage {
    public String save(MultipartFile file) throws IOException;
    public void delete();
}
