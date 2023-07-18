package com.points.point;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.points.point.Entities.*;
import com.points.point.Repository.PersonRepository;
import com.points.point.Repository.PointRepository;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.random.RandomGenerator;
import java.time.LocalDateTime;

@Service
public class PointService {

    private PersonRepository personRepository;
    private PointRepository pointRepository;
    private MongoTemplate mongoTemplate;
    private GridFsOperations gridFsOperations;
    private Point point;
    private Person person;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PointService.class.getName());

    @Autowired
    public PointService(PersonRepository personRepository,
                        PointRepository pointRepository,
                        MongoTemplate mongoTemplate,
                        GridFsOperations gridFsOperations
    ) {
        this.personRepository = personRepository;
        this.pointRepository = pointRepository;
        this.mongoTemplate = mongoTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    public Person addPerson(PersonDTO personDto) {
        person = new Person();
        person.setPersonId(personDto.getPersonId());
        person.setName(personDto.getName());
        person.setSurname(personDto.getSurname());
        person.setGender(personDto.getGender());
        person.setAge(personDto.getAge());
        person.setCellphone(personDto.getCellphone());
        person.setWhatsapp(personDto.getWhatsapp());
        person.setEmail(personDto.getEmail());
        logger.log(Level.INFO, person.toString());
        return mongoTemplate.save(person);
    }

    public Point addPointToDB(PointDTO pointDTO) {

        if(pointDTO.getPersonId()!=null){
            Query query = new Query();
            query.addCriteria(new Criteria().andOperator(Criteria.where("personId").is(pointDTO.getPersonId())));
            person=mongoTemplate.findOne(query,Person.class);
            if(person == null) {
                return null;
            }
            if(point == null) {
                point = new Point();
            }
                this.point.setPointCall(pointDTO.getPointCall());
                this.point.setPersonId(pointDTO.getPersonId());
                this.point.setSubject(pointDTO.getSubject());
                this.point.setMood(pointDTO.getMood());
                this.point.setNote(pointDTO.getNote());
                this.point.setCategory(pointDTO.getCategory());
                this.point.setPointDateTime(LocalDateTime.now());
                this.point.setCurrentDateTime(LocalDateTime.now());
                this.point.setCoordinates(randomCoordinatesGenerator());

                logger.log(Level.INFO, point.toString());

                Point p = mongoTemplate.save(this.point,"point");
                person.getPoints().add(p);
                updatePerson(person,"");

                return p;
        }
        return null;
    }

    public Person searchPersonByAvailableFields(Long personId, String name,String surname, Gender gender, Integer minAge, Integer maxAge,
                                                String cellphone,String whatsapp, String email, String last_used_test_scenario) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<Criteria>();

        if(personId!=null) {
            criteriaList.add(Criteria.where("personId").is(personId));
        }
        if(name!=null) {
            criteriaList.add(Criteria.where("name").is(name));
        }
        if(surname!=null) {
            criteriaList.add(Criteria.where("surname").is(surname));
        }
        if(gender!=null) {
            if(!gender.equals(Gender.UNSPECIFIED)){
                criteriaList.add(Criteria.where("gender").is(gender));
            }
        }
        if(minAge!=null) {
            criteriaList.add(Criteria.where("age").gte(minAge));
        }
        if(minAge!=null) {
            criteriaList.add(Criteria.where("age").lte(maxAge));
        }
        if(cellphone!=null) {
            criteriaList.add(Criteria.where("cellphone").is(cellphone));
        }
        if(whatsapp!=null) {
            criteriaList.add(Criteria.where("whatsapp").is(whatsapp));
        }
        if(email!=null) {
            criteriaList.add(Criteria.where("email").is(email));
        }
        if(last_used_test_scenario!=null) {
            criteriaList.add(Criteria.where("lastUsedTestScenario").ne(last_used_test_scenario));
        }

        if(!criteriaList.isEmpty()){
            query.addCriteria(new Criteria().andOperator(criteriaList));
            return mongoTemplate.findOne(query,Person.class);
            }
        return null;
    }

    public Point addMediaToDB(String pointCall,
                               HashMap<String,String> toBeMetaData,
                               MultipartFile multipartFile) throws IOException {
        DBObject metaData = new BasicDBObject();
        toBeMetaData.forEach((o, o2) -> metaData.put(o.toString(),o2.toString()));

        String mediaId = String.valueOf(gridFsOperations.store(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                metaData)
        );

        Medias media = new Medias();
            media.setMediaId(mediaId);
            media.setMediaPointCall(pointCall);
            media.setFileSize(multipartFile.getSize());
            media.setFileType(toBeMetaData.get("type"));
            media.setFilename(multipartFile.getOriginalFilename());

        return attachMediaToPoint(media);
    }

    public Point attachMediaToPoint(Medias media){
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(Criteria.where("pointCall").is(media.getMediaPointCall())));
        Point point = (Point) mongoTemplate.findOne(query,Point.class);
        System.out.println("################"+point.toString());//#############
        ArrayList<Medias> mediaList = new ArrayList<>();
        mediaList.add(media);
        point.setMedias(mediaList);

        Document doc = new Document();
        mongoTemplate.getConverter().write(point, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.upsert(query, update, Point.class, "point");

        return point;
    }

    public Coordinates randomCoordinatesGenerator() {
        Coordinates coordinates = new Coordinates();
        RandomGenerator randomFloatGenerator = RandomGenerator.getDefault();
        RandomGenerator randomIntGenerator = RandomGenerator.getDefault();
        randomFloatGenerator.nextFloat();
        randomIntGenerator.nextInt(1,1000);
        float ix = randomIntGenerator.nextInt(1,1000) + randomFloatGenerator.nextFloat();
        float iy = randomIntGenerator.nextInt(1,1000) + randomFloatGenerator.nextFloat();
        float iz = randomIntGenerator.nextInt(1,1000) + randomFloatGenerator.nextFloat();

        coordinates.setXlr(ix);
        coordinates.setYud(iy);
        coordinates.setZfb(iz);
        return coordinates;
    }

    public String linkPoints(PointLink linkInstructionObj) {
        String returnStr;

        if(!linkInstructionObj.getPointCallSrc().isEmpty() && !linkInstructionObj.getPointCallDest().isEmpty()){

            Query srcQuery = new Query();
            srcQuery.addCriteria(new Criteria().andOperator(Criteria.where("pointCall").regex(linkInstructionObj.getPointCallSrc(),"i")));
            Point srcPoint = (Point) mongoTemplate.find(srcQuery,Point.class).get(0);

            Query destQuery = new Query();
            destQuery.addCriteria(new Criteria().andOperator(Criteria.where("pointCall").regex(linkInstructionObj.getPointCallDest(),"i")));
            Point destPoint = (Point) mongoTemplate.find(destQuery,Point.class).get(0);

            if(linkInstructionObj.getBiDirectional()){
                try {
                    if(destPoint.getPointHashMaps().containsKey(srcPoint.getPointCall())){
                        destPoint.getPointHashMaps().remove(srcPoint.getPointCall());
                    }
                    srcPoint.getPointHashMaps().put(destPoint.getPointCall(),destPoint);
                } catch (NullPointerException npe) {
                    HashMap<String,Point> srcPointHashMap = new HashMap<>();
                    srcPointHashMap.put(destPoint.getPointCall(),destPoint);
                    srcPoint.setPointHashMaps(srcPointHashMap);
                }

                /*mongoTemplate.remove(srcQuery,Point.class);
                mongoTemplate.insert(srcPoint);*/

                Document doc = new Document();
                mongoTemplate.getConverter().write(srcPoint, doc);
                Update update = Update.fromDocument(doc);
                mongoTemplate.upsert(srcQuery, update, Point.class, "point");

                try {
                    if(srcPoint.getPointHashMaps().containsKey(destPoint.getPointCall())) {
                        srcPoint.getPointHashMaps().remove(destPoint.getPointCall());
                    }
                    destPoint.getPointHashMaps().put(srcPoint.getPointCall(),srcPoint);
                } catch (NullPointerException npe){
                    HashMap<String,Point> destPointHashMap = new HashMap<>();
                    destPointHashMap.put(srcPoint.getPointCall(),srcPoint);
                    destPoint.setPointHashMaps(destPointHashMap);
                }
                /*mongoTemplate.remove(destQuery,Point.class);
                mongoTemplate.insert(destPoint);*/

                Document doc1 = new Document();
                mongoTemplate.getConverter().write(destPoint, doc1);
                Update update1 = Update.fromDocument(doc1);
                mongoTemplate.upsert(destQuery, update1, Point.class, "point");

                returnStr = srcPoint.toString() + "" +
                        "" +
                        destPoint.toString();
            } else {
                try {
                    if(destPoint.getPointHashMaps().containsKey(srcPoint.getPointCall())){
                        destPoint.getPointHashMaps().remove(srcPoint.getPointCall());
                    }
                    srcPoint.getPointHashMaps().put(destPoint.getPointCall(),destPoint);
                } catch (NullPointerException npe) {
                    HashMap<String,Point> srcPointHashMap = new HashMap<>();
                    srcPointHashMap.put(destPoint.getPointCall(),destPoint);
                    srcPoint.setPointHashMaps(srcPointHashMap);
                }

                /*mongoTemplate.remove(srcQuery,Point.class);
                mongoTemplate.insert(srcPoint);*/

                Document doc2 = new Document();
                mongoTemplate.getConverter().write(srcPoint, doc2);
                Update update2 = Update.fromDocument(doc2);
                mongoTemplate.upsert(srcQuery, update2, Point.class, "point");

                returnStr = srcPoint.toString();
            }
        } else {
            return "srcPoint or destPoint is not set.";
        }
        return returnStr;
    }

    public List<Point> letsSeeAllPoints() {
        return mongoTemplate.findAll(Point.class);
    }

    public Point getPoint(String pointCall, String personId) {
        List<Criteria> criteriaList = new ArrayList<Criteria>();
            criteriaList.add(Criteria.where("personId").is(Long.valueOf(personId)));
            criteriaList.add(Criteria.where("pointCall").is(pointCall));
        Query query = new Query();
            query.addCriteria(new Criteria().andOperator(criteriaList));
        return (Point) mongoTemplate.findOne(query,Point.class);
    }

    public Medias getMedia(String filename) throws IOException {
        GridFSFile gridFSFile = gridFsOperations.getResource(filename).getGridFSFile();
        Medias mediaFile = new Medias();
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            mediaFile.setFilename( gridFSFile.getFilename() );
            mediaFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );
            mediaFile.setFileSize(gridFSFile.getMetadata().size());
            mediaFile.setFile( IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()) );
        }
        return mediaFile;
    }

    public void updatePerson(Person person, String askingScenario) {
        Query query = new Query();
        query.addCriteria(Criteria.where("personId").is(person.getPersonId()));

        if(!askingScenario.isEmpty()){
            person.setLastUsedTestScenario(askingScenario);
        }
        logger.log(Level.INFO, person.toString());

        Document doc = new Document();
        mongoTemplate.getConverter().write(person, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.upsert(query, update, Person.class, "person");
    }
}