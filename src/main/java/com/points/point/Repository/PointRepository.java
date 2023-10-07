package com.points.point.Repository;

import com.points.point.Entities.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends  MongoRepository<Point, String> {

    @Override
    List<Point> findAllById(Iterable<String> strings);

    Point findByPointCall(String pointCall);
}
