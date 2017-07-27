package com.dyan.weiyan.service;

import com.dyan.weiyan.dao.LoginTicketDAO;
import com.dyan.weiyan.dao.UserDAO;
import com.dyan.weiyan.model.LoginTicket;
import com.dyan.weiyan.model.User;
import com.dyan.weiyan.util.WeiyanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nowcoder on 2017/7/10.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public boolean register(String name, String email, String password, Map<String, String> res) {

        if (StringUtils.isBlank(name)) {
            res.put("namemsg", "用户名不能为空");
            return false;
        }

        if (StringUtils.isBlank(email)) {
            res.put("emailmsg", "邮箱不能为空");
            return false;
        }

        if (StringUtils.isBlank(password)) {
            res.put("passwordmsg", "密码不能为空");
            return false;
        }

        User u = userDAO.selectByEmail(email);
        if (u != null) {
            res.put("emailmsg", "当前邮箱已经被注册");
            return false;
        }

        // TODO: 邮箱格式检测
        u = new User();
        u.setName(name);
        u.setEmail(email);
        // TODO: 随机设置头像
        u.setHeadUrl("http://www.baidu.com/1.png");
        // TODO: MD5 salt
        u.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        u.setPassword(password);

        userDAO.addUser(u);

        res.put("ticket", addLoginTicket(u.getId()));
        res.put("msg", "注册成功");
        return true;
    }

    public boolean login(String email, String password, Map<String, String> res) {
        if (StringUtils.isBlank(email)) {
            res.put("emailmsg", "邮箱不能为空");
            return false;
        }

        if (StringUtils.isBlank(password)) {
            res.put("passwordmsg", "密码不能为空");
            return false;
        }

        User u = userDAO.selectByEmail(email);

        if (u == null) {
            res.put("emailmsg", "当前邮箱不存在");
            return false;
        }

        if (!StringUtils.equals(password, u.getPassword())) {
            res.put("passwordmsg", "密码错误");
            return false;
        }

        res.put("ticket", addLoginTicket(u.getId()));
        res.put("msg", "登录成功");
        return true;
    }

    public boolean logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, WeiyanUtil.TICKET_EXPIRED);
        return true;
    }

    public User getUserByTicket(String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return null;
        }
        LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
        if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() == WeiyanUtil.TICKET_EXPIRED) {
            return null;
        }

        return userDAO.selectById(loginTicket.getUserId());
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(WeiyanUtil.TICKET_NORMAL);
        loginTicket.setUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicket.setExpired(DateUtils.addDays(new Date(), 30));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }
}
