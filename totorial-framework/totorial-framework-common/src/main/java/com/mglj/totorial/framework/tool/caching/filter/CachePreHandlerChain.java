package com.mglj.totorial.framework.tool.caching.filter;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Created by zsp on 2018/8/10.
 */
public class CachePreHandlerChain implements CachePreHandler {

    private final List<CachePreHandler> handlerList = new CopyOnWriteArrayList<>();

    public CachePreHandlerChain() {

    }

    public CachePreHandlerChain(List<CachePreHandler> handlerList) {
        this.handlerList.addAll(handlerList);
    }

    public void add(CachePreHandler handler) {
        if(handler == null) {
            return;
        }
        handlerList.add(handler);
    }

    public void remove(CachePreHandler handler) {
        if(handler == null) {
            return;
        }
        handlerList.remove(handler);
    }

    @Override
    public <K> PreHandleResult isAllowed(K key) {
        return isAllowed0((handler) -> handler.isAllowed(key));
    }

    @Override
    public <K> PreHandleResult isAllowed(Collection<K> keys) {
        return isAllowed0((handler) -> handler.isAllowed(keys));
    }

    private PreHandleResult isAllowed0(Function<CachePreHandler, PreHandleResult> predicate) {
        if(!CollectionUtils.isEmpty(handlerList)) {
            for(CachePreHandler handler : handlerList) {
                PreHandleResult result = predicate.apply(handler);
                if(!result.wasOk()) {
                    return result;
                }
            }
        }

        return new PreHandleResult();
    }

}
