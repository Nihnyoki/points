package com.points.point.Entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.points.point.Entities.Coordinates;
import com.points.point.Entities.Medias;
import com.points.point.Entities.Point;
import com.points.point.Entities.PointViews;

import java.util.ArrayList;

public class PointDTO {
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String pointCall;
    @JsonView({PointViews.InternalPointView.class,PointViews.ExternalPointView.class})
    private Long personId;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private Coordinates coordinates;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String mood;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private ArrayList<String> category;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String subject;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private String note;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private ArrayList<Medias> medias;
    @JsonView({PointViews.ExternalPointView.class, PointViews.InternalPointView.class})
    private ArrayList<Point> points;

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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
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

    public ArrayList<Medias> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Medias> medias) {
        this.medias = medias;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "{" +
                "pointCall='" + pointCall + '\'' +
                ", personId=" + personId +
                ", coordinates=" + coordinates +
                ", mood='" + mood + '\'' +
                ", category=" + category +
                ", subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                ", medias=" + medias +
                ", points=" + points +
                '}';
    }
}
