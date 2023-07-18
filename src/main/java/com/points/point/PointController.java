package com.points.point;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.points.point.Entities.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/*import java.util.logging.Level;
import java.util.logging.Logger;*/


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin
@RequestMapping()
public class PointController {

    private static final Logger log = Logger.getLogger(PointController.class.getName());
    private PointService pointService;

    @Autowired
    public PointController( PointService pointService) {
         this.pointService = pointService;
    }

    @PostMapping(path ="/points")
    public ResponseEntity<?> logpoint(@RequestBody @JsonView(PointViews.ExternalPointView.class) PointDTO pointDTO) throws JsonProcessingException {

            Point point = pointService.addPointToDB(pointDTO);
            if(point==null){
                String payload_issue = "something is not correct in your payload.";
                log.log(Level.ERROR,payload_issue);
                return ResponseEntity.unprocessableEntity()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(payload_issue);
            } else {
                log.log(Level.INFO,point.toString());
                return new ResponseEntity<>(point,HttpStatusCode.valueOf(201));
            }
    }

    @PostMapping(path ="/person")
    public Object addPerson(@RequestBody @JsonView(PersonViews.ExternalPersonView.class) PersonDTO personDto) throws JsonProcessingException {

        InputStream schemaAsStream = PointController.class.getClassLoader().getResourceAsStream("model/person_schema.json");
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);

        ObjectMapper om = new ObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = om.readTree(String.valueOf(objectMapper
                .writerWithView(PersonViews.ExternalPersonView.class)
                .writeValueAsString(personDto)));

        Set<ValidationMessage> errors = schema.validate(jsonNode);
        String errorsCombined = ""; int x=0;
        for(ValidationMessage error: errors){
            errorsCombined += error.getMessage() + "\n";
            x++;
        }
        if(errorsCombined.isEmpty()){
            log.log(Level.INFO,personDto);
            return pointService.addPerson(personDto);
        }
        log.log(Level.ERROR,errorsCombined);
        return errorsCombined;
    }

    @GetMapping(path="/person")
    @JsonView(PersonViews.ExternalPersonView.class)
    public Object searchPersonByAvailableFields(@RequestParam(required = false) Long personId, @RequestParam(required = false) String name,
                            @RequestParam(required = false) String surname, @RequestParam(required = false) Gender gender,
                            @RequestParam(required = false) Integer minAge, @RequestParam(required = false) Integer maxAge,
                            @RequestParam(required = false) String cellphone, @RequestParam(required = false) String whatsapp,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false, defaultValue = "false") Boolean retrievingTestData,
                            @RequestParam(required = false) String askingScenario){

        Person person = pointService.searchPersonByAvailableFields(personId,name,surname,gender,minAge,maxAge,cellphone,whatsapp,email,askingScenario);

        if(retrievingTestData.equals(true) && person!=null){
            pointService.updatePerson(person,askingScenario);
            log.log(Level.INFO, askingScenario + " " +  person);
            return person;
        }

        if(person!=null){
            log.log(Level.INFO, person);
            return person;
        }

        log.log(Level.INFO, "No person matching provided criteria found");
        return "{ \"person \":\" No person matching provided criteria found\"}";
    }

    @PostMapping(path = "/points/link")
    public String pointLink(@RequestBody PointLink pointLink) {
        log.log(Level.INFO,pointLink);
        return pointService.linkPoints(pointLink);
    }

    @GetMapping( path = "/points")
    public List<Point> letsSeeAllPoints(){
        log.log(Level.INFO ,"Just." );
        return pointService.letsSeeAllPoints();
    }

    @GetMapping(path = "/point", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Point showOnePoint(@RequestParam(name = "Point-Call") String pointCall,@RequestParam(name = "personId") String personId){
        log.log(Level.INFO ,personId);
        return pointService.getPoint(pointCall,personId);
    }

    @RequestMapping(
            path = "/media",
            method = POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @JsonView(PointViews.ExternalPointView.class)
    public ResponseEntity<Point> addMediaToPoint(@RequestParam(name="Point-Call") String pointCall,
                                  @RequestParam(name="organization") String organization,
                                  @RequestParam(name="type") String type,
                                  @RequestParam(name="file") MultipartFile multipartFile
                                  ) throws IOException {
        HashMap<String,String> metaDataMap = new HashMap<>();
        metaDataMap.put("pointCall", pointCall);
        metaDataMap.put("organization", organization);
        metaDataMap.put("type", type);
        metaDataMap.put("filename", multipartFile.getOriginalFilename());
        System.out.println("#####pointService.addMediaToDB####"+multipartFile.getSize()+"  "+multipartFile.getOriginalFilename());//#############

        log.log(Level.INFO ,pointCall + " " + metaDataMap + " " + multipartFile );

        return new ResponseEntity<>(pointService.addMediaToDB(pointCall,metaDataMap, multipartFile), HttpStatusCode.valueOf(201));
    }

    @RequestMapping(method = {GET}, path="/media")
    public ResponseEntity<ByteArrayResource> getDBFile(@RequestParam(name="filename") String filename) throws IOException {
        Medias media = pointService.getMedia(filename);
        log.log(Level.INFO,filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFilename() + "\"")
                .body(new ByteArrayResource(media.getFile()));
    }
}
