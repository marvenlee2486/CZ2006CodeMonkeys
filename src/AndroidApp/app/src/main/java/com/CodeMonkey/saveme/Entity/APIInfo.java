package com.CodeMonkey.saveme.Entity;

public class APIInfo {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "APIInfo{" +
                "status='" + status + '\'' +
                '}';
    }
}
