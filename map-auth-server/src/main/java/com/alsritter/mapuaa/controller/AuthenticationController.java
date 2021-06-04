package com.alsritter.mapuaa.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@Setter(onMethod_ = {@Autowired})
public class AuthenticationController {


    // @PostMapping("/authenticate")
    // public ResponseEntity<AuthenticationResponse> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    //     Authentication authenticate = null;
    //     try {
    //         // 这里一步就是校验用户身份，点进去这个 authenticate（默认是 ProviderManager 这个实现类）
    //         // 它的参数类型是一个 Authentication，即传入一个未认证的 Authentication 进去，返回一个
    //         // 已经认证的 Authentication 出来
    //         authenticate = authenticationManager.authenticate(
    //                 new UsernamePasswordAuthenticationToken(
    //                         authenticationRequest.getUsername(),
    //                         authenticationRequest.getPassword()
    //                 )
    //         );
    //
    //     } catch (BadCredentialsException e) {
    //         throw new Exception("登陆错误", e);
    //     }
    //
    //     // 要清除密码
    //     ((TbUser) authenticate).setPassword(null);
    //
    //     // 生成令牌
    //     final String jwt = JwtUtil.generateTokenExpireInMinutes(authenticate, rsaKeyProperties.getPrivateKey(), 60);
    //     return ResponseEntity.ok(new AuthenticationResponse(jwt));
    // }


    // @PostMapping("/register")
    // public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
    //
    //     ResponseEntity<AuthenticationResponse> result = null;
    //
    //     userService.registerUser(
    //             registerRequest.getUsername(),
    //             registerRequest.getPassword());
    //
    //     try {
    //         result = createAuthenticateToken(new AuthenticationRequest(
    //                 registerRequest.getUsername(),
    //                 registerRequest.getPassword()));
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //
    //     return result;
    // }


    @Secured("QUERY")
    @GetMapping("/query")
    public ResponseEntity<String> testQuery() {
        return ResponseEntity.ok("查询成功~");
    }

    @Secured("DELETE")
    @GetMapping("/delete")
    public ResponseEntity<String> testDelete() {
        return ResponseEntity.ok("删除成功~");
    }

    @Secured("UPDATE")
    @GetMapping("/update")
    public ResponseEntity<String> testUpdate() {
        return ResponseEntity.ok("更新成功~");
    }

    @Secured("INSERT")
    @GetMapping("/insert")
    public ResponseEntity<String> testInsert() {
        return ResponseEntity.ok("插入成功~");
    }
}
