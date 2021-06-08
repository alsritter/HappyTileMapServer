package com.alsritter.oauth2.config;

import com.alsritter.oauth2.provider.EmailAuthenticationProvider;
import com.alsritter.oauth2.provider.PasswordAuthenticationProvider;
import com.alsritter.oauth2.provider.PhoneAuthenticationProvider;
import com.alsritter.oauth2.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 配置 Spring Security
 * <p>
 * 这里参考自：SpringCloud OAuth2 JWT认证 多种登录方式
 * https://juejin.cn/post/6914933029836324871
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 注入三个 provider，对应密码，手机号，邮箱三种登录方式
     */
    @Bean
    PasswordAuthenticationProvider passwordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }

    @Bean
    PhoneAuthenticationProvider phoneAuthenticationProvider() {
        return new PhoneAuthenticationProvider();
    }

    @Bean
    EmailAuthenticationProvider emailAuthenticationProvider() {
        return new EmailAuthenticationProvider();
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) {
        // 忽略哪些资源不用 security 来管理
        web.ignoring().antMatchers("/rsa/publicKey");  // 注意不要忽略 /oauth/token 不然它不会走过滤器
    }

    /**
     * 认证用户的来源
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);

        // //往管理器中添加provider
        // auth.authenticationProvider(passwordAuthenticationProvider())
        //         .authenticationProvider(phoneAuthenticationProvider())
        //         .authenticationProvider(emailAuthenticationProvider());
    }

    /**
     * 配置 SpringSecurity 相关信息
     * 允许匿名访问所有接口 主要是 oauth 接口
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 先关闭 CSRF 防护（跨站请求伪造，其实就是使用 Cookie 的那堆屁事，如果使用 JWT 可以直接关闭它）
                .authorizeRequests() // 允许使用 RequestMatcher 实现（即通过URL模式）基于 HttpServletRequest 限制访问
                .antMatchers(HttpMethod.OPTIONS).permitAll();   // 这是跨域请求时浏览器会发的预请求，这里直接放行


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    //禁用session
    }
}
