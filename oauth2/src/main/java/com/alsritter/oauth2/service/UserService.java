package com.alsritter.oauth2.service;

import cn.hutool.json.JSONUtil;
import com.alsritter.oauth2.constant.MessageConstant;
import com.alsritter.oauth2.domain.SecurityUser;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 从远程的用户服务取得对应的用户
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final IUserClient userClient;
    private final PasswordEncoder passwordEncoder;

    // private List<UserDTO> userList;

    // @PostConstruct
    // public void initData() {
    //     String password = passwordEncoder.encode("123456");
    //     userList = new ArrayList<>();
    //     userList.add(new UserDTO(1L, "alsritter", password, 1, CollUtil.toList("ADMIN")));
    //     userList.add(new UserDTO(2L, "andy", password, 1, CollUtil.toList("TEST")));
    // }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里测试生成个密码
        // log.info(passwordEncoder.encode("123456"));
        // log.info(userClient.toString());


        // 这里需要检查返回值
        ResponseEntity<SecurityUserDto> jwt = null;

        try {
            // feign.codec.DecodeException 异常多半是因为构造的 Result对象没有 空的构造器
            jwt = userClient.userInfoByName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jwt == null ||
                jwt.getStatusCode().isError() ||
                Objects.requireNonNull(jwt.getBody()).getUser() == null)
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);

        SecurityUserDto body = jwt.getBody();
        // 将从 feign 取得的数据构造成 SecurityUser
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUser(body.getUser());
        securityUser.setPermissions(body.getPermission().stream()
                .map(x -> new SimpleGrantedAuthority(x.getEnname())).collect(Collectors.toList()));

        log.info(JSONUtil.toJsonStr(securityUser));
        boolean matches = passwordEncoder.matches("123456", securityUser.getPassword());
        // 判断密码是否正确
        log.debug("判断密码是否正确: {}", matches);


        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }

        return securityUser;
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     List<UserDTO> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
    //     if (CollUtil.isEmpty(findUserList)) {
    //         throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
    //     }
    //     SecurityUser securityUser = new SecurityUser(findUserList.get(0));
    //     if (!securityUser.isEnabled()) {
    //         throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
    //     } else if (!securityUser.isAccountNonLocked()) {
    //         throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
    //     } else if (!securityUser.isAccountNonExpired()) {
    //         throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
    //     } else if (!securityUser.isCredentialsNonExpired()) {
    //         throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
    //     }
    //     return securityUser;
    // }
}
