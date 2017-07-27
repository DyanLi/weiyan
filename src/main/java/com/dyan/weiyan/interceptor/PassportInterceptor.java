package com.dyan.weiyan.interceptor;

import com.dyan.weiyan.model.HostHolder;
import com.dyan.weiyan.model.User;
import com.dyan.weiyan.service.UserService;
import com.dyan.weiyan.util.WeiyanUtil;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dyan on 17/7/24.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private String getTicketFromCookie(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (StringUtils.equals(cookie.getName(), "ticket")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("passport preHandle");
        String ticket = getTicketFromCookie(httpServletRequest);
        User user = userService.getUserByTicket(ticket);
        if (user != null) {
            hostHolder.setUser(user);
        } else if (ticket != null) {
            WeiyanUtil.UpdateCookieTicket(httpServletResponse, null, 0);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("passport postHandle");
        if (hostHolder.getUser() != null && modelAndView != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("afterCompletion");
        hostHolder.clear();
    }
}
