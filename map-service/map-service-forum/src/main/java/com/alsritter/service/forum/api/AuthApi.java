package com.alsritter.service.forum.api;

import com.alsritter.common.api.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@RestController
public class AuthApi {
    @PostMapping("/login")
    public CommonResult<String> login() {


        return CommonResult.success("this is test token");
    }
}
