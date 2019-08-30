package com.mglj.totorial.framework.tool.mq.rabbit;

import com.mglj.totorial.framework.tool.mq.MessageManager;
import com.mglj.totorial.framework.tool.mq.MqEvent;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.RabbitBinder;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zsp on 2018/9/2.
 */
public class RabbitMessageManager extends MessageManager {

    private final RabbitBinder rabbitBinder;

    public RabbitMessageManager(RabbitBinder rabbitBinder) {
        this.rabbitBinder = rabbitBinder;
    }

    @Override
    protected void doRegistrate(MqEvent event) {
        RabbitEvent rabbitEvent = (RabbitEvent)event;
        rabbitBinder.bind(rabbitEvent);
        List<RabbitEvent> relatedEventList = rabbitEvent.getRelatedEventList();
        if(!CollectionUtils.isEmpty(relatedEventList)) {
            for(RabbitEvent relatedEvent : relatedEventList) {
                if(!isRegistrated(relatedEvent)) {
                    registrate(relatedEvent);
                }
            }
        }
    }
}
