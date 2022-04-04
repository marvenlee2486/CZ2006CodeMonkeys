package com.CodeMonkey.saveme.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;

public class Certificate {

    private String key;
    private String AWSAccessKeyId;
    @SerializedName("x-amz-security-token")
    private String amzToken;
    private String policy;
    private String signature;
    private String file;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setS3BucketParameters(S3BucketParameters s3BucketParameters){
        setKey(s3BucketParameters.getKey());
        setAmzToken(s3BucketParameters.getAmzToken());
        setPolicy(s3BucketParameters.getPolicy());
        setAWSAccessKeyId(s3BucketParameters.getAWSAccessKeyId());
        setSignature(s3BucketParameters.getSignature());
    }

}
