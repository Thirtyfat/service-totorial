package com.mglj.totorial.framework.gid.domain;

/**
 * id生成服务实体
 * Created by yj on 2019/8/29.
 **/
public class GidServer  {

    public final static String IP_SEQUENCE_CONFLICT = "ip-or-sequence-conflict";
    /**
     * 编号
     */
    private Integer id;
    /**
     * ip地址
     */
    private String ip ;
    /**
     * 序号
     */
    private Integer sequence;

    /**
     * 设置编号
     */
    public void setId(Integer value) {
        this.id = value;
    }
    /**
     * 获取编号
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置ip地址
     */
    public void setIp(String value) {
        this.ip = value;
    }
    /**
     * 获取ip地址
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * 设置序号
     */
    public void setSequence(Integer value) {
        this.sequence = value;
    }
    /**
     * 获取序号
     */
    public Integer getSequence() {
        return this.sequence;
    }

}
