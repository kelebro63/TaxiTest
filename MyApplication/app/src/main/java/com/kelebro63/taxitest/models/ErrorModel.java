package com.kelebro63.taxitest.models;


public class ErrorModel {
    public static final String NOT_AUTHORIZED = "not_authorized";
    public static final String ACCESS_DENIED = "access_denied";
    public static final String PROFILE_BLOCKED = "profile_blocked";
    public static final String ALREADY_ASSIGNED = "already_assigned";
    public static final String ALREADY_TOOK = "already_took";

    private String error;
    private String message;

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
