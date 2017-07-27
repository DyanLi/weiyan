package com.dyan.weiyan.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyan.weiyan.model.Comment;
import com.dyan.weiyan.model.HostHolder;
import com.dyan.weiyan.model.Message;
import com.dyan.weiyan.model.News;
import com.dyan.weiyan.service.*;
import com.dyan.weiyan.util.WeiyanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dyan on 17/7/26.
 */
@Controller
public class MessageController {
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    @RequestMapping(path = "/msg/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject addMessage (@RequestParam("content") String content,
                              @RequestParam("toName") String toName){
        try{
            content = HtmlUtils.htmlEscape(content);
            int fromId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 1;
            int toId  = userService.getUserByName(toName).getId();

            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(WeiyanUtil.MESSAGE_UNREAD);
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);

            return WeiyanUtil.GetJSON(true, Integer.toString(message.getId()));

        } catch (Exception e ){
            logger.error("信息发布失败"+ e.getMessage());
            return WeiyanUtil.GetJSON(false,"信息发布失败");
        }
    }

    //选出会话fromid_toid
    @RequestMapping(path ="/msg/getMessage/{conversationId}", method = {RequestMethod.POST})
    @ResponseBody
    public String getMessage(@PathVariable("conversationId") String conversationId){
            try{
                List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 2);
                Map<String, Object> conversationMap = new HashMap<String, Object>();
                for (int i =0; i<conversationList.size(); i++ ){
                    conversationMap.put(Integer.toString(i), conversationList.get(i));
                }
                return WeiyanUtil.getJSONString(0,conversationMap);
            } catch (Exception e ){
                return e.getMessage();
            }
    }
}
