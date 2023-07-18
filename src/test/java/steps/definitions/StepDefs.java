package steps.definitions;

import com.points.point.Entities.Person;
import com.points.point.Entities.Point;
import com.points.point.PointController;
import hooks.ContentKey;
import hooks.Ops;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
public class StepDefs {
    private static final Logger log = Logger.getLogger(StepDefs.class.getName());
    Ops ops;
    public StepDefs() {
        this.ops = new Ops();
    }

    @When("calling generate folks")
    public void callingGenerateFolks(DataTable dt) throws JsonProcessingException {
        log.log(Level.INFO,"TstStep callingGenerateFolks, DataTable : " + dt.asMap());
        ops.generatePersons(dt.asMap(),false, false);
    }

    @Given("the user details below:")
    public void theUserDetailsBelow(DataTable dt) throws JsonProcessingException {

        ops.getReqResDialog().put(ContentKey.person_REQUEST_OBJ,(Person) ops.generatePersons(dt.asMap(),true, false));
        ops.getReqResDialog().put(
                ContentKey.person_REQUEST_JSON,
                ops.getJson(ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ))
        );

        log.log(Level.INFO,"TstStep theUserDetailsBelow,  person_REQUEST_OBJ) : " +
                ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ));
        log.log(Level.INFO,"TstStep theUserDetailsBelow,  person_REQUEST_JSON) : " +
                ops.getReqResDialog().get(ContentKey.person_REQUEST_JSON));
    }

    @When("user is added to point service database")
    public void user_is_added_to_point_service_database() {

        ops.getReqResDialog().put(ContentKey.person_RESPONSE_RES,
                given()
                .accept("application/json")
                .contentType("application/json")
                .body(ops.getReqResDialog().get(ContentKey.person_REQUEST_JSON))
                .post()
        );
        ops.getReqResDialog().put(ContentKey.person_RESPONSE_OBJ,
                ((Response) ops.getReqResDialog().get(ContentKey.person_RESPONSE_RES))
                        .getBody()
                        .as(Person.class));
    }

    @Then("check that the user is stored")
    public void check_that_the_user_is_stored() {
        Assert.assertEquals(((Person) ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ)).getPersonId(),
                ((Person) ops.getReqResDialog().get(ContentKey.person_RESPONSE_OBJ)).getPersonId());

        Assert.assertEquals(((Person) ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ)).getAge(),
                ((Person) ops.getReqResDialog().get(ContentKey.person_RESPONSE_OBJ)).getAge());

        Assert.assertEquals(((Person) ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ)).getName(),
                ((Person) ops.getReqResDialog().get(ContentKey.person_RESPONSE_OBJ)).getName());

        Assert.assertEquals( ((Person) ops.getReqResDialog().get(ContentKey.person_REQUEST_OBJ)).getAge(),
                ((Person) ops.getReqResDialog().get(ContentKey.person_RESPONSE_OBJ)).getAge() );

        System.out.println("Just.\n");
    }

    @Given("any person exists in the db:")
    public void anyPersonExistsInTheDb(DataTable dt) throws JsonProcessingException {
        Response res = given()
                .formParams(ops.getFormParams(dt))
                .accept("application/json")
                .get();

        ops.setPerson((Person) res.getBody().as(Person.class));
    }
    @When("this person posts a point with below details:")
    public void thisPersonPostsAPointWithBelowDetails(DataTable dt) throws JsonProcessingException {

        if(!dt.asMap().get("_contextCall").isEmpty()){
            ops.getReqResDialog().put(ContentKey.point_REQUEST_OBJ,(Point) ops.mapObject(dt.asMap(), true));
            ops.getReqResDialog().put(
                    ContentKey.point_REQUEST_JSON,
                    ops.getJson(ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ))
            );
        } else {
            ops.getReqResDialog().put(ContentKey.point_REQUEST_OBJ,(Point) ops.mapObject(dt.asMap(), false));
            ops.getReqResDialog().put(
                    ContentKey.point_REQUEST_JSON,
                    ops.getJson(ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ))
            );
        }

        ops.getReqResDialog().put(ContentKey.point_RESPONSE_RES,
                given()
                .accept("application/json")
                .contentType("application/json")
                .body(ops.getReqResDialog().get(ContentKey.point_REQUEST_JSON))
                .post()
        );

        ops.getReqResDialog().put(ContentKey.point_RESPONSE_OBJ,
                ((Response) ops.getReqResDialog().get(ContentKey.point_RESPONSE_RES))
                        .getBody()
                        .as(Point.class));
    }

    @Then("verify that the point is retrievable via the {string} service end_point")
    public void verifyThatThePointIsRetrievableViaTheServiceEnd_point(String endPoint) {
        ops.getReqResDialog().put(ContentKey.point_GET_RESPONSE_OBJ,
                given()
                        .accept("application/json")
                        .queryParam("Point-Call",((Point) ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ)).getPointCall())
                        .queryParam("personId",((Point) ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ)).getPersonId())
                        .get(endPoint).as(Point.class));

        Assert.assertEquals(
                ((Point) ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ)).getNote(),
                ((Point) ops.getReqResDialog().get(ContentKey.point_GET_RESPONSE_OBJ)).getNote()
        );

        Assert.assertEquals(
                ((Point) ops.getReqResDialog().get(ContentKey.point_REQUEST_OBJ)).getSubject(),
                ((Point) ops.getReqResDialog().get(ContentKey.point_GET_RESPONSE_OBJ)).getSubject()
        );
    }

    @Given("the users image {} is {}")
    public void theUsersImageIs(String filename_key, String filename_val) {

        Response res = given()
                .queryParam(filename_key, filename_val)
                .queryParam("file_id", "dummy_value_not_used")
                .when()
                .get();

        Assert.assertNotNull(res.getBody());
    }

    @Given("a user uploads image of type {string}:")
    public void aUserUploadsImageOfType(String contentType,io.cucumber.datatable.DataTable requestParams) throws IOException {
        requestParams.asMap().forEach((s, s2) -> {
            if(!s.equals("filename")) {
                ops.getFormParamHashMap().put(s, s2);
            }
            if (s.equals("filename")) {
                if(ops.getFilename() == null){
                    ops.setFilename(new String[]{s, s2});
                }
            }
            if (s.equals("_service")){
                ops.setEndPoint(s2);
            }
        });
        System.out.println("filename[filename.length-1] * this.service : "+ops.getFilename()[ops.getFilename().length-1]+" * "+ops.getService());
        Response res = given()
                .formParams(ops.getFormParamHashMap())
                .multiPart("file",
                        String.valueOf(ops.getFilename()[ops.getFilename().length-1]),
                        getMultipartFile(ops.getFilename()[ops.getFilename().length-1],contentType.toString()).getBytes())
                .contentType(ContentType.MULTIPART)
                .when()
                .post();
        ops.getChecksum(ops.getFile());
    }

    public MultipartFile getMultipartFile(String filename, String contentType) {

        ops.setFile(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator +
                "resources" + File.separator + "features" + File.separator + "image" + File.separator + "post"+ File.separator + filename));

        MultipartFile multipartFile = new MockMultipartFile(ops.getFile().getName(),
                ops.getFile().getName(),
                contentType,
                ops.getFile().getAbsolutePath().getBytes()
        );
        return multipartFile;
    }

     @Given("^get all points service is enquired:$")
        public void getAllPointsServiceIsEnquired(DataTable dt) {
            ops.setService(dt.asMap().get("_service"));
            ops.setEndPoint(ops.getService());

            Response res = given()
                    .accept("application/json")
                    .get();
            System.out.println(res.getBody().prettyPrint());
    }

    @Then("the user can get image {string} back via {string}")
    public void theUserCanGetImageBackVia(String filename, String url) throws IOException {
        HashMap hm = new HashMap<>();
        hm.put("filename",filename);

/*        Response res = given()
                .params(hm)
                .accept(ContentType.MULTIPART)
                .get(url);*/

        byte[] image = given()
                .params(hm)
                .accept(ContentType.MULTIPART)
                .when().get(url)
                .getBody()
                .asByteArray();

        File outputFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator +
                "resources" + File.separator + "features" + File.separator + "image" + File.separator + "get"+ File.separator + filename);
//      /ops.setFileOutputStream(new FileOutputStream(outputFile));
        try (
                OutputStream outputStream = new FileOutputStream(outputFile);
                //DataOutputStream output = new DataOutputStream(outputStream);
                //InputStream inputStream = res.asByteArray();
        ) {
            // output image to file
            //OutputStream outputStream = new FileOutputStream(outputImageFile);
            outputStream.write(image);
            outputStream.close();


            //output.writeBytes(res.getBody().asInputStream().read());
            //output.close();
            //outputStream.write(res.asByteArray());
            //outputStream.close();
            //res.asByteArray()
            /*int byteRead = -1;
            while ((byteRead = res.asInputStream().read()) != -1) {
                output.writeByte(byteRead);
            }*/
            //output.close();
        }

        Assert.assertEquals(ops.getChecksum(ops.getFile()),ops.getChecksum(outputFile));
        Assert.assertEquals(ops.getChecksum(ops.getFile()),"fafaihfshf");
    }
}