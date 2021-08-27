package com.alsritter.oauth2_2.service;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.common.token.SecurityUser;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
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
        SecurityUser securityUser = getUserByName(username);

        if (!securityUser.isEnabled()) {
            throw new DisabledException(ResultCode.ACCOUNT_DISABLED.getMessage());
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(ResultCode.ACCOUNT_LOCKED.getMessage());
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(ResultCode.ACCOUNT_EXPIRED.getMessage());
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(ResultCode.CREDENTIALS_EXPIRED.getMessage());
        }

        return securityUser;
    }


    public SecurityUser getUserByName(String username) {
        // 这里需要检查返回值
        SecurityUserDto body = null;

        try {
            // feign.codec.DecodeException 异常多半是因为构造的 Result对象没有 空的构造器
            body = userClient.userInfoByName(username);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        if (Objects.requireNonNull(body).getUser() == null)
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);

        return buildSecurityUser(body);
    }

    public SecurityUser getUserPhone(String phone) {
        SecurityUserDto body = null;
        try {
            body = userClient.userInfoByPhone(phone);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        if (Objects.requireNonNull(body).getUser() == null)
            throw new BusinessException(ResultCode.PHONE_NOT_FOUND_EXCEPTION);

        return buildSecurityUser(body);
    }

    public SecurityUser getUserByEmail(String email) {
        SecurityUserDto body = null;
        try {
            body = userClient.userInfoByEmail(email);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        if (Objects.requireNonNull(body).getUser() == null)
            throw new BusinessException(ResultCode.EMAIL_NOT_FOUND_EXCEPTION);

        return buildSecurityUser(body);
    }

    /**
     * 将从 feign 取得的数据构造成 SecurityUser
     */
    private SecurityUser buildSecurityUser(SecurityUserDto body) {
        //
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserAccount(body.getUser().getUsername());
        securityUser.setUserPassword(body.getUser().getPassword());
        securityUser.setPermissions(body.getRoles().stream()
                .map(x -> new SimpleGrantedAuthority(x.getEnname())).collect(Collectors.toList()));
        return securityUser;
    }

    /**
     * 注册一个用户
     */
    public void registerUser(RegisterUserTo user) {
        TbUser ruser = new TbUser();
        ruser.setPassword(passwordEncoder.encode(user.getPassword()));
        ruser.setUsername(user.getUsername());
        ruser.setCreateTime(LocalDateTime.now());
        ruser.setLastModifyTime(LocalDateTime.now());
        ruser.setDescription(user.getDescription());
        ruser.setEmail(user.getEmail());
        ruser.setPhone(user.getPhone());

        userClient.addUser(ruser);
    }
}
