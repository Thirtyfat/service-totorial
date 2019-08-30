package com.mglj.totorial.framework.tool.coordinate.domain;

/**
 * Created by zsp on 2019/1/22.
 */
public class ServerSequenceConfig {

    public final static int DEFAULT_TIMESTAMP_BITS = 41;
    public final static int DEFAULT_SERVER_SEQUENCE_BITS = 10;
    public final static int DEFAULT_INCREMENT_BITS = 12;
    public final static long DEFAULT_TIMESTAMP_OFFSET = 0L;
    public final static float DEFAULT_EMPTY_SLOT_FETCH_FACTOR = 2.0f;

    /**
     * 时间戳位数
     */
    private int timestampBits = DEFAULT_TIMESTAMP_BITS;
    /**
     * 服务节点序列号位数
     */
    private int serverSequenceBits = DEFAULT_SERVER_SEQUENCE_BITS;
    /**
     * 自增序列号位数
     */
    private int incrementBits = DEFAULT_INCREMENT_BITS;
    /**
     * 时间戳的偏移量
     */
    private long timestampOffset = DEFAULT_TIMESTAMP_OFFSET;
    /**
     * 调节获取空槽位个数的因子
     */
    private float emptySlotFetchFactor = DEFAULT_EMPTY_SLOT_FETCH_FACTOR;

    public int getTimestampBits() {
        return timestampBits;
    }

    public void setTimestampBits(int timestampBits) {
        this.timestampBits = timestampBits;
    }

    public int getServerSequenceBits() {
        return serverSequenceBits;
    }

    public void setServerSequenceBits(int serverSequenceBits) {
        this.serverSequenceBits = serverSequenceBits;
    }

    public int getIncrementBits() {
        return incrementBits;
    }

    public void setIncrementBits(int incrementBits) {
        this.incrementBits = incrementBits;
    }

    public long getTimestampOffset() {
        return timestampOffset;
    }

    public void setTimestampOffset(long timestampOffset) {
        this.timestampOffset = timestampOffset;
    }

    public float getEmptySlotFetchFactor() {
        return emptySlotFetchFactor;
    }

    public void setEmptySlotFetchFactor(float emptySlotFetchFactor) {
        this.emptySlotFetchFactor = emptySlotFetchFactor;
    }
}
