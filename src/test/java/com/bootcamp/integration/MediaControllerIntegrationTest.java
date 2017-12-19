package com.bootcamp.integration;

import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.entities.Media;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jayway.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <h2> The integration test for Media controller</h2>
 * <p>
 * In this test class, the methods :
 * <ul>
 * <li>create a media </li>
 * <li>get one media by it's id</li>
 * <li>get all media</li>
 * <li>And update a media have been implemented</li>
 * </ul>
 * before getting started , make sure , the media fonctionnel service is deploy
 * and running as well. you can also test it will the online ruuning service As
 * this test interact directly with the local database, make sure that the
 * specific database has been created and all it's tables. If you have data in
 * the table,make sure that before creating a data with it's id, do not use an
 * existing id.
 * <b>Note: you have to the location of the media in your project properties</b>
 * </p>
 */
public class MediaControllerIntegrationTest {

    private static Logger logger = LogManager.getLogger(MediaControllerIntegrationTest.class);

    /**
     * The Base URI of media fonctionnal service, it can be change with the
     * online URIof this service.
     */
    private String BASE_URI = "http://localhost:8086/media";

    /**
     * The path of the Media controller, according to this controller
     * implementation
     */
    private String MEDIA_PATH = "/medias";

    /**
     * This ID is initialize for create , getById, and update method, you have
     * to change it if you have a save data on this ID otherwise a error or
     * conflit will be note by your test.
     */
    private int mediaId = 0;
    private String mediaInternalName = "";

    /**
     * This method create a new media with the given id
     *
     * @see Media#id
     * <b>you have to chenge the name of the media if this name already exists
     * in the database
     * @see Media#getEntityType() else, the media will be created but not wiht the
     * given ID. and this will accure an error in the getById and update
     * method</b>
     * Note that this method will be the first to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 0, groups = {"MediaTest"})
    public void createMedia() throws Exception {
        String createURI = BASE_URI + MEDIA_PATH + "/PROJET/7";
        File dataFile = getFile("data-json" + File.separator + "poing.png").getAbsoluteFile();


        Gson gson = new Gson();
        Response response = given()
                .log().all()
                .multiPart( dataFile )
                .expect()
                .when()
                .post(createURI);
        mediaId = gson.fromJson( response.getBody().print(),Media.class ).getId();

       mediaInternalName = gson.fromJson(response.getBody().print(), Media.class).getInternalName();

        logger.debug(mediaId);
        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * This method get a media with the given id
     *
     * @see Media#id
     * <b>
     * If the given ID doesn't exist it will log an error
     * </b>
     * Note that this method will be the second to execute If every done , it
     * will return a 200 httpStatus code
     * @throws Exception
     */
    @Test(priority = 1, groups = {"MediaTest"})
    public void getMediaById() throws Exception {

        String getMediaById = BASE_URI + MEDIA_PATH + "/" + mediaId;

        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getMediaById);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get the statistics of the given entity type
     * <b>
     * the comments must exist in the database
     * </b>
     * Note that this method will be the third to execute If every done , it
     * will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 2, groups = {"MediaTest"})
    public void internalNameMedia() throws Exception {
        String statsURI = BASE_URI + MEDIA_PATH + "/file/" + mediaInternalName;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(statsURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the medias in the database If every done , it will return a 200
     * httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 3, groups = {"MediaTest"})
    public void getAllMedias() throws Exception {
        String getAllMediaURI = BASE_URI + MEDIA_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getAllMediaURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Get All the medias related to a specify entity in the database If every
     * done , it will return a 200 httpStatus code
     *
     * @throws Exception
     */
    @Test(priority = 4, groups = {"MediaTest"})
    public void getMediasByEntity() throws Exception {
        String getMediasByEntityURI = BASE_URI + MEDIA_PATH + "/PROJET/7";
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getMediasByEntityURI);

        logger.debug(response.getBody().prettyPrint());

        Assert.assertEquals(response.statusCode(), 200);

    }

    /**
     * Convert a relative path file into a File Object type
     *
     * @param relativePath
     * @return File
     * @throws Exception
     */
    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    /**
     * Get on media by a given ID from the List of medias
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Media getMediaById(int id) throws Exception {
        List<Media> medias = loadDataMediaFromJsonFile();
        Media media = medias.stream().filter(item -> item.getId() == id).findFirst().get();

        return media;
    }

    /**
     * Convert a medias json data to a media objet list this json file is in
     * resources
     *
     * @return a list of media in this json file
     * @throws Exception
     */
    public List<Media> loadDataMediaFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "medias.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Media>>() {
        }.getType();
        List<Media> medias = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return medias;
    }

}
