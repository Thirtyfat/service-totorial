package com.mglj.totorial.framework.tool.data.redis.tool;

/**
 * Created by zsp on 2018/9/20.
 */
public class DirectAccessRequest {

    private String namespace;
    private String domain;
    private String key;
    private String value;
    private Long expiredSeconds;
    private Integer bucketSize = 1;
    private Integer scanSize = 100;

    public DirectAccessRequest() {

    }

    public DirectAccessRequest(String namespace, String domain, String key) {
        this.namespace = namespace;
        this.domain = domain;
        this.key = key;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpiredSeconds() {
        return expiredSeconds;
    }

    public void setExpiredSeconds(Long expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    public Integer getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(Integer bucketSize) {
        this.bucketSize = bucketSize;
    }

    public Integer getScanSize() {
        return scanSize;
    }

    public void setScanSize(Integer scanSize) {
        this.scanSize = scanSize;
    }

    @Override
    public String toString() {
        return "DirectAccessRequest{" +
                "namespace='" + namespace + '\'' +
                ", domain='" + domain + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", expiredSeconds=" + expiredSeconds +
                ", bucketSize=" + bucketSize +
                ", scanSize=" + scanSize +
                '}';
    }
}
