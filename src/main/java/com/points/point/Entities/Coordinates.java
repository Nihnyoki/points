package com.points.point.Entities;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

public class Coordinates {
    private Float xlr, yud, zfb;

    @Override
    public String toString() {
        return "{" +
                "xlr=" + xlr +
                ", zfb=" + zfb +
                ", yud=" + yud +
                '}';
    }

    public Float getXlr() {
        return xlr;
    }

    public void setXlr(Float xlr) {
        this.xlr = xlr;
    }

    public Float getYud() {
        return yud;
    }

    public void setYud(Float yud) {
        this.yud = yud;
    }

    public Float getZfb() {
        return zfb;
    }

    public void setZfb(Float zfb) {
        this.zfb = zfb;
    }
}