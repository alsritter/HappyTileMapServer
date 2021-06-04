package com.alsritter.mapgateway.filter;

/**
 * @author alsritter
 * @version 1.0
 **/

import com.alsritter.common.util.JwtUtil;
import com.alsritter.mapuaa.config.RsaKeyProperties;
import com.alsritter.mapservice.mapserviceuser.entity.TbUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * OncePerRequestFilter 它能够确保在一次请求只通过一次 filter，而不需要重复执行
 **/
@Component
@Setter(onMethod_ = {@Autowired})
@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private RsaKeyProperties rsaKeyProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // // 标准的 Token 都是从这个 Authorization 里面取得数据的
        final String authorizationHeader = request.getHeader("Authorization");

        //没有登录
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            PrintWriter out = response.getWriter();

            Map<String, Object> map = new HashMap<>(4);
            map.put("code", HttpServletResponse.SC_FORBIDDEN);
            map.put("message", "请登录！");

            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
            return;
        }

        //登录之后从token中获取用户信息（这里用公钥解密）
        String token = authorizationHeader.substring(7);
        TbUser sysUser = JwtUtil.getInfoFromToken(token, rsaKeyProperties.getPublicKey(), TbUser.class).getUserInfo();

        if (sysUser != null) {
            Authentication authResult = new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, sysUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authResult);
            chain.doFilter(request, response);
        }
    }
}