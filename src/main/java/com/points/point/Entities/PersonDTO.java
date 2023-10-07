package com.points.point.Entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private Long personId;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private String name;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private String surname;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private Gender gender;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private Integer age;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private String cellphone;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private String whatsapp;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    private String email;
    @JsonView({PersonViews.InternalPersonView.class,PersonViews.ExternalPersonView.class})
    @Nullable
    private List<Point> points;

    public PersonDTO() {
        if(points == null){
            this.points = new ArrayList<Point>();
        }
    }


    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Point> getPoints() {
        if(points.isEmpty()){
            points = new ArrayList<Point>();
        }
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "{" +
                "personId=" + personId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", cellphone='" + cellphone + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", email='" + email + '\'' +
                ", points=" + points +
                '}';
    }
}
