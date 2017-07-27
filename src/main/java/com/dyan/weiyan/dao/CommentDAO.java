package com.dyan.weiyan.dao;

import com.dyan.weiyan.model.Comment;
import com.dyan.weiyan.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Dyan on 17/7/25.
 */

@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " content, entry_id, entry_type, created_date, user_id ";
    String SELECT_FIELDS = " id, content, entry_id, entry_type, created_date, user_id, status ";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{entryId},#{entryType},#{createdDate},#{userId})"})
    int addComment(Comment comment);

    @Update({"update ", TABLE_NAME, " set status=#{status} where entry_id=#{entryId} and entry_type=#{entryType}"})
    void updateStatus(@Param("entryId") int entryId, @Param("entryType") int entryType, @Param("status") int status);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME,
            " where entry_id=#{entryId} and entry_type=#{entryType} order by id desc"})
    List<Comment> selectByEntry(@Param("entryId") int entryId, @Param("entryType") int entryType);

    @Select({"select count(id) from ", TABLE_NAME, " where entry_id=#{entryId}"})
    int getCommentCount(@Param("entryId") int entryId);

    
    
}
