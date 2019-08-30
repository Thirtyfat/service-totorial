package com.mglj.totorial.framework.support.facade.impl;

import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.coordinate.domain.Server;
import com.mglj.totorial.framework.tool.coordinate.service.api.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsp on 2019/1/22.
 */
@RestController
public class ServerControllerImpl {

    @Autowired
    private ServerService serverService;

    @PostMapping("/server/test/init")
    public Result<?> initServer() {
        serverService.initServer();
        return Result.result();
    }

    @PostMapping("/server/test/get-or-update")
    public Result<?> testGetSequenceOrUpdateEmptyServer() {
        List<String> users = new ArrayList<>();
        for(int i = 0; i < 500; i++) {
            users.add("u" + i);
        }
        users.parallelStream()
                .forEach(e -> serverService.getSequenceOrUpdateEmptyServer("test", e, 80));
        return Result.result();
    }

    @PostMapping("/server/test/list-empty")
    public Result<List<Server>> listEmptyServer(@RequestBody Integer size) {
        return Result.result(serverService.listEmptyServer(size == null ? 10 : size));
    }

}
