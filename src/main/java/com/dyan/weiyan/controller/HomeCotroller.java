package com.dyan.weiyan.controller;

import com.dyan.weiyan.model.HostHolder;
import com.dyan.weiyan.service.NewsService;
import com.dyan.weiyan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Dyan on 17/7/24.
 */
@Controller
public class HomeCotroller {
//    @Autowired
//    NewsService newsService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    HostHolder hostHolder;

    @RequestMapping(path = "/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String index(){
        return "hello 你好 世界 ";

    }


}
