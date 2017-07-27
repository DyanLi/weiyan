package com.dyan.weiyan.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyan.weiyan.model.*;
import com.dyan.weiyan.service.CommentService;
import com.dyan.weiyan.service.NewsService;
import com.dyan.weiyan.service.QiniuService;
import com.dyan.weiyan.service.UserService;
import com.dyan.weiyan.util.WeiyanUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Dyan on 17/7/24.
 */

@Controller
public class NewsController {
    private static Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = "/addNews", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject addNews(@RequestParam("content") String content,
                              @RequestParam("image") String image){
        try{
            content = HtmlUtils.htmlEscape(content);
            News news = new News();
            if (hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            } else {
                news.setUserId(1);
            }
            news.setContent(content);
            news.setImage(image);
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return WeiyanUtil.GetJSON(true, "信息发布成功");
        } catch (Exception e ){
            logger.error("信息发布失败"+ e.getMessage());
            return WeiyanUtil.GetJSON(false,"信息发布失败");
        }
    }

    //根据newId获取发布微言的内容
    @RequestMapping(path = "/getNews/{newsId}", method = {RequestMethod.POST})
    @ResponseBody
    public String getNews(@PathVariable("newsId") int newsId){
        News news = newsService.getById(newsId);
        return news.getContent().toString();
    }


    //根据offset、limit获取部分最新内容列表
    @RequestMapping(path = "/getLatestNews", method = {RequestMethod.POST})
    @ResponseBody
    public String getNews(@RequestParam("offset") int offset,
                          @RequestParam("limit") int limit){

        int userId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 1;
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        Map<String, Object> userNews = new HashMap<String, Object>();
        for (News news : newsList) {
            userNews.put(userService.getUser(news.getUserId()).getName()+"_"+ news.getId(), news);
        }
        return WeiyanUtil.getJSONString(0, userNews);
    }


    //添加评论
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("content") String content,
                             @RequestParam("id") int entryId,
                             @RequestParam("type") int entryType) {
        try {
            //过滤content,防止xss
            content = HtmlUtils.htmlEscape(content);
            Comment comment = new Comment();
            int userId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 1;
            comment.setUserId(userId);
            comment.setContent(content);
            comment.setEntryId(entryId);
            if ( entryType == CommentType.COMMENT_COMMENT ){
                comment.setEntryType(CommentType.COMMENT_COMMENT);
            }else {
                comment.setEntryType(CommentType.COMMENT_NEWS);
            }

            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            // 更新news里的评论数量
            int count = commentService.getCommentCount(comment.getEntryId());
            newsService.updateCommentCount(comment.getEntryId(), count);
            // 怎么异步化
            return WeiyanUtil.GetJSON(true, "评论发布成功！").toJSONString();
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
            return WeiyanUtil.GetJSON(true, "评论发布失败！").toJSONString();
        }
        //return "redirect:/news/" + entryId;
    }


    //根据newsId获取评论列表
    @RequestMapping(path = {"/listComments"}, method = {RequestMethod.POST})
    @ResponseBody
    public String listComments(@RequestParam("newsId") int newsId) {
        News news = newsService.getById(newsId);
        List<Comment> comments = commentService.getCommentsByEntry(news.getId(), CommentType.COMMENT_NEWS);
        Map<String, Object> userComment = new HashMap<String, Object>();
        for (Comment comment : comments) {
            userComment.put(userService.getUser(comment.getUserId()).getName() +"_"+ comment.getId(), comment);
        }
        return WeiyanUtil.getJSONString(0, userComment);
    }


    @RequestMapping(path = "/uploadImage", method = {RequestMethod.POST})
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return "图片上传失败";
            }
            return fileUrl;
        } catch (Exception e) {
            return "图片上传失败" + e.getMessage();
        }
    }


    // get image from local
    @RequestMapping(path = {"/getImage"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("imageName") String imageName,
                         HttpServletResponse response) {

        try {
              response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(WeiyanUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        } catch (Exception e){
            logger.error("读取图片错误" +  e.getMessage());
        }
    }
}
