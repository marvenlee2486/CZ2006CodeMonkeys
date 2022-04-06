package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;
import java.sql.Timestamp;

public class S3BucketParameters {

    private String key;
    private String AWSAccessKeyId;
    @SerializedName("x-amz-security-token")
    private String amzToken;
    private String policy;
    private String signature;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAWSAccessKeyId() {
        return AWSAccessKeyId;
    }

    public void setAWSAccessKeyId(String AWSAccessKeyId) {
        this.AWSAccessKeyId = AWSAccessKeyId;
    }

    public String getAmzToken() {
        return amzToken;
    }

    public void setAmzToken(String amzToken) {
        this.amzToken = amzToken;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "S3BucketParameters{" +
                "key='" + key + '\'' +
                ", AWSAccessKeyId='" + AWSAccessKeyId + '\'' +
                ", amzToken='" + amzToken + '\'' +
                ", policy='" + policy + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
