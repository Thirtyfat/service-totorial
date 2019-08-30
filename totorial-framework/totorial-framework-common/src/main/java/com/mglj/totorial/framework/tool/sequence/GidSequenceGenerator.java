package com.mglj.totorial.framework.tool.sequence;

import com.mglj.totorial.framework.tool.gid.GidGenerator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zsp on 2018/9/21.
 */
public class GidSequenceGenerator implements SequenceGenerator {

    @Autowired
    private GidGenerator gidGenerator;

    @Override
    public long generate() {
        return gidGenerator.generate();
    }

}
