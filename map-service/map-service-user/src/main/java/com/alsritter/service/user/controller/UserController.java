package com.alsritter.service.user.controller;

import com.alsritter.common.api.CommonResult;
import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.entity.TbUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author alsritter
 * @version 1.0
 **/
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    private final TbUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<TbUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<CommonResult<String>> addUser(@Validated TbUser user) {
        userService.addUser(user);
        return ResponseEntity.ok(CommonResult.success("ok!"));
    }
}
