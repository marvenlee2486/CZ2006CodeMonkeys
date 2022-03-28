package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;

public class UserRsp {

    private String statusCode;
    private JsonObject headers;
    private User body;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public JsonObject getHeaders() {
        return headers;
    }

    public void setHeaders(JsonObject headers) {
        this.headers = headers;
    }

    public User getBody() {
        return body;
    }

    public void setBody(User body) {
        this.body = body;
    }







}
