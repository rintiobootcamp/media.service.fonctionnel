package com.bootcamp.utils;

/**
 * Created by darextossa on 11/28/17.
 */
public class MediaAppUtils {

    /**
     * Generate a unique name for the media
     *
     * @return the generate name
     */
    public static String generateFileName() {
        long time = System.currentTimeMillis();
        return time + "";
    }
}
