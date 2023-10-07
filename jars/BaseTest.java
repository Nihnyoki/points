package steps.definitions;

import com.points.point.Entities.*;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest {
    private Person person;
    protected Point point;
    protected String service;
    protected HashMap<String,Object> reqResDialog;
    protected List<Criteria> criteria;
    protected HashMap<String,String> kv;
    protected Person person_x = new Person();
    protected HashMap<String,Object> formParamHashMap = new HashMap<>();
    protected String[] filename;

    protected String getPayload(Map<String, String> asMap, Boolean isContextCall) throws JsonProcessingException {

        asMap.forEach((o, o2) -> {
            if (o.equals("_service") && o2.equals("person")) {
                setEndPoint(o2);
            } else if (o.equals("person_personId")){
                person.setPersonId(Long.parseLong(o2));
            } else if (o.equals("person_name")){
                person.setName(o2);
            } else if (o.equals("person_surname")){
                person.setSurname(o2);
            } else if (o.equals("person_gender")){
                switch (o2){
                    case "male":
                        person.setGender(Gender.MALE);
                        break;
                    case "female":
                        person.setGender(Gender.FEMALE);
                        break;
                    case "other":
                        person.setGender(Gender.OTHER);
                        break;
                    default:
                        person.setGender(Gender.UNSPECIFIED);
                        break;
                }
            } else if (o.equals("person_age")){
                person.setAge(Integer.valueOf(o2));
            } else if (o.equals("person_cellphone")){
                person.setCellphone(o2);
            } else if (o.equals("person_whatsapp")){
                person.setWhatsapp(o2);
            } else if (o.equals("person_email")){
                person.setEmail(o2);
            } else if (o.equals("_service") && o2.equals("points")) {
                setEndPoint(o2);
            } else if (o.equals("point_pointCall")) {
                point.setPointCall(o2);
            } else if (o.equals("point_personId")) {
                if(isContextCall.equals(true)){
                    point.setPersonId(person_x.getPersonId());
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
            reqResDialog.put("person_req_obj",person);
            return objectMapper
                    .writerWithView(PersonViews.ExternalPersonView.class)
                    .writeValueAsString(person);
        } else if (service.equals("points")) {
            reqResDialog.put("point_req_obj",point);
            return objectMapper
                    .writerWithView(PointViews.ExternalPointView.class)
                    .writeValueAsString(point);
        }
        return null;
    }

    protected HashMap<String, Object> getFormParams(DataTable dt) {

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

    protected void setEndPoint(String service){
        RestAssured.baseURI = "http://localhost:8080";
        if (service.equals("person")) {
            RestAssured.basePath = "/person";
            this.service = service;
            person = new Person();
        } else if (service.equals("points")) {
            RestAssured.basePath = "/points";
            this.service = service;
            point = new Point();
         } else if (service.equals("media")) {
            RestAssured.basePath = "/media";
            this.service = service;
        }
    }
}