package com.mglj.totorial.framework.tool.coordinate.domain;

import java.util.Date;

/**
 * 应用服务实例
 *
 * Created by zsp on 2018/7/11.
 */
public class Server {

    /**
     * equals to 1 << 10
     */
    public final static int MAX_SERVER_SIZE = 0x0400;
    public final static int MAX_SERVER_SIZE_MASK = 0x03FF;

    /**
     * 标识
     */
    private Long id;
    /**
     *
     */
    private String type;
    /**
     *
     */
    private String purpose;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private String group;
    /**
     *
     */
    private String ip;
    /**
     *
     */
    private Integer port;
    /**
     *
     */
    private Integer sequence;
    /**
     *
     */
    private ServerStatusEnum status;
    /**
     *
     */
    private Date latestStartupTime;

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", type=" + type +
                ", purpose='" + purpose + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", group='" + group + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", sequence=" + sequence +
                ", status=" + status +
                ", latestStartupTime=" + latestStartupTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public ServerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ServerStatusEnum status) {
        this.status = status;
    }

    public Date getLatestStartupTime() {
        return latestStartupTime;
    }

    public void setLatestStartupTime(Date latestStartupTime) {
        this.latestStartupTime = latestStartupTime;
    }
}
