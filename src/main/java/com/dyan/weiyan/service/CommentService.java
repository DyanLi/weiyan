package com.dyan.weiyan.service;

import com.dyan.weiyan.dao.CommentDAO;
import com.dyan.weiyan.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import com.dyan.weiyan.dao.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dyan on 17/7/25.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentsByEntry(int entryId, int entryType) {
        return commentDAO.selectByEntry(entryId, entryType);
    }

    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entryId) {
        return commentDAO.getCommentCount(entryId);
    }

    public void deleteComment(int entryId, int entryType) {
        commentDAO.updateStatus(entryId, entryType, 1);
    }
}
