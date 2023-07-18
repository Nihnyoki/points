package com.points.point.Entities;

import java.io.Serializable;

public class PointLink {
    private String pointCallSrc;
    private String pointCallDest;
    private Boolean biDirectional;

    public String getPointCallSrc() {
        return pointCallSrc;
    }

    public void setPointCallSrc(String pointCallSrc) {
        this.pointCallSrc = pointCallSrc;
    }

    public String getPointCallDest() {
        return pointCallDest;
    }

    public void setPointCallDest(String pointCallDest) {
        this.pointCallDest = pointCallDest;
    }

    public Boolean getBiDirectional() {
        return biDirectional;
    }

    public void setBiDirectional(Boolean biDirectional) {
        this.biDirectional = biDirectional;
    }
}
