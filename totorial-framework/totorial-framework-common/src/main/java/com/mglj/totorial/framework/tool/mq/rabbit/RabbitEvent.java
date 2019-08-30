package com.mglj.totorial.framework.tool.mq.rabbit;


import com.mglj.totorial.framework.tool.mq.MqEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsp on 2018/8/31.
 */
public class RabbitEvent implements MqEvent {

    private final static boolean DEFAULT_MANUAL_ACK = true;
    private final static int DEFAULT_PREFETCH_COUNT = 1;
    private final static int DEFAULT_CONCURRENT_CONSUMERS = 1;
    private final static int DEFAULT_TX_SIZE = 1;

    /**
     * 交换机类型
     */
    private ExchangeTypeEnum exchangeType = ExchangeTypeEnum.DIRECT;
    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 路由
     */
    private String routeKey;
    /**
     * 是否是手动确认模式
     */
    private boolean manualAck = DEFAULT_MANUAL_ACK;
    /**
     *
     */
    private int prefetchCount = DEFAULT_PREFETCH_COUNT;
    /**
     *
     */
    private int concurrentConsumers = DEFAULT_CONCURRENT_CONSUMERS;
    /**
     *
     */
    private int txSize = DEFAULT_TX_SIZE;
    /**
     * 消息存活的时间（毫秒）
     */
    private int survivalMillis;
    /**
     * 死信交换机名称
     */
    private String deadLetterExchangeName;
    /**
     * 死信路由
     */
    private String deadLetterRouteKey;
    /**
     * 相关的事件，例如死信
     */
    private List<RabbitEvent> relatedEventList;

    public void addRelatedEvent(RabbitEvent event) {
        if(event == null) {
            return;
        }
        if(relatedEventList == null) {
            relatedEventList = new ArrayList<>();
        }
        relatedEventList.add(event);
    }

    public void removeRelatedEvent(RabbitEvent event) {
        if(event == null) {
            return;
        }
        if(relatedEventList == null) {
            relatedEventList = new ArrayList<>();
        }
        relatedEventList.remove(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RabbitEvent that = (RabbitEvent) o;

        if (exchangeType != that.exchangeType) return false;
        if (exchangeName != null ? !exchangeName.equals(that.exchangeName) : that.exchangeName != null) return false;
        if (queueName != null ? !queueName.equals(that.queueName) : that.queueName != null) return false;
        return routeKey != null ? routeKey.equals(that.routeKey) : that.routeKey == null;
    }

    @Override
    public int hashCode() {
        int result = exchangeType != null ? exchangeType.hashCode() : 0;
        result = 31 * result + (exchangeName != null ? exchangeName.hashCode() : 0);
        result = 31 * result + (queueName != null ? queueName.hashCode() : 0);
        result = 31 * result + (routeKey != null ? routeKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return exchangeType
                + ": exchanger(" + exchangeName
                +"), q(" + queueName
                +"), [" + routeKey + "]";
    }

    public ExchangeTypeEnum getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeTypeEnum exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public boolean isManualAck() {
        return manualAck;
    }

    public void setManualAck(boolean manualAck) {
        this.manualAck = manualAck;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public int getTxSize() {
        return txSize;
    }

    public void setTxSize(int txSize) {
        this.txSize = txSize;
    }

    public int getSurvivalMillis() {
        return survivalMillis;
    }

    public void setSurvivalMillis(int survivalMillis) {
        this.survivalMillis = survivalMillis;
    }

    public String getDeadLetterExchangeName() {
        return deadLetterExchangeName;
    }

    public void setDeadLetterExchangeName(String deadLetterExchangeName) {
        this.deadLetterExchangeName = deadLetterExchangeName;
    }

    public String getDeadLetterRouteKey() {
        return deadLetterRouteKey;
    }

    public void setDeadLetterRouteKey(String deadLetterRouteKey) {
        this.deadLetterRouteKey = deadLetterRouteKey;
    }

    public List<RabbitEvent> getRelatedEventList() {
        return relatedEventList;
    }
}
