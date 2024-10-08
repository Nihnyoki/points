package com.points.point.Repository;

import com.points.point.Entities.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends  MongoRepository<Person, String> {

}
