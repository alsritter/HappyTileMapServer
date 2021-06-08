package com.alsritter.service.user.controller;

import com.alsritter.service.user.service.TbUserService;
import com.alsritter.serviceapi.user.entity.TbUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    private final TbUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<TbUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
