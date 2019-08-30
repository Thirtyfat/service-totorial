package com.mglj.totorial.framework.support.domain.dto;

/**
 * Created by zsp on 2018/11/2.
 */
public class CacheDTO {

    private String name;
    private String description;
    private String type;
    private boolean started;
    private int expiredSeconds;
    private Long hits;
    private Long emptyHits;
    private Long misses;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
    public int getExpiredSeconds() {
        return expiredSeconds;
    }
    public void setExpiredSeconds(int expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }
    public Long getHits() {
        return hits;
    }
    public void setHits(Long hits) {
        this.hits = hits;
    }
    public Long getEmptyHits() {
        return emptyHits;
    }
    public void setEmptyHits(Long emptyHits) {
        this.emptyHits = emptyHits;
    }
    public Long getMisses() {
        return misses;
    }
    public void setMisses(Long misses) {
        this.misses = misses;
    }

}
