package com.dyan.weiyan.dao;

import com.dyan.weiyan.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Dyan on 17/7/25.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " content, image, created_date, user_id ";
    String SELECT_FIELDS = " id, content, image, like_count, comment_count, created_date, user_id ";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{image},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME,
            " where user_id = #{userId} order by id desc limit #{offset},#{limit}"})
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}