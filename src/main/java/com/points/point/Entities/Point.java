package com.points.point.Entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.annotation.Id;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Document(collection = "point",value = "point" )
public class Point implements Serializable{
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Id
    @JsonView(PointViews.ExternalPointView.class)
    String id;
    @Indexed()
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String pointCall;
    @Indexed()
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private Long personId;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private Coordinates coordinates;
    @Indexed
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String mood;
    @Indexed
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private ArrayList<String> category;
    @Indexed
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String subject;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String note;
    @Nullable
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private LocalDateTime currentDateTime;
    @Nullable
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private LocalDateTime pointDateTime;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private ArrayList<Medias> medias;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private HashMap<String, Point> pointHashMaps;

    public String getPointCall() {
        return pointCall;
    }

    public void setPointCall(String pointCall) {
        this.pointCall = pointCall;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public LocalDateTime getPointDateTime() {
        return pointDateTime;
    }

    public void setPointDateTime(LocalDateTime pointDateTime) {
        this.pointDateTime = pointDateTime;
    }

    public ArrayList<Medias> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Medias> medias) {
        this.medias = medias;
    }

    public HashMap<String, Point> getPointHashMaps() {
        return pointHashMaps;
    }

    public void setPointHashMaps(HashMap<String, Point> pointHashMaps) {
        this.pointHashMaps = pointHashMaps;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", pointCall='" + pointCall + '\'' +
                ", personId=" + personId +
                ", coordinates=" + coordinates +
                ", mood='" + mood + '\'' +
                ", category=" + category +
                ", subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                ", currentDateTime=" + currentDateTime +
                ", pointDateTime=" + pointDateTime +
                ", medias=" + medias +
                ", pointHashMaps=" + pointHashMaps +
                '}';
    }
}
