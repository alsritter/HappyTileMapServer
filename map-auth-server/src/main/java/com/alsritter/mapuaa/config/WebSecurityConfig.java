package com.alsritter.mapuaa.config;

import com.alsritter.mapuaa.filter.JwtRequestFilter;
import com.alsritter.mapuaa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author alsritter
 * @version 1.0
 **/
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 认证用户的来源
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    // /**
    //  * 配置SpringSecurity相关信息
    //  *
    //  * @param http
    //  * @throws Exception
    //  */
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.csrf().disable()
    //             .authorizeRequests()
    //             // .antMatchers("/r/r1").hasAnyAuthority("p1") // 配置权限
    //             .antMatchers("/login*").permitAll()
    //             .anyRequest().authenticated()
    //             .and()
    //             .formLogin();
    // }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 先关闭 CSRF 防护（跨站请求伪造，其实就是使用 Cookie 的那堆屁事，如果使用 JWT 可以直接关闭它）
        http.csrf().disable()
                .authorizeRequests()
                // 这个 antMatcher 方法用于匹配请求（注意方法名后面要加 's'）
                .antMatchers(HttpMethod.POST, "/authenticate", "/register").permitAll()
                .anyRequest().authenticated()
                // 这里关闭 Session 验证（就是 Cookie-Session 那个）
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 把自己注册的过滤器放在 UsernamePasswordAuthenticationFilter 之前
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
