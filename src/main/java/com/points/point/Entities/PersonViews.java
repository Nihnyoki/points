package com.points.point.Entities;

public class PersonViews {
    public static interface ExternalPersonView {

    }
    public static interface InternalPersonView extends PersonViews.ExternalPersonView {
    }
}
