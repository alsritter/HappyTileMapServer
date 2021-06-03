package com.alsritter.mapuaa.controller;

import com.alsritter.mapuaa.service.UserService;
import com.alsritter.mapuaa.util.JwtUtil;
import com.alsritter.mapuaa.vo.AuthenticationRequest;
import com.alsritter.mapuaa.vo.AuthenticationResponse;
import com.alsritter.mapuaa.vo.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@Setter(onMethod_ = {@Autowired})
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        Authentication authenticate = null;
        try {
            // 这里一步就是校验用户身份，点进去这个 authenticate（默认是 ProviderManager 这个实现类）
            // 它的参数类型是一个 Authentication，即传入一个未认证的 Authentication 进去，返回一个
            // 已经认证的 Authentication 出来
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new Exception("登陆错误", e);
        }

        // 其实如果上面已经认证通过了，这里的 (UserDetails) authenticate.getPrincipal() 其实也可以使用下面这个方式取得
        // final UserDetails userDetails = myDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        // 不过有些时候会在 AuthenticationProvider 里面注入一些权限角色进这个 UserDetails 里面的 getAuthorities(); 方法里面
        final String jwt = jwtUtil.generateToken((UserDetails) authenticate.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {

        ResponseEntity<AuthenticationResponse> result = null;

        userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword());

        try {
            result = createAuthenticateToken(new AuthenticationRequest(
                    registerRequest.getUsername(),
                    registerRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
