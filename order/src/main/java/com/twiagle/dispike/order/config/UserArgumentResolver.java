package com.twiagle.dispike.order.config;


import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.exception.GlobalException;
import com.twiagle.dispike.common.result.CodeMsg;
import com.twiagle.dispike.order.redis.RedisService;
import com.twiagle.dispike.order.redis.SpikeUserPrefix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == SpikeUser.class;
    }

    @Override
    public SpikeUser resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String requestParameter = request.getParameter(COOKIE_NAME_TOKEN);
        String cookieParameter = getCookieValue(request, COOKIE_NAME_TOKEN);
        String token;
        if (!StringUtils.isEmpty(requestParameter)) {
            token = requestParameter;
        } else if (!StringUtils.isEmpty(cookieParameter)) {
            token = cookieParameter;
        } else
            return null;
        SpikeUser spikeUser = getByToken(response, token);
        if (null == spikeUser) throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        return spikeUser;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie.getValue();
        }
        return null;
    }

    private void reviseCookie(HttpServletResponse response, String key, String value, int expiredSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SpikeUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token))
            return null;

        SpikeUser spikeUser = redisService.get(SpikeUserPrefix.token, token, SpikeUser.class);
        if (spikeUser != null)
            reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.token.expireSeconds());//update cookie expire
        return spikeUser;
    }
}
