package com.atguigu.yygh.client;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
public interface DictFeignClient {

    @GetMapping(value = "/admin/cmn/dict/getName/{parentDictCode}/{value}")
    public String getName(
            @PathVariable("parentDictCode") String parentDictCode,
            @PathVariable("value") String value);

    @GetMapping(value = "/getName/{value}")
    public String getName(
            @PathVariable("value") String value);


}
