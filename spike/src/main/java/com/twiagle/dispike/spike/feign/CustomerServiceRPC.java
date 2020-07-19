package com.twiagle.dispike.spike.feign;

import com.twiagle.dispike.common.entities.SpikeUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient("customer")
public interface CustomerServiceRPC {
    @ResponseBody
    @GetMapping("/login/{userId}")
    public SpikeUser getById(@PathVariable long userId);
}
