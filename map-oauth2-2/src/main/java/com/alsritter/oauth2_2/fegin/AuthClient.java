package com.alsritter.oauth2_2.fegin;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.oauth2_2.service.UserService;
import com.alsritter.serviceapi.auth.domain.RegisterUserTo;
import com.alsritter.serviceapi.auth.domain.UserInfoVO;
import com.alsritter.serviceapi.auth.feign.IAuthClient;
import com.alsritter.serviceapi.user.domain.SecurityUserDto;
import com.alsritter.serviceapi.user.entity.TbUser;
import com.alsritter.serviceapi.user.feign.IUserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthClient implements IAuthClient {

    private final UserService userService;
    // private final RedisTemplate<String, Object> redisTemplate;
    private final ResourceServerTokenServices resourceServerTokenServices;
    private final IUserClient userClient;
    private final PasswordEncoder passwordEncoder;

    private final AccessTokenConverter accessTokenConverter =
            new DefaultAccessTokenConverter();

    @Override
    public void register(@RequestBody @Validated RegisterUserTo user) {
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new BusinessException("????????????", e);
        }
    }

    @Override
    public UserInfoVO getUserInfoByToken(String accessToken) {
        OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(accessToken);
        // ??????????????????????????? Redis ?????? Token ?????????????????? Key ??????????????????????????????????????????
        if (token == null || token.isExpired()) {
            throw new BusinessException(ResultCode.ACCOUNT_EXPIRED);
        }

        OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());
        Map<String, Object> response = (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);

        // ?????????????????????????????????????????????
        if (!response.containsKey("user_name")) {
            throw new BusinessException(ResultCode.ACCESS_REQUIRED_EXCEPTION);
        }

        SecurityUserDto dtoUser = userClient.userInfoByName(response.get("user_name").toString());
        if (dtoUser == null || dtoUser.getUser() == null) {
            throw new BusinessException(ResultCode.ACCOUNT_GET_USER_INFO_EXCEPTION);
        }

        TbUser user = dtoUser.getUser();


        // // ??????????????? Redis ??????
        // redisTemplate.opsForHash().putAll(RedisConstant.USER_INFO_BY_TOKEN + accessToken, infoVO.buildMap());
        // ???????????????????????????????????????????????? Token ?????????????????????????????????????????????????????????

        // ????????? description ????????????
        return UserInfoVO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .createTime(user.getCreateTime())
                .lastModifyTime(user.getLastModifyTime())
                .email(user.getEmail())
                .phone(user.getPhone())
                .lockFlag(user.getLockFlag())
                .status(user.getStatus()).build();

    }

    @Override
    public boolean checkPassword(Long id, String password) {
        TbUser user = userClient.getUser(id);
        return passwordEncoder.matches(password , user.getPassword());
    }

    @Override
    public boolean setNewPassword(Long id, String password) {
        String encode = passwordEncoder.encode(password);
        return userClient.setNewPassword(id, encode);
    }
}
