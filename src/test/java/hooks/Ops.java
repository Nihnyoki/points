package hooks;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.points.point.Entities.*;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.data.mongodb.core.query.Criteria;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import static io.restassured.RestAssured.given;

public class Ops<T> {
    private Person person;
    protected Point point;
    protected String service;
    protected HashMap<ContentKey,Object> reqResDialog;
    protected List<Criteria> criteria;
    protected HashMap<String,String> kv;
    protected HashMap<String,Object> formParamHashMap = new HashMap<>();
    protected String[] filename;
    private Response res;
    private T t;
    private File file;
    FileOutputStream fout;

    public Ops(){
        if(reqResDialog == null){
            reqResDialog = new HashMap<ContentKey,Object>();
        }
        if(person == null){
            person = new Person();
        }
        if(point == null){
            point = new Point();
        }
    }

    public HashMap<ContentKey, Object> getReqResDialog() {
        return reqResDialog;
    }

    public void setReqResDialog(HashMap<ContentKey, Object> reqResDialog) {
        this.reqResDialog = reqResDialog;
    }

    public Object generatePersons(Map<String, String> stringStringMap, Boolean justReturnObject, boolean isContextCall) throws JsonProcessingException {
        HashMap<String,String> hshMap = new HashMap<>();

        hshMap.put("_service","person");
        hshMap.put("person_name","just.");
        hshMap.put("person_surname","just.");
        hshMap.put("person_gender","female");
        hshMap.put("person_age","48");
        hshMap.put("person_cellphone","+5111111111");
        hshMap.put("person_whatsapp","+7111111111");
        hshMap.put("person_email","justpoint@gmail.com");

        for (int i=0; i<10; i++) {
            hshMap.put("person_personId",generateValue("person_personId"));
            hshMap.put("person_age",generateValue("person_age"));
            hshMap.put("person_gender",generateValue("person_gender"));

            if(!(stringStringMap == null)){{
                if(!stringStringMap.isEmpty()){
                    stringStringMap.forEach((s, s2) -> {
                        hshMap.put(s,s2);
                    });
                    stringStringMap = null;
                }
            }}

            if(justReturnObject){
                return mapObject(hshMap, isContextCall);
            }

            res = given()
                    .accept("application/json")
                    .contentType("application/json")
                    .body(getPayload(hshMap, isContextCall))
                    .post();
        }
        return res.getBody().asString();
    }

    public String generateValue(String field) {

        String results = null;
        if (field.equals("person_personId")) {
            Coordinates coordinates = new Coordinates();
            RandomGenerator generator = RandomGenerator.getDefault();
            generator.nextInt(10000000, 90000000);
            Integer x = generator.nextInt(10000000, 90000000);
            results = String.valueOf(x) + String.valueOf(x);
        }
        if (field.equals("person_age")) {
            RandomGenerator generator = RandomGenerator.getDefault();
            generator.nextInt(0, 200);
            Integer x = generator.nextInt(0, 200);
            results = String.valueOf(x);
        }
        if (field.equals("person_gender")) {
            RandomGenerator generator = RandomGenerator.getDefault();
            generator.nextInt(0, 2);
            Integer x = generator.nextInt(0, 2);
            if(x==0){
                results = "female";
            } else if(x==1){
                results = "male";
            } else if(x==2){
                results = "other";
            } else if(x==3){
                results = "unspecified";
            }
        }
        return results;
    }

    public String getPayload(Map<String, String> asMap, Boolean isContextCall) throws JsonProcessingException {

        asMap.forEach((o, o2) -> {
            if (o.equals("_service") && o2.equals("person")) {
                this.service = o2;
                setEndPoint(o2);
            } else if (o.equals("person_personId")){
                this.getPerson().setPersonId(Long.parseLong(o2));
            } else if (o.equals("person_name")){
                this.getPerson().setName(o2);
            } else if (o.equals("person_surname")){
                this.getPerson().setSurname(o2);
            } else if (o.equals("person_gender")){
                switch (o2){
                    case "male":
                        this.getPerson().setGender(Gender.MALE);
                        break;
                    case "female":
                        this.getPerson().setGender(Gender.FEMALE);
                        break;
                    case "other":
                        this.getPerson().setGender(Gender.OTHER);
                        break;
                    default:
                        this.getPerson().setGender(Gender.UNSPECIFIED);
                        break;
                }
            } else if (o.equals("person_age")){
                getPerson().setAge(Integer.valueOf(o2));
            } else if (o.equals("person_cellphone")){
                getPerson().setCellphone(o2);
            } else if (o.equals("person_whatsapp")){
                getPerson().setWhatsapp(o2);
            } else if (o.equals("person_email")){
                getPerson().setEmail(o2);
            } else if (o.equals("_service") && o2.equals("points")) {
                this.service = o2;
                setEndPoint(o2);
            } else if (o.equals("point_pointCall")) {
                point.setPointCall(o2);
            } else if (o.equals("point_personId")) {
                if(isContextCall.equals(true)){
                    point.setPersonId(getPerson().getPersonId());
                } else {
                    point.setPersonId(Long.parseLong(o2));
                }
            } else if (o.equals("point_subject")) {
                point.setSubject(o2);
            } else if (o.equals("point_category")) {
                point.setCategory(new ArrayList<String>(List.of(o2.toString().split(","))));
            } else if (o.equals("point_mood")) {
                point.setMood(o2);
            } else if (o.equals("point_note")) {
                point.setNote(o2);
            }
        });

        final ObjectMapper objectMapper = new ObjectMapper();

        if (service.equals("person")) {
            reqResDialog.put(ContentKey.person_REQUEST_OBJ,person);
            return objectMapper
                    .writerWithView(PersonViews.ExternalPersonView.class)
                    .writeValueAsString(getPerson());
        } else if (service.equals("points")) {
            reqResDialog.put(ContentKey.point_REQUEST_OBJ,point);
            return objectMapper
                    .writerWithView(PointViews.ExternalPointView.class)
                    .writeValueAsString(point);
        }
        return null;
    }

    public Object mapObject(Map<String, String> asMap, Boolean isContextCall) {

        asMap.forEach((o, o2) -> {
            if (o.equals("_service") && o2.equals("person")) {
                this.service = o2;
                setEndPoint(o2);
            } else if (o.equals("person_personId")){
                this.getPerson().setPersonId(Long.parseLong(o2));
            } else if (o.equals("person_name")){
                this.getPerson().setName(o2);
            } else if (o.equals("person_surname")){
                this.getPerson().setSurname(o2);
            } else if (o.equals("person_gender")){
                switch (o2){
                    case "male":
                        this.getPerson().setGender(Gender.MALE);
                        break;
                    case "female":
                        this.getPerson().setGender(Gender.FEMALE);
                        break;
                    case "other":
                        this.getPerson().setGender(Gender.OTHER);
                        break;
                    default:
                        this.getPerson().setGender(Gender.UNSPECIFIED);
                        break;
                }
            } else if (o.equals("person_age")){
                getPerson().setAge(Integer.valueOf(o2));
            } else if (o.equals("person_cellphone")){
                getPerson().setCellphone(o2);
            } else if (o.equals("person_whatsapp")){
                getPerson().setWhatsapp(o2);
            } else if (o.equals("person_email")){
                getPerson().setEmail(o2);
            } else if (o.equals("_service") && o2.equals("points")) {
                this.service = o2;
                setEndPoint(o2);
            } else if (o.equals("point_pointCall")) {
                point.setPointCall(o2);
            } else if (o.equals("point_personId")) {
                if(isContextCall.equals(true)){
                    point.setPersonId(getPerson().getPersonId());
                } else {
                    point.setPersonId(Long.parseLong(o2));
                }
            } else if (o.equals("point_subject")) {
                point.setSubject(o2);
            } else if (o.equals("point_category")) {
                point.setCategory(new ArrayList<String>(List.of(o2.toString().split(","))));
            } else if (o.equals("point_mood")) {
                point.setMood(o2);
            } else if (o.equals("point_note")) {
                point.setNote(o2);
            }
        });

        if( this.getService().equals("person")){
            return person;
        }
        if( this.getService().equals("points")){
            return point;
        }
            return null;
    }

    public String getJson(T t) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        if (t instanceof Person){
            return objectMapper
                    .writerWithView(PersonViews.ExternalPersonView.class)
                    .writeValueAsString(t);
        }

        if (t instanceof Point){
            return objectMapper
                    .writerWithView(PointViews.ExternalPointView.class)
                    .writeValueAsString(t);
        }
        return null;
    }

    public HashMap<String, Object> getFormParams(DataTable dt) {

        formParamHashMap.put("retrievingTestData",true);
        dt.asMap().forEach((p, p2) -> {

            if(p.equals("asking_scenario")){
                formParamHashMap.put("askingScenario",p2);
            }
            if(p.equals("person_minAge")){
                formParamHashMap.put("minAge",p2);
            }
            if(p.equals("person_maxAge")){
                formParamHashMap.put("maxAge",p2);
            }
            if(p.equals("person_gender")){
                if(p2.equals("male")){
                    formParamHashMap.put("gender", Gender.MALE);
                } else if (p2.equals("female")) {
                    formParamHashMap.put("gender",Gender.FEMALE);
                } else if (p2.equals("other")) {
                    formParamHashMap.put("gender",Gender.OTHER);
                } else {
                    formParamHashMap.put("gender",Gender.UNSPECIFIED);
                }
            }
        });
        return formParamHashMap;
    }

    public String getChecksum(File file) throws IOException {
        ByteSource byteSource = com.google.common.io.Files.asByteSource(file);
        HashCode hc = byteSource.hash(Hashing.sha256());
        String checksum = hc.toString();
        System.out.println("##### getChecksum : \n" + checksum);
        return checksum;
    }

    public void setEndPoint(String service){
        RestAssured.baseURI = "http://localhost:8080";
        if (service.equals("person")) {
            RestAssured.basePath = "/person";
        } else if (service.equals("points")) {
            RestAssured.basePath = "/points";
        } else if (service.equals("media")) {
            RestAssured.basePath = "/media";
            this.service = service;
        }   
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public HashMap<String, Object> getFormParamHashMap() {
        return formParamHashMap;
    }

    public void setFormParamHashMap(HashMap<String, Object> formParamHashMap) {
        this.formParamHashMap = formParamHashMap;
    }

    public String[] getFilename() {
        return filename;
    }

    public void setFilename(String[] filename) {
        this.filename = filename;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileOutputStream getFileOutputStream() {
        return fout;
    }

    public void setFileOutputStream(FileOutputStream fout) {
        this.fout = fout;
    }
}