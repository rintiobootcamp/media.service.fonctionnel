package com.bootcamp;

import com.bootcamp.crud.CensureCRUD;
import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.entities.Censure;
import com.bootcamp.entities.Media;
import com.rintio.elastic.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class ElasticTest {
    private final Logger LOG = LoggerFactory.getLogger(ElasticTest.class);


    @Test
    public void createIndexMedia()throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Media> medias = MediaCRUD.read();
        for (Media media : medias){
            elasticClient.creerIndexObjectNative("medias","media",media,media.getId());
            LOG.info("media "+media.getId()+" created");
        }
    }
}
