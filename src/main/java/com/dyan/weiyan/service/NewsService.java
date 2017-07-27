package com.dyan.weiyan.service;

import com.dyan.weiyan.dao.NewsDAO;
import com.dyan.weiyan.model.News;
import com.dyan.weiyan.util.WeiyanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dyan on 17/7/24.
 */

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().indexOf(".");
        if (dotPos < 0){
            return null;
        }
        String fileSuffix = file.getOriginalFilename().substring(dotPos + 1);

        if (! WeiyanUtil.isFileAllowed(fileSuffix)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileSuffix;
        Files.copy(file.getInputStream(), new File(WeiyanUtil.IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return WeiyanUtil.DOMAIN_DIR + "image?name=" + fileName;

    }

}
