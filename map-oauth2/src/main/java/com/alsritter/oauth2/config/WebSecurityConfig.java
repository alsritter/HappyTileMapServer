package com.alsritter.oauth2.config;

import com.alsritter.oauth2.provider.EmailAuthenticationProvider;
import com.alsritter.oauth2.provider.PasswordAuthenticationProvider;
import com.alsritter.oauth2.provider.PhoneAuthenticationProvider;
import com.alsritter.oauth2.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

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

    /**
     * 这里把 UsernamePasswordAuthenticationFilter 换成自定义的 AuthenticationProcessingFilter 过滤器
     * <p>
     * 先来回顾一下默认的认证过程：
     * <p>
     * 1、用户提交用户名、密码被 SecurityFilterChain 中的 UsernamePasswordAuthenticationFilter 过滤器获取到，
     * 封装为请求 Authentication，通常情况下是 UsernamePasswordAuthenticationToken 这个实现类。所以这里替换为自己的
     * <p>
     * 2、然后过滤器将 Authentication 提交至认证管理器（AuthenticationManager）进行认证
     * <p>
     * 3、认证成功后， AuthenticationManager 身份管理器返回一个被填充满了信息的
     * （包括权限信息，身份信息，细节信息，但密码通常会被移除） Authentication 实例。
     * <p>
     * 4、SecurityContextHolder 安全上下文容器通过 `SecurityContextHolder.getContext().setAuthentication(…)`
     * 方法将第 3 步填充了信息的 Authentication 填充进来。
     * <p>
     * <p>
     * 可以看出 AuthenticationManager 接口（认证管理器）是认证相关的核心接口，也是发起认证的出发点，它的实现类为 ProviderManager。
     * 而 Spring Security 支持多种认证方式，因此 ProviderManager 维护着一个 `List<AuthenticationProvider>` 列表，存放多种认证方式，
     * 最终实际的认证工作是由 AuthenticationProvider 完成的。
     */
    AuthenticationProcessingFilter authenticationProcessingFilter(AuthenticationManager authenticationManager) {
        AuthenticationProcessingFilter userPasswordAuthenticationProcessingFilter = new AuthenticationProcessingFilter();

        //为filter设置管理器
        userPasswordAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);

        //登录成功后通过端点生成令牌
        userPasswordAuthenticationProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        //登录失败后跳转
        userPasswordAuthenticationProcessingFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        return userPasswordAuthenticationProcessingFilter;
    }

    //核心：配置管理器
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        //往管理器中添加provider
        auth.authenticationProvider(passwordAuthenticationProvider())
                .authenticationProvider(phoneAuthenticationProvider())
                .authenticationProvider(emailAuthenticationProvider());
    }


    /**
     * 认证用户的来源
     * <p>
     * 补充资料：authenticationProvider
     * <p>
     * AuthenticationManager 接口（认证管理器）是认证相关的核心接口，也是发起认证的出发点，它的实现类为 ProviderManager。
     * 而 Spring Security 支持多种认证方式，因此 ProviderManager 维护着一个 `List<AuthenticationProvider>` 列表，
     * 存放多种认证方式，最终实际的认证工作是由 AuthenticationProvider 完成的。
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
        super.configure(auth);
        //
        //
        // auth.authenticationProvider(passwordAuthenticationProvider())
        //         .authenticationProvider(phoneAuthenticationProvider())
        //         .authenticationProvider(emailAuthenticationProvider());
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
     * 配置 SpringSecurity 相关信息
     * 允许匿名访问所有接口 主要是 oauth 接口
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 先关闭 CSRF 防护（跨站请求伪造，其实就是使用 Cookie 的那堆屁事，如果使用 JWT 可以直接关闭它）
                .authorizeRequests() // 允许使用 RequestMatcher 实现（即通过URL模式）基于 HttpServletRequest 限制访问
                .antMatchers(HttpMethod.OPTIONS).permitAll();   // 这是跨域请求时浏览器会发的预请求，这里直接放行


        // 把自定义的过滤器加载 UsernamePasswordAuthenticationFilter 这个默认过滤器前面
        http.addFilterBefore(authenticationProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests()
                .antMatchers(
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.woff2",
                        "/code/image")
                .permitAll();//以上的请求都不需要认证


        // // app 登录的时候我们只要提交的 action，不要跳转到登录页
        // http.formLogin()
        //         //登录页面，app用不到
        //         //.loginPage("/authentication/login")
        //         //登录提交action，app会用到
        //         // 用户名登录地址
        //         .loginProcessingUrl("/form/token")
        //         //成功处理器 返回Token
        //         .successHandler(customAuthenticationSuccessHandler)
        //         //失败处理器
        //         .failureHandler(customAuthenticationFailureHandler);
        //
        // http.csrf().disable();

        // 这里不要禁用 Session 因为需要它保存验证码之类的东西
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    //禁用session
    }
}
