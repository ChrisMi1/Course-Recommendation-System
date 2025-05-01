package com.uniwa.course_recommendation.entity;

public abstract class DbEntity {

    public abstract Long getUniqueID();

    public abstract String getEntityTitle();

    public String getSimpleLabel() {
        return String.valueOf(getUniqueID());
    }
    public String getDetailedLabel() {
        return getUniqueID() + " - " + getSimpleLabel();
    }
}
