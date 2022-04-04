package com.CodeMonkey.saveme.Entity;

public class CertificateRsp {

    private String url;
    private S3BucketParameters fields;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public S3BucketParameters getFields() {
        return fields;
    }

    public void setFields(S3BucketParameters fields) {
        this.fields = fields;
    }
}
