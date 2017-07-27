package com.dyan.weiyan.controller;

import com.dyan.weiyan.service.UserService;
import com.dyan.weiyan.util.WeiyanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dyan on 17/7/24.
 */
@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/register"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           HttpServletResponse response) {
        try {
            Map<String, String> res = new HashMap<String, String>();
            boolean ret = userService.register(name, email, password, res);
            if (ret) {
                WeiyanUtil.UpdateCookieTicket(response, res.get("ticket"), 3600 * 24 * 10);
                return WeiyanUtil.GetJSON(true).toJSONString();
            } else {
                return WeiyanUtil.GetJSON(ret, res).toJSONString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return WeiyanUtil.GetJSON(false, "注册失败").toJSONString();
        }
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletResponse response) {
        try {
            Map<String, String> res = new HashMap<String, String>();
            boolean ret = userService.login(email, password, res);
            if (ret) {
                WeiyanUtil.UpdateCookieTicket(response, res.get("ticket"), 3600 * 24 * 10);
                return WeiyanUtil.GetJSON(true).toJSONString();
            } else {
                return WeiyanUtil.GetJSON(ret, res).toJSONString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return WeiyanUtil.GetJSON(false, "登陆失败").toJSONString();
        }
    }

    @RequestMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket,
                         HttpServletResponse response) {
        userService.logout(ticket);
        WeiyanUtil.UpdateCookieTicket(response, null, 0);
        return "redirect:/";
    }
}