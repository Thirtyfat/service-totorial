package com.mglj.totorial.framework.tool.op;


import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import com.mglj.totorial.framework.tool.gid.GidGenerator;

import java.util.Objects;

/**
 * Created by zsp on 2018/11/8.
 */
public class FrequentlyOperationPreventHandlerImpl implements FrequentlyOperationPreventHandler {

    private StringOperationTemplate<String, String> stringOperationTemplate;
    private GidGenerator gidGenerator;

    public void setStringOperationTemplate(StringOperationTemplate<String, String> stringOperationTemplate) {
        this.stringOperationTemplate = stringOperationTemplate;
    }

    public void setGidGenerator(GidGenerator gidGenerator) {
        this.gidGenerator = gidGenerator;
    }

    @Override
    public boolean validate(String requestId, long seconds) {
        Objects.requireNonNull(requestId);
        if(seconds < 1) {
            throw new IllegalArgumentException("The seconds should be larger than 0.");
        }
        Boolean hasKey = stringOperationTemplate.hasKey(requestId);
        if(hasKey == null || !hasKey) {
            stringOperationTemplate.set(requestId, "1", seconds);
            return true;
        }
        return false;
    }
}
