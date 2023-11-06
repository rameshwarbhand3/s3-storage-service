package com.ram.storage.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UploadFileApiTest {

    public static final String BUCKET_NAME = "test-bucket";
    @Autowired
    private AmazonS3 s3Client;

    @LocalServerPort
    private int port;
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.s3.bucket-name", () -> BUCKET_NAME);
        registry.add("aws.s3.region", () -> localStack.getRegion());
        registry.add("aws.s3.endpoint", () -> localStack.getEndpointOverride(S3).toString());
    }

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        try {
            localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUploadFileApiSuccess() throws IOException {
        given().port(port)
                .formParam("fileName", "test.txt")
                .multiPart(new File("src/test/resources/test.txt"))
                .when()
                .post("/storage/upload")
                .then()
                .statusCode(HttpStatus.SC_OK);
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, "test.txt");
        assertEquals("test.txt", s3Object.getKey());
        assertEquals("Hello THIS IS TESTING USING REST-ASSURED.", IOUtils.toString(s3Object.getObjectContent()));
    }

    @Test
    public void testDownloadFileApiSuccess() {

        s3Client.putObject(BUCKET_NAME,"test.txt","test.txt");
        String downLoadedFile = given()
                .port(port)
                .formParam("objectKey", "test.txt")
                .when().get("/storage/download")
                .then().statusCode(HttpStatus.SC_OK).extract().asString();


         assertEquals("test.txt", downLoadedFile);


    }

}
